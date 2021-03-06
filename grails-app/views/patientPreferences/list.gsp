<%@ page import="org.motechproject.tama.api.model.Preferences" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patientPreferences.label', default: 'PatientPreferences')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'patientPreferences.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="appointmentReminderEnabled" title="${message(code: 'patientPreferences.appointmentReminderEnabled.label', default: 'Appointment Reminder Enabled')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${patientPreferencesInstanceList}" status="i" var="patientPreferencesInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${patientPreferencesInstance.id}">${fieldValue(bean: patientPreferencesInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatBoolean boolean="${patientPreferencesInstance.appointmentReminderEnabled}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${patientPreferencesInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
