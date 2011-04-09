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

            appointmentReminderPatientDao.addAppointment(it)

            //TODO set the following variables properly
            String eventType = "???" // schedule appointment job event type - constant should be somewhere in the code
            String patientIdKey =  "???" // constant should be somewhere in the code
            String appointmentIdKey = "???" // constant should be somewhere in the code

            eventParameters = [patientIdKey: it.patientId, appointmentIdKey: it.id];



            MotechEvent motechEvent = new MotechEvent(jobId, eventType, eventParameters);

            eventGateway.sendEventMessage(motechEvent)
        }

    }

    def unschedulePatientAppointmentReminders(String patientId) {

        appointmentReminderPatientDao.get(patientId).appointments.each {


             //TODO set the following variables properly
            String eventType = "???" // unschedule job event type - constant should be somewhere in the code


            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, null);
            eventGateway.sendEventMessage(motechEvent)

            appointmentReminderPatientDao.removeAppointment(it)
        }
    }
}
