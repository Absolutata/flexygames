<g:set var="token" value="${sessionInstance.playground.computeForecastToken(sessionInstance.date)}" />
<div id="cont_${token}-" style="text-align: center;">
	<div id="spa_${token}-">
		<a id="a_${token}-" href="http://www.meteocity.com/france/nice_v6088/"
		   target="_blank" style="color: #333; text-decoration: none;">
			<g:message code="forecast" default="Forecast" /> ${city}</a> © meteocity.com
	</div>
	<script type="text/javascript">
		${sessionInstance.playground.generateForecastScript(token)}
	</script>

</div>

