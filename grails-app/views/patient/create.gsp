<%@ page import="org.motechproject.tama.Patient;org.motechproject.tama.Gender" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${patientInstance}">
            <div class="errors">
                <g:renderErrors bean="${patientInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" name="patientForm">
				<g:render template="patientForm"></g:render>
                <div class="buttons">
                    <span class="button"><g:submitButton id="create-btn" name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
