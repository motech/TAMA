package org.motechproject.tama

import java.util.List
import org.motechproject.appointmentreminder.model.Patient
import org.motechproject.appointmentreminder.model.Preferences
import org.motechproject.appointmentreminder.model.Doctor
import org.motechproject.appointmentreminder.model.Clinic
import org.motechproject.appointmentreminder.model.Appointment
import org.motechproject.tama.Appointment as TamaAppointment
import org.motechproject.tama.dao.AppointmentDao as TamaAppointmentDao
import org.motechproject.tama.Clinic as TamaClinic
import org.motechproject.tama.Doctor as TamaDoctor
import org.motechproject.tama.Patient as TamaPatient
import org.motechproject.tama.PatientPreferences as TamaPreferences
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.eventgateway.EventGateway
import org.motechproject.model.MotechEvent
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AppointmentReminderService {

    static transactional = false
	def TamaAppointmentDao tamaAppointmentDao
    def ARPatientDAO appointmentReminderPatientDAO
    def EventGateway eventGateway
    def PatientService patientService
	def config = ConfigurationHolder.config

	def enableAppointmentReminder(Preferences preferences, List<Appointment> appointments) {
		log.info("Attempting to enable appointment reminder for patient id = " + preferences.patientId)
		Patient patient = appointmentReminderPatientDAO.get(preferences.patientId)
		patient.preferences = preferences
		appointmentReminderPatientDAO.update(patient)
		schedulePatientAppointmentReminders (appointments)
		log.info("Completed the enabling of appointment reminder for patient id = " + preferences.patientId)
	}

	def disableAppointmentReminder(Preferences preferences) {
		log.info("Attempting to disable appointment reminder for patient id = " + preferences.patientId)
		unschedulePatientAppointmentReminders (preferences.patientId)
		Patient patient = appointmentReminderPatientDAO.get(preferences.patientId)
		patient.preferences = preferences 
		appointmentReminderPatientDAO.update(patient)
		log.info("Completed the disabling of appointment reminders for patient id = " + preferences.patientId)
	}

	/**
	 * Add a patient's preferences
	 */
	def addPreferences(Preferences preferences) {
		Patient patient = appointmentReminderPatientDAO.get(preferences.patientId)
		patient.preferences = preferences
		appointmentReminderPatientDAO.update (patient)
	}

	/**
	 * Add a set of appointments
	 */
	def addAppointments(List<Appointment> appointments) {
		for(Appointment appointment in appointments) {
			appointmentReminderPatientDAO.addAppointment(appointment);
		}
	}


    def schedulePatientAppointmentReminders(List<Appointment> appointments) {

        appointments.each {


            String jobId = UUID.randomUUID().toString()
            it.reminderScheduledJobId = jobId

            appointmentReminderPatientDAO.addAppointment(it)

            String eventType = config.tama.appointmentreminder.event.type.schedule.key
            String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
            String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key

            Map eventParameters = new HashMap()
            eventParameters.put(patientIdKey, it.patientId);
            eventParameters.put(appointmentIdKey, it.id);


            MotechEvent motechEvent = new MotechEvent(jobId, eventType, eventParameters);

            log.info("Sending message to schedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)
        }

    }

    private unschedulePatientAppointmentReminders(String patientId) {

        log.info("Unscheduling appointment reminders for the patient ID: " + patientId)


        appointmentReminderPatientDAO.get(patientId).appointments.each {

            String eventType = config.tama.appointmentreminder.event.type.unschedule.key

            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, null);

            log.info("Sending message to unschedule appointment reminder: " + it + " job ID: " + it.reminderScheduledJobId)
            eventGateway.sendEventMessage(motechEvent)

             log.info("Removing from the Appointment Reminder database appointment reminder: " + it)
            appointmentReminderPatientDAO.removeAppointment(it)
        }
    }
	
	/**
	 * Convert a Tama Patient into an Appointment Reminder Patient
	 * @param patient TAMA patient object
	 * @param clinic TAMA clinic object
	 * @param doctor TAMA doctor
	 * @return Appointment Reminder Patient object
	 */
	def convertToAppointmentReminderPatient(TamaPatient patient, TamaClinic clinic, TamaDoctor doctor){
		Clinic arClinic = new Clinic(id:clinic.id, name:clinic.name)
		Doctor arDoctor = new Doctor(id:doctor.id, name:doctor.name, clinic:arClinic)
		
		Patient arPatient = new Patient(
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
	def convertToAppointmentReminderAppointments(List<TamaAppointment> appointments) {
		List<Appointment> arAppointments = new ArrayList<Appointment>()
		for(TamaAppointment a in appointments) {
			// exclude initial registration appointment
			if(a.followup!=TamaAppointment.Followup.REGISTERED) {
				arAppointments.add ( new Appointment(id:a.id, patientId:a.patientId, reminderWindowStart: a.reminderWindowStart, 
					reminderWindowEnd:a.reminderWindowEnd, date: a.date) )
			}
		}
		
		return arAppointments
	}
	
	/**
	 * Convert the TAMA Patient Preferences into Appointment Reminder Preferences
	 * @param preferences TAMA Patient Preferences
	 * @param patientId Patient Id
	 * @return Appointment Reminder Preferences
	 */
	def convertToAppointmentReminderPreferences(TamaPreferences preferences, String patientId) {
		return new Preferences(enabled: preferences.appointmentReminderEnabled, patientId:patientId, bestTimeToCallHour: preferences.bestTimeToCallHour, bestTimeToCallMinute:preferences.bestTimeToCallMinute)
	}

		
	/**
	 * Implement AR service methods to save appointment date (no need to unschedule previous appointment since scheduler automatically does this by JobID)
	 * @param appointment
	 * @return
	 */
	def saveAppointmentDate(TamaAppointment appointment) {
		// update appointment objects
		Appointment arAppointment = appointmentReminderPatientDAO.getAppointment(appointment.id)
		arAppointment.date = appointment.date
		tamaAppointmentDao.update(appointment)
		appointmentReminderPatientDAO.updateAppointment(arAppointment)
		
		// fire off message to AR Handler
		String eventType = config.tama.appointmentreminder.event.type.scheduleappointment.key
		String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
		String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key

		Map eventParameters = new HashMap()
		eventParameters.put(patientIdKey, appointment.patientId);
		eventParameters.put(appointmentIdKey, appointment.id);


		MotechEvent motechEvent = new MotechEvent(arAppointment.reminderScheduledJobId, eventType, eventParameters);

		log.info("Sending message to schedule concrete appointment reminder: " + appointment + " job ID: " + arAppointment.reminderScheduledJobId)
		eventGateway.sendEventMessage(motechEvent)
	}

}