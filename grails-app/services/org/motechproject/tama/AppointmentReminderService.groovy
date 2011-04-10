package org.motechproject.tama

import java.util.Map;

import org.motechproject.appointmentreminder.model.Preferences;
import org.motechproject.appointmentreminder.model.Appointment;
<<<<<<< HEAD
import org.motechproject.appointmentreminder.model.Patient;
=======
>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.eventgateway.EventGateway
import org.motechproject.model.MotechEvent
import org.codehaus.groovy.grails.commons.ConfigurationHolder;


class AppointmentReminderService {

    static transactional = false

<<<<<<< HEAD
    ARPatientDAO appointmentReminderPatientDAO
=======
    def ARPatientDAO appointmentReminderPatientDAO
>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657
    def EventGateway eventGateway
    def PatientService patientService
	def config = ConfigurationHolder.config

	def enableAppointmentReminder(Preferences preferences, List<Appointment> appointments) {
		
	}
	
	def disableAppointmentReminder(String patientId) {
		
	}
	
	/**
	 * Add a patient's preferences
	 */
	def addPreferences(Preferences preferences) {
		
	}
	
	/**
	 * Add a set of appointments
	 */
	def addAppointments(List<Appointment> appointments) {
		
	}
	

    def schedulePatientAppointmentReminders(String patientId) {
		Patient p = appointmentReminderPatientDAO.get(patientId)
        appointmentReminderPatientDAO.get(patientId).appointments.each {

<<<<<<< HEAD
			// TODO: Need to refactor PatientService to move job id creation here instead of in PatientService
//            String jobId = UUID.randomUUID().toString()
//            it.reminderScheduledJobId = jobId
//
//            appointmentReminderPatientDAO.addAppointment(it)

            String eventType = config.tama.appointmentreminder.event.type.schedule.key
            String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
            String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key

            Map<String, Object> eventParameters = [patientIdKey: it.patientId, appointmentIdKey: it.id];
=======
        patientService.getPatient(patientId).appointments.each {


            String jobId = UUID.randomUUID().toString()
            it.reminderScheduledJobId = jobId

            //TODO add/update Appointment to the AppointmentReminder DB
            //if an appointment already in the db - update otherwise - add
            //appointmentReminderPatientDAO.addAppointment(it)

            //TODO get the values of the following variables from constants specified in motech code
            String eventType = "ScheduleAppointmentReminder"
            String patientIdKey =  "PatientID"
            String appointmentIdKey = "AppointmentID"
>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657

            Map eventParameters = new HashMap()
            eventParameters.put(patientIdKey, it.patientId);
            eventParameters.put(appointmentIdKey, it.id);


            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, eventParameters);

            log.info("Sending message to schedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)
        }

    }

    def unschedulePatientAppointmentReminders(String patientId) {

<<<<<<< HEAD
        appointmentReminderPatientDAO.get(patientId).appointments.each {
=======
        log.info("Unscheduling appointment reminders for the patient ID: " + patientId)

>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657

        appointmentReminderPatientDAO.get(patientId).appointments.each {

<<<<<<< HEAD
             //TODO set the following variables properly
            String eventType = config.tama.appointmentreminder.event.type.unschedule.key // unschedule job event type - constant should be somewhere in the code
=======

             //TODO set eventType from a constant specified in motech code
            String eventType = "UnscheduleAppointmentReminder"
>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657


            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, null);

            log.info("Sending message to unschedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)

<<<<<<< HEAD
			// TODO: Remove reminder job id from the appointment
            //appointmentReminderPatientDAO.removeAppointment(it)
=======
             log.info("Removing from the Appointment Reminder database appointment reminder: " + it)
            appointmentReminderPatientDAO.removeAppointment(it)
>>>>>>> 75828cde0b0c8ceb6482b7d3834a5d5a0b053657
        }
    }
}
