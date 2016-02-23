package flexygames

class Participation implements Comparable<Participation> {

	String statusCode
	String userLog
	Date lastUpdate
	String lastUpdater
	
	public enum Status {
//		REQUESTED ("REQUESTED", "#FF7977"),
//		AVAILABLE("AVAILABLE", "#AAFF95"),
//		DECLINED("DECLINED", "#FFBB7F"),
//		REMOVED("REMOVED", "#BB8888"),
//		APPROVED("APPROVED", "#58FF6B"),
//		DONE_GOOD("DONE_GOOD", "#8F8EFF"),
//		DONE_BAD("DONE_BAD", "#A4D9DF"),
//		UNDONE("UNDONE", "#FF4444"),
		REQUESTED ("REQUESTED", "#F3EEEE"),
		AVAILABLE("AVAILABLE", "#06F6F9"),
		DECLINED("DECLINED", "#FF8497"),
		APPROVED("APPROVED", "#57F78F"),
		WAITING_LIST("WAITING_LIST", "#63B6FF"),
		REMOVED("REMOVED", "#FE6F0F"),
		DONE_GOOD("DONE_GOOD", "#0FF460"),
		DONE_BAD("DONE_BAD", "#FFFC00"),
		UNDONE("UNDONE", "#FF0000"),
		private final String code
		private final String color
		Status (String code, String color) {
			this.code = code
			this.color = color
		}
		String code() {
			return code
		}
		String color() {
			return color
		}
		static String color(String code) {
			for (Status it : Status.values()) {
				if (it.code() == code) return it.color
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Grails stuff
	///////////////////////////////////////////////////////////////////////////
	
	static belongsTo = [player: User, session: Session]

	static transients = ['effective', 'wins', 'defeats', 'draws', 'votingScore', 'actionScore']

	static constraints = {
		player(unique:'session')
		session()
		statusCode(inList: Status.values().collect{it.code()})
		userLog(nullable: true, blank:true, maxSize:100)
		lastUpdate(nullable: true)
		lastUpdater(nullable: true, blank:false)
	}

	static mapping = {
		//cache true
		player fetch: 'join'
		session fetch: 'join'
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Business methods
	///////////////////////////////////////////////////////////////////////////
	
	boolean isEffective () {
		return statusCode == Participation.Status.DONE_GOOD.code || statusCode == Participation.Status.DONE_BAD.code
	}

	List<SessionRound> getWins() {
		def result = []
		for (SessionRound round : session.rounds) {
			def winningTeam
			if (round.playersForTeamA.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_A
			} else if (round.playersForTeamB.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_B
			}
			if (winningTeam) {
				def r = round.getResult()
				if (r == winningTeam) {
					 result << round
				}
			}
		}
		return result
	}
	
	List<SessionRound> getDefeats() {
		def result = []
		for (SessionRound round : session.rounds) {
			def winningTeam
			if (round.playersForTeamA.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_B
			} else if (round.playersForTeamB.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_A
			}
			if (winningTeam) {
				def r = round.getResult()
				if (r == winningTeam) {
					 result << round
				}
			}
		}
		return result
	}
	
	List<SessionRound> getDraws() {
		def result = []
		for (SessionRound round : session.rounds) {
			def r = round.getResult()
			if (round.playersForTeamA.contains(player) || round.playersForTeamB.contains(player)) {
				if (r == SessionRound.Result.DRAW) {
					 result << round
				}
			}
		}
		return result
	}
	
	// Score depending win/defeat (useless)
	def getResultScore() {
		int score = 0
		for (SessionRound round : session.rounds) {
			def winningTeam
			if (round.playersForTeamA.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_A
			} else if (round.playersForTeamB.contains(player)) {
				winningTeam = SessionRound.Result.WON_BY_TEAM_B
			}
			if (winningTeam ) {
				def r = round.getResult()
				if (r == winningTeam) {
					 score += 1
				} /*else {
					// gestion des matchs nuls
					if (r == SessionRound.Result.DRAW) {
						score += 1
					}
				}*/
			}
		}
		return score
	}

	// Score depending action number
	def getActionScore() {
		int score = 0
		session.rounds.each{ round ->
			def actions = GameAction.findAllBySessionRoundAndMainContributor(round, this.player)
			actions.each{score++}
		}
		return score
	}
	
	// Score depending votes
	def getVotingScore() {
		int score = 0
		def votes = Vote.findAllBySessionAndPlayer(this.session, this.player)
		votes.each { vote ->
			score += vote.score
		}
		return score
	}
	
	static int getAllEffectiveCount () {
		def q = Participation.where {
			statusCode == Status.DONE_GOOD.code || statusCode == Status.DONE_BAD.code
		}
		return q.count()
	}
	
	///////////////////////////////////////////////////////////////////////////
	// System methods
	///////////////////////////////////////////////////////////////////////////
	
	String toString() {
		return "Participation of $player for $session"
}

	public boolean equals (Object o) {
		if (o instanceof Participation) {
			return session == o.session && player == o.player && statusCode == o.statusCode && userLog == o.userLog
		}
		return false
	}

	int compareTo(Participation o) {
		int i = statusCode.compareTo(o.statusCode)
		if (i != 0) {
			return i
		}
		if (statusCode == Status.AVAILABLE.code()) {
			i = lastUpdate.compareTo(o.lastUpdate)
			if (i != 0) return i
		}
		i = player.compareTo(o.player)
		if (i != 0) {
			return i
		}
		return lastUpdate.compareTo(o.lastUpdate)
	}
}