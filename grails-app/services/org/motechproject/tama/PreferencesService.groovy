package org.motechproject.tama

import org.motechproject.appointments.api.AppointmentService
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Preferences

class PreferencesService {

    static transactional = false

    AppointmentService appointmentService

	PatientService patientService
	AppointmentReminderService appointmentReminderService

	/**
	 * Update an existing PatientPreferences object
	 * @param patientPreferences object to be updated
	 * @return updated object
	 */
	def preferencesUpdated(Patient patient) {
		Preferences preferences = patient.preferences

        if (preferences.appointmentReminderEnabled) {
            enableAppointmentReminder(patient)
        } else {
            disableAppointmentReminder(patient)
        }
	}
	
	private void enableAppointmentReminder(Patient patient) {
		appointmentReminderService.enableAppointmentReminder(patient)

        patientService.updatePatient(patient)
	}
	
	private void disableAppointmentReminder(Patient patient) {
		appointmentReminderService.disableAppointmentReminder(patient)

        patientService.updatePatient(patient)
	}
	
}
