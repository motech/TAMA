
<%@ page import="org.motechproject.tama.Appointment; org.motechproject.tama.Patient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patience.label', default: 'Appointment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body><%--
        <div class="nav">
            <span class="menuButton"><g:link id="${patientId}" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        --%><div class="body">
            <h1>Patient ${patientInstance?.clinicPatientId}</h1><%--
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            --%><g:render template="/include/tabs" model="${['clinicPatientId': patientInstance.clinicPatientId]}" />
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr><%--
                        
                            <g:sortableColumn property="id" title="${message(code: 'appointment.id.label', default: 'Id')}" />
                            <g:sortableColumn property="patientId" title="${message(code: 'appointment.patientId.label', default: 'Patient Id')}" />
                        
                            --%>
                            
                            <g:sortableColumn property="followup" title="${message(code: 'appointment.followup.label', default: 'Followup')}" />
                            <g:sortableColumn property="reminderWindowStart" title="${message(code: 'appointment.reminderWindowStart.label', default: 'Reminder Window Start')}" />
                            <g:sortableColumn property="reminderWindowEnd" title="${message(code: 'appointment.reminderWindowEnd.label', default: 'Reminder Window End')}" />
                            <g:sortableColumn property="date" title="${message(code: 'appointment.date.label', default: 'Date')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${appointmentInstanceList}" status="i" var="appointmentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}"><%--
                        
                            <td><g:link action="show" id="${appointmentInstance.id}">${fieldValue(bean: appointmentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: patientInstance, field: "clinicPatientId")}</td>
                        
                            --%>

            				<td>${fieldValue(bean: appointmentInstance, field: "followup")}</td>
                            <td><g:formatDate format="dd-MM-yyyy" date="${appointmentInstance.reminderWindowStart}" /></td>
                            <td><g:formatDate format="dd-MM-yyyy" date="${appointmentInstance.reminderWindowEnd}" /></td>
                            <td>
                            	<g:if test="${appointmentInstance.date}">
		                            <g:formatDate format="dd-MM-yyyy" date="${appointmentInstance.date}" name="dateOfBirth"/>
								</g:if>
								<g:else>
									<g:link elementId="appointmentDate" id="${patientInstance?.clinicPatientId}" action="list" params="[appointmentInstance:appointmentInstance]">Schedule</g:link>
									<script>
									$(function() {
										$( "#appointmentDate" ).datepicker({
											changeMonth: true,
											changeYear: true,2
											dateFormat: DATE_FORMAT,
											maxDate: 0
										});
									});
									</script>
								</g:else>
                            </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${appointmentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
