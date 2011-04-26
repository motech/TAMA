package org.motechproject.tama

import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.tama.dao.ClinicDAO
import org.motechproject.tama.dao.DoctorDAO
import org.motechproject.tama.dao.PatientDAO
import org.motechproject.tama.dao.PreferencesDAO
import org.motechproject.tama.model.Patient
import org.motechproject.tama.model.Preferences

class PatientPreferencesService {

    static transactional = false

	PreferencesDAO preferencesDao
	PatientDAO patientDao
	DoctorDAO doctorDao
	ClinicDAO clinicDao
	AppointmentsDAO appointmentsDao
	AppointmentReminderService appointmentReminderService
	
	/**
	 * Create a non-existant patient preferences object
	 * @param patientPreferences object to be stored
	 * @return stored object with populated id
	 */
	def createPatientPreferences(Preferences patientPreferences) {
		preferencesDao.add(patientPreferences)
		moduleManagement (null, patientPreferences)
		return patientPreferences
	}
	
	/**
	 * Update an existing PatientPreferences object
	 * @param patientPreferences object to be updated
	 * @return updated object
	 */
	def updatePatientPreferences(Preferences patientPreferences) {
		Preferences previousPrefs = preferencesDao.get(patientPreferences.id)
		preferencesDao.update(patientPreferences)
		moduleManagement (previousPrefs, patientPreferences)
		return patientPreferences
	}
	
	/**
	 * Find a patient's preferences base upon their clinic id and clinic patient id
	 * @param clinicId the id of the clinic the patient resides within
	 * @param clinicPatientId the clinic id of the patient
	 * @return An instance of the requested PatientPreferences object
	 */
    def findByClinicPatientId(String clinicId, String clinicPatientId) {
		return preferencesDao.findByClinicPatientId(clinicId, clinicPatientId)
    }
	
	/**
	* Process changes to their preferences in relation to the various modules/features within TAMA
	*/
    private void moduleManagement(Preferences previous, Preferences updated) {
	   moduleAppointmentReminder(previous, updated)
    }
	
	/**
	 * Handle changes to the appointment reminder preferences and associated actions. 
	 * @param previous Previous preference settings
	 * @param updated Current preference settings
	 */
    private void moduleAppointmentReminder(Preferences previous, Preferences updated) {
	
		// Determine:
		// A) has the module been enabled/disabled in this post
		// OR
		// B) is it enabled and has the best time to call changed
		
	   if (previous == null  || updated.appointmentReminderEnabled != previous.appointmentReminderEnabled) {
		   if (updated.appointmentReminderEnabled != null && updated.appointmentReminderEnabled == Boolean.TRUE) {
			   // Appointment Reminder has been enabled so we need to
		   	   enableAppointmentReminder (previous, updated)
		   } else if (previous != null && (previous.appointmentReminderEnabled != null && previous.appointmentReminderEnabled == Boolean.TRUE) && 
		   				(updated.appointmentReminderEnabled == null || updated.appointmentReminderEnabled == Boolean.FALSE)){
			   // Appointment Reminder has been disabled (when it was previously enabled) so we need to
			   disableAppointmentReminder (previous, updated)
			   
		   } else if (previous != null && (updated.appointmentReminderEnabled != null && updated.appointmentReminderEnabled == Boolean.TRUE) && 
		   				(previous.bestTimeToCallHour != updated.bestTimeToCallHour || 
							   previous.bestTimeToCallMinute != updated.bestTimeToCallMinute )) {
			   // Best time to call has changed (and appointment reminder is enabled) so we need to disable appointment 
			   // reminder and re-enable it with the new times
			   // Disable
		   	   disableAppointmentReminder (previous, updated)
			   // Re-Enable
			   enableAppointmentReminder (previous, updated)

		   } else {
		   	   // Just update the patient's preferences since appointment reminder was and still is disabled
			   Patient patient = patientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)

		   }
	   } else if (previous != null && (updated.appointmentReminderEnabled != null && updated.appointmentReminderEnabled == Boolean.TRUE) &&
	   				(previous.bestTimeToCallHour != updated.bestTimeToCallHour ||
				     previous.bestTimeToCallMinute != updated.bestTimeToCallMinute )) {
			// Best time to call has changed (and appointment reminder is enabled) so we need to disable appointment
			// reminder and re-enable it with the new times
			// Disable
			disableAppointmentReminder (previous, updated)
			// Re-Enable
			enableAppointmentReminder (previous, updated)
		}
    }
	
	private void enableAppointmentReminder(Preferences previous, Preferences updated) {
		// - Schedule appointment reminders
		Patient patient = patientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)
		
		List<Appointment> appointments = appointmentsDao.findByPatientId (patient.id)

		appointmentReminderService.enableAppointmentReminder(Preferences, updated)

	}
	
	private void disableAppointmentReminder(Preferences previous, Preferences updated) {
		// - Unschedule appointment reminders for all appointments to occur in the future (eg if they enable it after 2 appointments we won't schedule old appointments)
		Patient patient = patientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)
		appointmentReminderService.disableAppointmentReminder(updated)
	}
	
}
