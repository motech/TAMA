

<%@ page import="org.motechproject.tama.Patient;org.motechproject.tama.Gender" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${patientInstance}">
            <div class="errors">
                <g:renderErrors bean="${patientInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="clinicPatientId"><g:message code="patient.clinicPatientId.label" default="Clinic Patient Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'clinicPatientId', 'errors')}">
                                    <g:textField name="clinicPatientId" value="${patientInstance?.clinicPatientId}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="phoneNumber"><g:message code="patient.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${patientInstance?.phoneNumber}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="gender"><g:message code="patient.gender.label" default="Gender" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'gender', 'errors')}">
                                    <g:select name="gender" value="${patientInstance?.gender}" from="${Gender.values()}" optionKey="key"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateOfBirth"><g:message code="patient.dateOfBirth.label" default="Date Of Birth" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'dateOfBirth', 'errors')}">
                                    <g:textField name="dateOfBirth" value="${patientInstance?.dateOfBirth}" />
                                   	<script>
									$(function() {
										$( "#dateOfBirth" ).datepicker({
											changeMonth: true,
											changeYear: true,
											dateFormat: DATE_FORMAT,
											maxDate: 0
										});
									});
									</script>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="passcode"><g:message code="patient.passcode.label" default="Passcode" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'passcode', 'errors')}">
                                    <g:textField name="passcode" value="${patientInstance?.passcode}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="doctorId"><g:message code="patient.doctorId.label" default="Principal Doctor" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: patientInstance, field: 'doctorId', 'errors')}">
                                    <g:select name="doctorId" value="${patientInstance?.doctorId}" from="${doctors}" optionKey="id" optionValue="name"/>
                                </td>
                            </tr>
                        
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
