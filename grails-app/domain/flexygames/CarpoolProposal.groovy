package flexygames

class CarpoolProposal {

    Integer freePlaceNbr
    String carDescription
    String rdvDescription
    Session session
    User driver
    Set<CarpoolRequest>  approvedRequests

    static belongsTo = [session: Session]

    static hasMany = [approvedRequests: CarpoolRequest]

    static constraints = {
        driver nullable: false, unique: 'session'
    }
}
