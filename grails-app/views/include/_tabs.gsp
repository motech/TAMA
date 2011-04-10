<%
	def controllersForPatientsTab = ['patient']
	def controllersForVisitsTab = ['appointment']
	def controllersForIvrTab = []
	def controllersForAlertsTab = []
	def controllersForRegimenTab = []
	def controllersForLabResultsTab = []
	def controllersForPreferencesTab = ['patientPreferences']
 %>

<div id="sub-tab-container">
	<ul>
		<li class="${controllersForPatientsTab.contains(controllerName)?'active':'inactive'}">
			<g:link controller="patient" action="show" id="${clinicPatientId}">Overview of Patient</g:link>
		</li>
		<li class="${controllersForVisitsTab.contains(controllerName)?'active':'inactive'}">
			<g:link controller="appointment" action="list" id="${clinicPatientId}">Clinic Visits</g:link>
		</li>
		<li class="${controllersForIvrTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Interactions with IVR</a>
		</li>
		<li class="${controllersForAlertsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Alerts</a>
		</li>
		<li class="${controllersForRegimenTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">ART Regimen</a>
		</li>
		<li class="${controllersForLabResultsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Lab Results</a>
		</li>
		<li class="${controllersForPreferencesTab.contains(controllerName)?'active':'inactive'}">
			<g:link action="create" controller="patientPreferences" id="${clinicPatientId}">Patient's Preferences</g:link>
		</li>
	</ul>
</div>