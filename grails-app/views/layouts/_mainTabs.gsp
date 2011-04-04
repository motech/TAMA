<%
	def controllersForPatientsTab = ['patient']
	def controllersForAlertsTab = []
	def controllersForAppointmentsTab = []
	def controllersForReportsTab = []
	def controllersForSettingsTab = []
 %>
<div id="main-tab-container">
	<ul>
		<li class="${controllersForPatientsTab.contains(controllerName)?'active':'inactive'}"><g:link controller="patient" action="list">Patients</g:link></li>
		<li class="${controllersForAlertsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-2">Alerts</a></li>
		<li class="${controllersForAppointmentsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Appointments</a></li>
		<li class="${controllersForReportsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Reports</a></li>
		<li class="${controllersForSettingsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Settings</a></li>
	</ul>
</div>
	
	
