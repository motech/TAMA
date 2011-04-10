package org.motechproject.tama

import org.motechproject.tama.dao.PatientDao;
import org.motechproject.tama.dao.AppointmentDao
import org.motechproject.tama.dao.PatientPreferencesDao
import org.motechproject.tama.AppointmentReminderService
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.appointmentreminder.model.Doctor as ARDoctor
import org.motechproject.appointmentreminder.model.Patient as ARPatient
import org.motechproject.appointmentreminder.model.Clinic as ARClinic
import org.motechproject.appointmentreminder.model.Appointment as ARAppointment
import org.motechproject.appointmentreminder.model.Preferences as ARPreferences


class PatientPreferencesService {

    static transactional = false

	PatientPreferencesDao tamaPatientPreferencesDao
	PatientDao tamaPatientDao
	DoctorDao tamaDoctorDao
	ClinicDao tamaClinicDao
	AppointmentDao tamaAppointmentDao
	ARPatientDAO appointmentReminderPatientDAO
	
	AppointmentReminderService appointmentReminderService
	
	/**
	 * Create a non-existant patient preferences object
	 * @param patientPreferences object to be stored
	 * @return stored object with populated id
	 */
	def createPatientPreferences(PatientPreferences patientPreferences) {
		tamaPatientPreferencesDao.add(patientPreferences)
		moduleManagement (null, patientPreferences)
		return patientPreferences
	}
	
	/**
	 * Update an existing PatientPreferences object
	 * @param patientPreferences object to be updated
	 * @return updated object
	 */
	def updatePatientPreferences(PatientPreferences patientPreferences) {
		PatientPreferences previousPrefs = tamaPatientPreferencesDao.get(patientPreferences.id)
		tamaPatientPreferencesDao.update(patientPreferences)
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
		return tamaPatientPreferencesDao.findByClinicPatientId(clinicId, clinicPatientId)
    }
	
	/**
	* Process changes to their preferences in relation to the various modules/features within TAMA
	*/
    private void moduleManagement(PatientPreferences previous, PatientPreferences updated) {
	   moduleAppointmentReminder(previous, updated)
    }
	
	/**
	 * Handle changes to the appointment reminder preferences and associated actions. 
	 * @param previous Previous preference settings
	 * @param updated Current preference settings
	 */
    private void moduleAppointmentReminder(PatientPreferences previous, PatientPreferences updated) {
	
	   if (previous == null  || updated.appointmentReminderEnabled != previous.appointmentReminderEnabled) {
		   if (updated.appointmentReminderEnabled == true) {
			   // Appointment Reminder has been enabled so we need to
			   // - Store appointment and patient preferences into appointment reminder database
			   Patient patient = tamaPatientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)
			   
			   ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id)
			   List<Appointment> appointments = tamaAppointmentDao.findByPatientId (patient.id)
			   Set<ARAppointment> arAppointments = convertToAppointmentReminderAppointments (appointments)
			   arPatient.appointments = arAppointments
			   ARPreferences arPreferences = convertToAppointmentReminderPreferences(updated, patient.id)
			   arPatient.preferences = arPreferences
			   appointmentReminderPatientDAO.update (arPatient) // Add the patient object to the database
			   
			   // - Schedule appointment reminders for all appointments to occur in the future (eg if they enable it after 2 appointments we won't schedule old appointments)
			   appointmentReminderService.schedulePatientAppointmentReminders(patient.id)
			   
		   } else if (previous != null && previous.appointmentReminderEnabled == true && updated.appointmentReminderEnabled == false){
			   // Appointment Reminder has been disabled (when it was previously enabled) so we need to
			   // - Unschedule appointment reminders for all appointments to occur in the future (eg if they enable it after 2 appointments we won't schedule old appointments)
		   	   appointmentReminderService.unschedulePatientAppointmentReminders(patient.id)
			   // - Update patient preferences accordingly
			   Patient patient = tamaPatientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)
			   
			   ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id)
			   ARPreferences arPreferences = convertToAppointmentReminderPreferences(updated, patient.id)
			   arPatient.preferences = arPreferences
			   appointmentReminderPatientDAO.update (arPatient) // Add the patient object to the database
			   
		   } else {
		   	   // Just update the patient's preferences since appointment reminder was and still is disabled
			   Patient patient = tamaPatientDao.findByClinicPatientId(updated.clinicId, updated.clinicPatientId)
			   
			   ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id)
			   ARPreferences arPreferences = convertToAppointmentReminderPreferences(updated, patient.id)
			   arPatient.preferences = arPreferences
			   appointmentReminderPatientDAO.update (arPatient) // Add the patient object to the database

		   }
	   }
    }
	
	private convertToAppointmentReminderPatient(Patient patient, Clinic clinic, Doctor doctor){
		ARClinic arClinic = new ARClinic(id:clinic.id, name:clinic.name)
		ARDoctor arDoctor = new ARDoctor(id:doctor.id, name:doctor.name, clinic:arClinic)
		
		ARPatient arPatient = new ARPatient(
			id:patient.id,
			clinicPatientId:patient.clinicPatientId,
			gender:patient.gender.toString(),
			clinic:arClinic,
			doctor:arDoctor,
			phoneNumber:patient.phoneNumber
			)
		
		return arPatient
	}
	
	/**
	 * Convert from org.motech.tama.Appointment to org.motechproject.appointmentreminder.model.Appointment
	 * @param appointments A list of TAMA Appointment objects
	 * @return A list of Appointment Reminder Appointment Objects
	 */
	private convertToAppointmentReminderAppointments(List<Appointment> appointments) {
		Set<ARAppointment> arAppointments = new ArrayList<ARAppointment>()
		for(Appointment a in appointments) {
			arAppointments.add ( new ARAppointment(id:a.id, patientId:a.patientId, reminderWindowStart: a.reminderWindowStart, 
				reminderWindowEnd:a.reminderWindowEnd, date: a.date, reminderScheduledJobId: UUID.randomUUID().toString()) )
		}
		
		return arAppointments
	}
	
	/**
	 * Convert the TAMA Patient Preferences into Appointment Reminder Preferences
	 * @param preferences TAMA Patient Preferences
	 * @param patientId Patient Id
	 * @return Appointment Reminder Preferences
	 */
	private convertToAppointmentReminderPreferences(PatientPreferences preferences, String patientId) {
		// FIXME: best time to call is not only the hour but also the minute
//		ARPreferences arPreferences = new ARPreferences();
//		arPreferences.enabled = preferences.appointmentReminderEnabled
//		arPreferences.patientId = patientId
//		arPreferences.bestTimeToCall = preferences.bestTimeToCallHour
//		return arPreferences
		return new ARPreferences(enabled: preferences.appointmentReminderEnabled, patientId:patientId, bestTimeToCall: preferences.bestTimeToCallHour)
	}

}
