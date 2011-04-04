<%
	def controllersForPatientsTab = ['patient']
	def controllersForVisitsTab = ['visit']
	def controllersForIvrTab = []
	def controllersForAlertsTab = []
	def controllersForRegimenTab = []
	def controllersForLabResultsTab = []
	def controllersForPreferencesTab = []
 %>

<div id="sub-tab-container">
	<ul>
		<li class="${controllersForPatientsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-1">Overview of Patient</a>
		</li>
		<li class="${controllersForVisitsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-2">Clinic Visits</a>
		</li>
		<li class="${controllersForIvrTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Interactions with IVR</a>
		</li>
		<li class="${controllersForAlertsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Alerts</a>
		</li>
		<li class="${controllersForRegimenTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">ART Regimen</a>
		</li>
		<li class="${controllersForLabResultsTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Lab Results</a>
		</li>
		<li class="${controllersForPreferencesTab.contains(controllerName)?'active':'inactive'}"><a href="#tabs-3">Patient's Preferences</a>
		</li>
	</ul>
</div>