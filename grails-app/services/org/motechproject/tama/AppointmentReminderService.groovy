package org.motechproject.tama

import org.motechproject.appointmentreminder.model.Preferences;
import org.motechproject.appointmentreminder.model.Appointment;
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDao
import org.motechproject.eventgateway.EventGateway
import org.motechproject.model.MotechEvent

class AppointmentReminderService {

    static transactional = false

    def ARPatientDao appointmentReminderPatientDao
    def EventGateway eventGateway
    def PatientService patientService

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

        patientService.getPatient(patientId).appointments.each {


            String jobId = UUID.randomUUID().toString()
            it.reminderScheduledJobId = jobId

            //TODO add/update Appointment to the AppointmentReminder DB
            //if an appointment already in the db - update otherwise - add
            //appointmentReminderPatientDao.addAppointment(it)

            //TODO get the values of the following variables from constants specified in motech code
            String eventType = "ScheduleAppointmentReminder"
            String patientIdKey =  "PatientID"
            String appointmentIdKey = "AppointmentID"

            Map eventParameters = new HashMap()
            eventParameters.put(patientIdKey, it.patientId);
            eventParameters.put(appointmentIdKey, it.id);


            MotechEvent motechEvent = new MotechEvent(jobId, eventType, eventParameters);

            log.info("Sending message to schedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)
        }

    }

    def unschedulePatientAppointmentReminders(String patientId) {

        log.info("Unscheduling appointment reminders for the patient ID: " + patientId)


        appointmentReminderPatientDao.get(patientId).appointments.each {


             //TODO set eventType from a constant specified in motech code
            String eventType = "UnscheduleAppointmentReminder"


            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, null);

            log.info("Sending message to unschedule appointment reminder: " + it + " job ID: " + jobId)
            eventGateway.sendEventMessage(motechEvent)

             log.info("Removing from the Appointment Reminder database appointment reminder: " + it)
            appointmentReminderPatientDAO.removeAppointment(it)
        }
    }
}
