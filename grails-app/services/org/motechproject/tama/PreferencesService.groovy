package org.motechproject.tama

import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.tama.api.dao.ClinicDAO
import org.motechproject.tama.api.dao.DoctorDAO
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Preferences

class PreferencesService {

    static transactional = false

	PatientDAO patientDao
	AppointmentsDAO appointmentsDao
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

        patientDao.update(patient)
	}
	
	private void disableAppointmentReminder(Patient patient) {
		appointmentReminderService.disableAppointmentReminder(patient)

        patientDao.update(patient)
	}
	
}
