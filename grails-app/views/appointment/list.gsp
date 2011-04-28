
<%@ page import="org.motechproject.tama.model.Appointment; org.motechproject.tama.model.Patient" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'patience.label', default: 'Appointment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'clinic-visits.css')}" />
        <script type="text/javascript">
		function showMessage(msg){
		    if ($("#message-container").hasClass("empty-message")){
		 		$("#message-container").removeClass("empty-message");
			}
		 	if (!$("#message-container").hasClass("message")){
		 		$("#message-container").addClass("message");
		    }
		 	$("#message-container").html(msg);
		}

		//check if there are unsaved items
	    $(window).bind('beforeunload', function(){
			var warning;
		    $("a.save").each(function(i){
			    if (!$(this).hasClass("hide")) {
			    	warning = true;
			    }
			});
			return warning;
        });
        </script>
    </head>
    <body><%--
        <div class="nav">
            <span class="menuButton"><g:link id="${patientId}" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        --%><div class="body">
            <h1>Patient ${patientInstance?.clinicPatientId}</h1><%--
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            --%><g:render template="/include/tabs" model="${['clinicPatientId': patientInstance.clinicPatientId]}" />
            <div id="message-container" class="empty-message"></div>
            <div id="visit-list" class="list">
                <table>
                    <thead>
                        <tr><%--
                        
                            <g:sortableColumn property="id" title="${message(code: 'appointment.id.label', default: 'Id')}" />
                            <g:sortableColumn property="patientId" title="${message(code: 'appointment.patientId.label', default: 'Patient Id')}" />
                        
                            --%>
                            
                            <g:sortableColumn property="followup" title="${message(code: 'appointment.followup.label', default: 'Followup')}" />
                            <g:sortableColumn property="reminderWindowStart" title="${message(code: 'appointment.reminderWindowStart.label', default: 'Window Starts')}" />
                            <g:sortableColumn property="reminderWindowEnd" title="${message(code: 'appointment.reminderWindowEnd.label', default: 'Window Ends')}" />
                            <g:sortableColumn property="date" title="${message(code: 'appointment.date.label', default: 'Appointment Set For')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${appointmentInstanceList}" status="i" var="appointmentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}"><%--
                        
                            <td><g:link action="show" id="${appointmentInstance.id}">${fieldValue(bean: appointmentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: patientInstance, field: "clinicPatientId")}</td>
                        
                            --%>

            				<td>${fieldValue(bean: appointmentInstance, field: "followup")}</td>
                            <td><g:formatDate date="${appointmentInstance.reminderWindowStart}" /></td>
                            <td><g:formatDate date="${appointmentInstance.reminderWindowEnd}" /></td>
                            <td>
                            <g:if test="${appointmentInstance.followup.equals(Appointment.Followup.REGISTERED)}">
                            	-
                            </g:if>
                            <g:else>
	                            <div class="schedule-container">
	                            	<g:if test="${appointmentInstance.date}">
			                            <g:set var="textValue" value="${formatDate(date:appointmentInstance.date)}" />
			                            <g:set var="deleteShowHide" value=""/>
									</g:if>
									<g:else>
			                            <g:set var="textValue" value="Schedule it now" />
			                            <g:set var="deleteShowHide" value="hide"/>
									</g:else>
									<input id="text-${appointmentInstance.id}" type="text" value="${textValue}"  class="schedule"/>
									<input id="prev-${appointmentInstance.id}" type="hidden" value="${textValue}"  class="schedule"/>
									<a id="save-${appointmentInstance.id}" class="save hide"> </a>
									<a id="delete-${appointmentInstance.id}" class="delete ${deleteShowHide}"> </a>
									
									<div id="confirm-${appointmentInstance.id}" title="${appointmentInstance.followup}"></div>
									
									<script type="text/javascript">
									$(function() {
										$("#text-${appointmentInstance.id}").datepicker({
											changeMonth: true,
											changeYear: true,
											dateFormat: DATE_FORMAT,
											//minDate: 0,
											minDate:"${formatDate(date:appointmentInstance.reminderWindowStart)}",
											maxDate:"${formatDate(date:appointmentInstance.reminderWindowEnd)}",
											onSelect:function(dateText, inst) {
												$("#save-${appointmentInstance.id}").removeClass("hide");
												$("#delete-${appointmentInstance.id}").removeClass("hide");
											}
										});

										$("#delete-${appointmentInstance.id}").click(function(){
											if (!$("#save-${appointmentInstance.id}").hasClass("hide")){
												$("#save-${appointmentInstance.id}").addClass("hide");
												if ($("#prev-${appointmentInstance.id}").val() == "Schedule it now"){
													$("#delete-${appointmentInstance.id}").addClass("hide");
												}
												$("#text-${appointmentInstance.id}").val($("#prev-${appointmentInstance.id}").val());
											} else {
												$("#confirm-${appointmentInstance.id}").html("Are you sure you want to cancel this patient's appointment on " + $("#text-${appointmentInstance.id}").val() + "?")
												$("#confirm-${appointmentInstance.id}").dialog("open");
											}
											return false;
										});

										$("#save-${appointmentInstance.id}").click(function(){
											$.post("${createLink(action:'saveAppointmentDate')}", 
													{id:'${appointmentInstance.id}', 
													 date:$("#text-${appointmentInstance.id}").val()},
													 function(data){
														 if (data == 'true'){
														 	$("#save-${appointmentInstance.id}").addClass("hide");
														 	//update the prev- so that we can use it for reset
														 	$("#prev-${appointmentInstance.id}").val($("#text-${appointmentInstance.id}").val());
														 	showMessage("${appointmentInstance.followup} is set for " + $("#text-${appointmentInstance.id}").val() + ".");
														 }
													 }
											);
											return false;
										});										

										$("#confirm-${appointmentInstance.id}").dialog({
											resizable: false,
											height:140,
											modal: true,
											autoOpen: false,
											buttons: {
												"Yes, cancel it": function() {
													$( this ).dialog( "close" );
													$.post("${createLink(action:'deleteAppointmentDate')}", 
															{id:'${appointmentInstance.id}'},
															 function(data){
																 if (data == 'true'){
																 	$("#delete-${appointmentInstance.id}").addClass("hide");
																 	$("#text-${appointmentInstance.id}").val("Schedule it now");
																 	//update the prev- so that we can use it for reset
																 	$("#prev-${appointmentInstance.id}").val("Schedule it now");
																 	showMessage("${appointmentInstance.followup} is cancelled.");
																 }
															 }
													);
												},
												"No, keep it": function() {
													$( this ).dialog( "close" );
												}
											}
										});
									});
									</script>
								</div>
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
