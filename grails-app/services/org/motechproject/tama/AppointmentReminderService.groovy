package org.motechproject.tama

import java.util.Map;

import org.motechproject.appointmentreminder.model.Preferences;
import org.motechproject.appointmentreminder.model.Appointment;
import org.motechproject.appointmentreminder.model.Patient;
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.eventgateway.EventGateway
import org.motechproject.model.MotechEvent
import org.codehaus.groovy.grails.commons.ConfigurationHolder;


class AppointmentReminderService {

    static transactional = false

    ARPatientDAO appointmentReminderPatientDAO
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

			// TODO: Need to refactor PatientService to move job id creation here instead of in PatientService
//            String jobId = UUID.randomUUID().toString()
//            it.reminderScheduledJobId = jobId
//
//            appointmentReminderPatientDAO.addAppointment(it)

            String eventType = config.tama.appointmentreminder.event.type.schedule.key
            String patientIdKey =  config.tama.appointmentreminder.event.type.schedule.patientid.key
            String appointmentIdKey = config.tama.appointmentreminder.event.type.schedule.appointmentid.key

            Map<String, Object> eventParameters = [patientIdKey: it.patientId, appointmentIdKey: it.id];



            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, eventParameters);

            eventGateway.sendEventMessage(motechEvent)
        }

    }

    def unschedulePatientAppointmentReminders(String patientId) {

        appointmentReminderPatientDAO.get(patientId).appointments.each {


             //TODO set the following variables properly
            String eventType = config.tama.appointmentreminder.event.type.unschedule.key // unschedule job event type - constant should be somewhere in the code


            MotechEvent motechEvent = new MotechEvent(it.reminderScheduledJobId, eventType, null);
            eventGateway.sendEventMessage(motechEvent)

			// TODO: Remove reminder job id from the appointment
            //appointmentReminderPatientDAO.removeAppointment(it)
        }
    }
}
