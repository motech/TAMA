
<%@ page import="org.motechproject.tama.Patient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
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
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.clinicPatientId.label" default="Clinic Patient Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: patientInstance, field: "clinicPatientId")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.phoneNumber.label" default="Phone Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: patientInstance, field: "phoneNumber")}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.gender.label" default="Gender" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: patientInstance, field: "gender")}</td>
                            
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.dateOfBirth.label" default="Date Of Birth" /></td>
                            
                            <td valign="top" class="value"><g:formatDate format="dd-MM-yyyy" date="${patientInstance?.dateOfBirth}" /></td>
                            
                        </tr>
                    
						<%-- 
                       <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.passcode.label" default="Passcode" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: patientInstance, field: "passcode")}</td>
                            
                        </tr>
                        --%>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="patient.doctor.label" default="Principal Doctor" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: doctor, field: "name")}</td>
                            
                        </tr>
                    
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form id="${patientInstance?.clinicPatientId}" action="edit">
                    <g:hiddenField name="clinicPatientId" value="${patientInstance?.clinicPatientId}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                </g:form>
                <g:form id="${patientInstance?.clinicPatientId}" action="delete">
                    <g:hiddenField name="clinicPatientId" value="${patientInstance?.clinicPatientId}" />
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
