

<%@ page import="org.motechproject.tama.PatientPreferences" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patientPreferences.label', default: 'PatientPreferences')}" />
        <title>Patient Preferences</title>
    </head>
    <body>
        <div class="body">
            <h1>Patient ${patientPreferencesInstance?.clinicPatientId}</h1>
            <g:render template="/include/tabs" model="${['clinicPatientId': patientPreferencesInstance.clinicId]}"  />
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${patientPreferencesInstance}">
            <div class="errors">
                <g:renderErrors bean="${patientPreferencesInstance}" as="list" />
            </div>
            </g:hasErrors>
            <h2>Patient Preferences</h2>
            <g:form action="save" >
            	<g:hiddenField name="clinicPatientId" value="${patientPreferencesInstance.clinicPatientId}" />
                <div class="dialog">
                	<div class="fieldgroup">
	                	<h3>Which types of calls would the patient like to receive:</h3>
	                    <table class="innerTable" cellpadding="0" cellspacing="0">
	                        <tbody>
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <g:checkBox name="appointmentReminderEnabled" value="${patientPreferencesInstance?.appointmentReminderEnabled}" /> 
	                                    <label for="appointmentReminderEnabled"><g:message code="patientPreferences.appointmentReminderEnabled.label" default="Appointment Reminder Enabled" /></label>
	                                </td>
	                                <td class="description">
	                                	TODO: Description of what a patient should expect
	                                </td>
	                            </tr>
	                        
	                        </tbody>
	                    </table>
                    </div>
                	<div class="fieldgroup">
	                	<h3>When is the most convenient time for the patient to receive calls from the IVR?</h3>
	                	<div>Please explain to the patient that TAMA is not a person, just a computer system, so it can call the patient at any time day or night, whenever it is most convenient to the patient.</div>
						<div>
	                        <g:select name="bestTimeToCallHour" value="" from="${0..23}" value="${patientPreferencesInstance?.bestTimeToCallHour}"  /> : 
	                        <g:select name="bestTimeToCallMinute" value="" from="${[00, 10, 20, 30, 40, 50]}"  value="${patientPreferencesInstance?.bestTimeToCallMinute}"  />
						</div>
	                    <div>NB: The IVR will always call the patient at the selected time, except for the "Pill Reminder" calls, which will happen whenever the patient is due to take their pill(s).</div>
                    </div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
