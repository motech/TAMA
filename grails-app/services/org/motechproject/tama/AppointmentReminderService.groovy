package org.motechproject.tama

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.eventgateway.EventGateway
import org.motechproject.model.MotechEvent
import org.motechproject.tama.dao.PatientDAO
import org.motechproject.tama.model.Patient
import org.motechproject.tama.model.Preferences

class AppointmentReminderService {

    static transactional = false
	def AppointmentsDAO appointmentsDao
    def PatientDAO patientDAO
    def EventGateway eventGateway
    def PatientService patientService
	def config = ConfigurationHolder.config
	/**
	 * M is constant used to determine the start of the actual appointment window (Start = End - M)
	 */
	private int M = config.tama.m;

	def enableAppointmentReminder(Preferences preferences, List<Appointment> appointments) {
		log.info("Attempting to enable appointment reminder for patient id = " + preferences.patientId)
		Patient patient = patientDAO.get(preferences.patientId)
        patient.preferences = preferences
        scheduleIvrCall(patient, preferences);
		schedulePatientAppointmentReminders (appointments)
		log.info("Completed the enabling of appointment reminder for patient id = " + preferences.patientId)
	}

	def disableAppointmentReminder(Preferences preferences) {
		log.info("Attempting to disable appointment reminder for patient id = " + preferences.patientId)
		unschedulePatientAppointmentReminders (preferences.patientId)
		Patient patient = patientDAO.get(preferences.patientId)
        patient.preferences = preferences
		unscheduleIvrCall(patient, preferences);
		patientDAO.update(patient)
		log.info("Completed the disabling of appointment reminders for patient id = " + preferences.patientId)
	}

	/**
	 * Add a patient's preferences
	 */
	def addPreferences(Preferences preferences) {
		Patient patient = patientDAO.get(preferences.patientId)
		patient.preferences = preferences
		patientDAO.update (patient)
	}

	/**
	 * Add a set of appointments
	 */
	def addAppointments(List<Appointment> appointments) {
		for(Appointment appointment in appointments) {
			appointmentsDao.addAppointment(appointment);
		}
	}

	/**
	 * Schedule IVR Call at best time 
	 * @param preferences
	 * @return
	 */
	def scheduleIvrCall(Patient patient, Preferences preferences) {
		preferences.ivrCallJobId = UUID.randomUUID().toString()
		String subject = config.tama.outbox.event.schedule.execution
		String phoneNumberKey =  config.tama.outbox.event.phonenumber.key
		String partyIDKey = config.tama.outbox.event.partyid.key
		String jobIdKey = config.tama.outbox.event.schedule.jobid.key;
		String bestHourKey = config.tama.outbox.event.besttimetocallhour.key
		String bestMinuteKey = config.tama.outbox.event.besttimetocallminute.key

		Map eventParameters = new HashMap()
		eventParameters.put(phoneNumberKey, patient.phoneNumber);
		eventParameters.put(partyIDKey, preferences.patientId);
		eventParameters.put(jobIdKey, preferences.ivrCallJobId);
		eventParameters.put(bestHourKey, preferences.bestTimeToCallHour);
		eventParameters.put(bestMinuteKey, preferences.bestTimeToCallMinute);
		
		MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

		log.info("Sending message to schedule IVR Call: " + motechEvent)
		eventGateway.sendEventMessage(motechEvent)
	}
	
	
	/**
	 * Unschedule IVR Call
	 * @param preferences
	 * @return
	 */
	def unscheduleIvrCall(Patient patient, Preferences preferences) {
		String subject = config.tama.outbox.event.unschedule.execution
		String jobIdKey = config.tama.outbox.event.schedule.jobid.key;

		Map eventParameters = new HashMap()
		eventParameters.put(jobIdKey, preferences.ivrCallJobId);
		MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

		log.info("Sending message to unschedule IVR Call: " + motechEvent)
		eventGateway.sendEventMessage(motechEvent)
	}

    def schedulePatientAppointmentReminders(List<Appointment> appointments) {

        appointments.each {


            String jobId = UUID.randomUUID().toString()
            it.reminderScheduledJobId = jobId

            appointmentsDao.addAppointment(it)

            String subject = config.tama.appointmentreminder.event.schedule.subject
            String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
            String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key
            String jobIdKey = config.motech.scheduler.event.type.schedule.jobid.key;

            Map eventParameters = new HashMap()
            eventParameters.put(patientIdKey, it.patientId);
            eventParameters.put(appointmentIdKey, it.id);
            eventParameters.put(jobIdKey, jobId);

            MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

            log.info("Sending message to schedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)
        }

    }

    private unschedulePatientAppointmentReminders(String patientId) {

        log.info("Unscheduling appointment reminders for the patient ID: " + patientId)

        // Todo
        appointmentReminderPatientDAO.get(patientId).appointments.each {

            String subject = config.tama.appointmentreminder.event.unschedule.subject

            Map eventParameters = new HashMap()
            eventParameters.put(config.motech.scheduler.event.type.schedule.jobid.key, it.reminderScheduledJobId);
            MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

            log.info("Sending message to unschedule appointment reminder: " + it + " job ID: " + it.reminderScheduledJobId)
            eventGateway.sendEventMessage(motechEvent)

             log.info("Removing from the Appointment Reminder database appointment reminder: " + it)
            appointmentsDao.removeAppointment(it)
        }
    }


	/**
	 * Implement AR service methods to save appointment date (no need to unschedule previous appointment since scheduler automatically does this by JobID)
	 * @param appointment
	 * @return
	 */
	def saveAppointmentDate(Appointment appointment) {
		
		//FIXME: would not schedule appointments when appointment reminder is enabled later
		if (appointmentReminderPatientDAO.contains(appointment.id)){
			Appointment arAppointment = appointmentReminderPatientDAO.getAppointment(appointment.id)
			arAppointment.date = appointment.date
			
			// check to see if we should schedule concrete appointment (handles create/delete cases)
			if (appointment.date) {
				// set window using M
				arAppointment.reminderWindowStart = DateUtils.addDays(appointment.date, -M)	
				arAppointment.reminderWindowEnd = appointment.date
			} else {
				// set window using care schedule
				arAppointment.reminderWindowStart = appointment.reminderWindowStart
				arAppointment.reminderWindowEnd = appointment.reminderWindowEnd
			}
			
			appointmentReminderPatientDAO.updateAppointment(arAppointment)
			
			// fire off message to AR Handler
			String eventType = config.tama.appointmentreminder.event.schedule.subject
			String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
			String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key
			String jobIdKey = config.tama.appointmentreminder.event.type.schedule.jobid.key

			Map eventParameters = new HashMap()
			eventParameters.put(patientIdKey, appointment.patientId);
			eventParameters.put(appointmentIdKey, appointment.id);
			eventParameters.put(jobIdKey, arAppointment.reminderScheduledJobId);

			MotechEvent motechEvent = new MotechEvent(eventType, eventParameters);
	
			log.info("Sending message to schedule appointment reminder: " + arAppointment + " job ID: " + arAppointment.reminderScheduledJobId)
			eventGateway.sendEventMessage(motechEvent)
		}
	}

}