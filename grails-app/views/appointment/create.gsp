

<%@ page import="org.motechproject.tama.model.Appointment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'appointment.label', default: 'Appointment')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${appointmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${appointmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="patientId"><g:message code="appointment.patientId.label" default="Patient Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'patientId', 'errors')}">
                                    <g:textField name="patientId" value="${appointmentInstance?.patientId}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date"><g:message code="appointment.date.label" default="Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'date', 'errors')}">
                                    <g:datePicker name="date" precision="day" value="${appointmentInstance?.date}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reminderWindowEnd"><g:message code="appointment.reminderWindowEnd.label" default="Reminder Window End" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'reminderWindowEnd', 'errors')}">
                                    <g:datePicker name="reminderWindowEnd" precision="day" value="${appointmentInstance?.reminderWindowEnd}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reminderWindowStart"><g:message code="appointment.reminderWindowStart.label" default="Reminder Window Start" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: appointmentInstance, field: 'reminderWindowStart', 'errors')}">
                                    <g:datePicker name="reminderWindowStart" precision="day" value="${appointmentInstance?.reminderWindowStart}"  />
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
