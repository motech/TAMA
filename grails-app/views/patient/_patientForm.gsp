<%@ page import="org.motechproject.tama.Patient;org.motechproject.tama.Gender" %>
<div class="dialog">
	<script type="text/javascript">
		$(function(){
			$("#patientForm").validate({
				rules: {
					clinicPatientId: "required",
					phoneNumber: "required",
					confirmPhoneNumber: {
						required: true,
						equalTo: "#phoneNumber"
					},
					gender: "required",
					dateOfBirth: "required",
					passcode: {
						required: true,
						minlength: 4,
						maxlength: 8,
						number: true
					},
					confirmPasscode: {
						required: true,
						minlength: 4,
						equalTo: "#passcode"
					},
					doctor:"required"
				}
			}); 
		});
	</script>
	<table>
		<tbody>

			<tr class="prop">
				<td valign="top" class="name"><label for="clinicPatientId"><g:message
							code="patient.clinicPatientId.label" default="Clinic Patient Id" />*</label>
				</td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'clinicPatientId', 'errors')}">
					<g:textField name="clinicPatientId"
						value="${patientInstance?.clinicPatientId}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="phoneNumber"><g:message
							code="patient.phoneNumber.label" default="Phone Number" />*</label></td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'phoneNumber', 'errors')}">
					<g:textField name="phoneNumber"
						value="${patientInstance?.phoneNumber}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="confirmPhoneNumber"><g:message
							code="patient.confirmPhoneNumber.label"
							default="Re-enter Phone Number" />*</label></td>
				<td valign="top"><g:textField name="confirmPhoneNumber"
						value="${patientInstance?.phoneNumber}" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="gender"><g:message
							code="patient.gender.label" default="Gender" />*</label></td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'gender', 'errors')}">
					<g:select name="gender" value="${patientInstance?.gender}"
						from="${Gender.values()}" optionKey="key" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="dateOfBirth"><g:message
							code="patient.dateOfBirth.label" default="Date Of Birth" />*</label></td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'dateOfBirth', 'errors')}">
					<g:textField name="dateOfBirth"
						value="${formatDate(format:'dd-MM-yyyy', date:patientInstance?.dateOfBirth)}" /> 
						<script>
						$(function() {
							$( "#dateOfBirth" ).datepicker({
								changeMonth: true,
								changeYear: true,
								dateFormat: DATE_FORMAT,
								maxDate: 0
							});
						});
						</script>
				</td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="passcode"><g:message
							code="patient.passcode.label" default="Passcode" /> (4-8
						digits)*</label></td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'passcode', 'errors')}">
					<g:passwordField name="passcode"
						value="${patientInstance?.passcode}" maxlength="8" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="confirmPasscode"><g:message
							code="patient.confirmPasscode.label" default="Re-enter Passcode" />*</label>
				</td>
				<td valign="top" class="value"><g:passwordField
						name="confirmPasscode" value="${patientInstance?.passcode}"
						maxlength="8" /></td>
			</tr>

			<tr class="prop">
				<td valign="top" class="name"><label for="doctorId"><g:message
							code="patient.doctorId.label" default="Principal Doctor" />*</label></td>
				<td valign="top"
					class="value ${hasErrors(bean: patientInstance, field: 'doctorId', 'errors')}">
					<g:select name="doctorId" value="${patientInstance?.doctorId}"
						from="${doctors}" optionKey="id" optionValue="name" /></td>
			</tr>


		</tbody>
	</table>
</div>