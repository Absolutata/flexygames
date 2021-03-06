<%@ page import="flexygames.Session" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="mobile"/>
    <g:javascript library="interact-1.2.4.min" />
</head>

<body>

    <g:render template="title" />

    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="errors">${flash.error}</div>
    </g:if>

    <g:set var="sessionIsManagedByCurrentUser" value="${sessionInstance.isManagedBy(org.apache.shiro.SecurityUtils.subject.principal)}" />

    <g:render template="mobileInfos" />

    <g:render template="mobileParticipants" />

    <g:render template="compositions" />

    <g:render template="scoresheet" />

    <g:render template="votes" />

    <g:render template="mobileComments" />

</body>
</html>
