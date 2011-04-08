<%@ page import="org.motechproject.tama.Patient;org.motechproject.tama.Gender" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
		<style type="text/css">
		.dialog table {
		    width:inherit;
		}
		.prop .name {
		    width: 15%;
		}
		.prop .value {
		    width: 85%;
		}
		</style>           
    </head>
    <body>
        <div class="nav">
            <g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
        </div>    
        <div class="body">
            <h1>Patient ${patientInstance?.clinicPatientId}</h1>
            <g:render template="tabs" />
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${patientInstance}">
            <div class="errors">
                <g:renderErrors bean="${patientInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" name="patientForm">
                <g:hiddenField name="id" value="${patientInstance?.id}" />
                <g:hiddenField name="revision" value="${patientInstance?.revision}" />
                <g:render template="patientForm"></g:render>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
