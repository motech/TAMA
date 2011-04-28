package org.motechproject.tama

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.dao.RemindersDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.appointments.api.model.Reminder
import org.motechproject.event.EventRelay
import org.motechproject.model.MotechEvent
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient

class AppointmentReminderService {

    static transactional = false
	def AppointmentsDAO appointmentsDao
    def RemindersDAO remindersDao
    def PatientDAO patientDAO
    def EventRelay eventRelay
	def config = ConfigurationHolder.config

    // Method should take into account events in the past.  No need to create reminders for them (however it doesn't matter
    // since they will not fire
	def enableAppointmentReminder(Patient patient) {
		log.info("Attempting to enable appointment reminder for patient id = " + patient.clinicPatientId)

        List<Appointment> appointments = appointmentsDao.findByExternalId(patient.id)

        scheduleIvrCall(patient)
		schedulePatientAppointmentReminders(appointments)

		log.info("Completed the enabling of appointment reminder for patient id = " + patient.clinicPatientId)
	}

	def disableAppointmentReminder(Patient patient) {
		log.info("Attempting to disable appointment reminder for patient id = " + patient.clinicPatientId)

        List<Appointment> appointments = appointmentsDao.findByExternalId(patient.id)

		unschedulePatientAppointmentReminders(appointments)
		unscheduleIvrCall(patient);

		log.info("Completed the disabling of appointment reminders for patient id = " + patient.clinicPatientId)
	}

	/**
	 * Schedule IVR Call at best time 
	 * @param preferences
	 * @return
	 */
	def scheduleIvrCall(Patient patient) {
		patient.preferences.ivrCallJobId = UUID.randomUUID().toString()

		String subject = config.tama.outbox.event.schedule.execution
		String phoneNumberKey =  config.tama.outbox.event.phonenumber.key
		String partyIDKey = config.tama.outbox.event.partyid.key
		String jobIdKey = config.tama.outbox.event.schedule.jobid.key;
		String bestHourKey = config.tama.outbox.event.besttimetocallhour.key
		String bestMinuteKey = config.tama.outbox.event.besttimetocallminute.key

		Map eventParameters = new HashMap()
		eventParameters.put(phoneNumberKey, patient.phoneNumber);
		eventParameters.put(partyIDKey, patient.clinicPatientId);
		eventParameters.put(jobIdKey, patient.preferences.ivrCallJobId);
		eventParameters.put(bestHourKey, patient.preferences.bestTimeToCallHour);
		eventParameters.put(bestMinuteKey, patient.preferences.bestTimeToCallMinute);
		
		MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

		log.info("Sending message to schedule IVR Call: " + motechEvent)
		eventRelay.sendEventMessage(motechEvent)
	}
	
	
	/**
	 * Unschedule IVR Call
	 * @param preferences
	 * @return
	 */
	def unscheduleIvrCall(Patient patient) {
		String subject = config.tama.outbox.event.unschedule.execution
		String jobIdKey = config.tama.outbox.event.schedule.jobid.key;

		Map eventParameters = new HashMap()
		eventParameters.put(jobIdKey, patient.preferences.ivrCallJobId);
		MotechEvent motechEvent = new MotechEvent(subject, eventParameters);

		log.info("Sending message to unschedule IVR Call: " + motechEvent)
		eventRelay.sendEventMessage(motechEvent)
	}

    private unschedulePatientAppointmentReminders(List<Appointment> appointments) {

        // This implementation assumes that no other module is creating reminders against appointments.  If someone
        // is then instead of deleting all reminders and resetting them we would need to hold references so we can
        // disable/delete just ours

        if (appointments.size() > 0) {
            def appointment = appointments.get(0)

            remindersDao.findByExternalId(appointment.externalId).each {
                remindersDao.removeReminder(it)
            }
        }
    }

    /**
	 * service methods to save appointment date
	 * @param appointmentId
	 * @param date
	 * @return
	 */
	def saveAppointmentScheduledDate(String appointmentId, Date date){
		Appointment appointment = appointmentsDao.getAppointment(appointmentId);
		appointment.scheduledDate = date;

        appointmentsDao.updateAppointment(appointment)
   	}


    def schedulePatientAppointmentReminders(List<Appointment> appointments) {
        appointments.each {
            createRemindersForAppointment(it)
        }
    }

    def createRemindersForAppointment(Appointment appointment) {

        // If we have a scheduled date then we want to base our reminders off of that date.  If we do not
        // then we want to use the due date.  We also will use a different window for scheduled v. unscheduled
        // appointments
        //
        // This implementation assumes that no other module is creating reminders against appointments.  If someone
        // is then instead of deleting all reminders and resetting them we would need to hold references so we can
        // disable/delete just ours
        remindersDao.findByAppointmentId(appointment.id).each {
            remindersDao.removeReminder(it)
        }

        if (appointment.scheduledDate) {
            /**
             * M is constant used to determine the start of the window (Start = End - N)
             */
            createReminderForAppointment(appointment.externalId, appointment.id, appointment.scheduledDate, config.tama.m, 0)
        } else {
            /**
             * N is constant used to determine the start of the window (Start = End - N)
             */
            createReminderForAppointment(appointment.externalId, appointment.id, appointment.dueDate, config.tama.n, 0)
        }
    }

    private def createReminderForAppointment(String externalId, String appointmentId, Date date, int beforeOffset, int afterOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.truncate(date, Calendar.DATE));

        Reminder reminder = new Reminder()
        reminder.externalId = externalId
        reminder.appointmentId = appointmentId

        cal.add(Calendar.DATE, afterOffset)
        reminder.endDate = cal.getTime()

        cal.add(Calendar.DATE, -afterOffset);
        cal.add(Calendar.DATE, -beforeOffset);
        reminder.startDate = cal.getTime();

        reminder.units = Reminder.intervalUnits.DAYS
        // todo make repeatCount optional
        reminder.repeatCount = (reminder.endDate.getTime() - reminder.startDate.getTime()) / 86400000

        remindersDao.addReminder(reminder)
    }
}