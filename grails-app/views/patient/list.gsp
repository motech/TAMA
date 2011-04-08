<%@ page import="org.motechproject.tama.Patient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
        	<g:link class="create" action="create" class="button right"><g:message code="patient.new"/></g:link>
        </div>
        <div class="body">
	        <h1><g:message code="patient.list"/></h1>
	        <g:if test="${flash.message}">
	        <div class="message">${flash.message}</div>
	        </g:if>
	        <div class="list">
	            <table>
	                <thead>
	                    <tr>
	                    	<th>Clinic Patient Id</th>
	                    	<th>Phone Number</th>
	                    	<th>Gender</th>
	                    	<th>Date Of Birth</th>
	                    	<th>Principal Doctor</th>
	                    <%--
	                        <g:sortableColumn property="clinicPatientId" title="${message(code: 'patient.clinicPatientId.label', default: 'Clinic Patient Id')}" />
	                        <g:sortableColumn property="phoneNumber" title="${message(code: 'patient.phoneNumber.label', default: 'Phone Number')}" />
	                        <g:sortableColumn property="gender" title="${message(code: 'patient.gender.label', default: 'Gender')}" />
	                        <g:sortableColumn property="dateOfBirth" title="${message(code: 'patient.dateOfBirth.label', default: 'Date Of Birth')}" />
	                        <g:sortableColumn property="doctor" title="${message(code: 'patient.doctor.label', default: 'Principal Doctor')}" />
	                    --%>
	                    </tr>
	                </thead>
	                <tbody>
	                <g:each in="${patientInstanceList}" status="i" var="patientInstance">
	                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                    
	                        <td><g:link action="show" id="${patientInstance.clinicPatientId}">${fieldValue(bean: patientInstance, field: "clinicPatientId")}</g:link></td>
	                    
	                        <td>${fieldValue(bean: patientInstance, field: "phoneNumber")}</td>
	                        
	                        <td>${fieldValue(bean: patientInstance, field: "gender")}</td>

	                        <td><g:formatDate format="dd-MM-yyyy" date="${patientInstance.dateOfBirth}" /></td>
	                    
	                        <td><%=doctors.find{it.id==patientInstance.doctorId}.name%></td>
	                        
	                    </tr>
	                </g:each>
	                </tbody>
	            </table>
	        </div>
	        <div class="paginateButtons">
	            <g:paginate total="${patientInstanceTotal}" />
	        </div>
        </div>
    </body>
</html>
