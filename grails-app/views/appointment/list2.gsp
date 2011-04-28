<%@ page import="org.motechproject.appointments.api.model.Appointment; org.motechproject.appointments.api.model.Reminder; org.motechproject.appointments.api.model.Visit; org.motechproject.tama.api.model.Patient" %>
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
                        <tr>
                            <g:sortableColumn property="followup" title="${message(code: 'appointment.title.label', default: 'Followup')}" />
                            <g:sortableColumn property="reminderWindowStart" title="${message(code: 'appointment.reminderWindowStart.label', default: 'Reminders Start')}" />
                            <g:sortableColumn property="reminderWindowEnd" title="${message(code: 'appointment.reminderWindowEnd.label', default: 'Reminders End')}" />
                            <g:sortableColumn property="date" title="${message(code: 'appointment.dueDate.label', default: 'Appointment Due Date')}" />
                            <g:sortableColumn property="date" title="${message(code: 'appointment.scheduledDate.label', default: 'Appointment Set For')}" />
                            <g:sortableColumn property="date" title="${message(code: 'appointment.visitDate.label', default: 'Visit Date')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <tr class="odd">
                        <td>Registration</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <g:if test="${visit?.visitDate}">
                            <td><g:formatDate date="${visit.visitDate}" /></td>
                        </g:if>
                        <g:else>
                            <td></td>
                        </g:else>
                    </tr>
                    <g:each in="${appointmentInstanceList}" status="i" var="appointmentInstance">
                        <g:set var="windowStart" value="" />
                        <g:set var="windowEnd" value="" />

                        <g:each in="${remindersList}" var="reminder">
                            <g:logMsg level="debug">Testing ${appointmentInstance.id} and ${reminder.appointmentId}</g:logMsg>
                            <g:if test="${appointmentInstance.id == reminder.appointmentId}">
                                <g:set var="windowStart" value="${formatDate(date:reminder.startDate)}" />
                                <g:set var="windowEnd" value="${formatDate(date:reminder.endDate)}" />
                            </g:if>
                        </g:each>

                        <g:each in="${visitList}" var="v">
                            <g:if test="${appointmentInstance.id == v.appointmentId && v.visitDate}">
                                <g:set var="visitDate" value="${formatDate(date:v.visitDate)}" />
                            </g:if>
                        </g:each>

                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

            				<td>${fieldValue(bean: appointmentInstance, field: "title")}</td>
                            <td>${windowStart}</td>
                            <td>${windowEnd}</td>
                            <td><g:formatDate date="${appointmentInstance.dueDate}" /></td>
                            <td>
                            <div class="schedule-container">
                                <g:if test="${appointmentInstance.scheduledDate}">
                                    <g:set var="textValue" value="${formatDate(date:appointmentInstance.scheduledDate)}" />
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

                                <div id="confirm-${appointmentInstance.id}" title="${appointmentInstance.title}"></div>

                                <script type="text/javascript">
                                $(function() {
                                    $("#text-${appointmentInstance.id}").datepicker({
                                        changeMonth: true,
                                        changeYear: true,
                                        dateFormat: DATE_FORMAT,
                                        //minDate: 0,
                                        minDate:"${windowStart}",
                                        maxDate:"${windowEnd}",
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
                                                        showMessage("${appointmentInstance.title} is set for " + $("#text-${appointmentInstance.id}").val() + ".");
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
                                                                showMessage("${appointmentInstance.title} is cancelled.");
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
                            </td>
                            <td>${visitDate}</td>

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
