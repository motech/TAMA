package org.motechproject.tama

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.motechproject.appointments.api.AppointmentService
import org.motechproject.appointments.api.ReminderService
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.appointments.api.model.Reminder
import org.motechproject.tama.api.model.Clinic
import org.motechproject.tama.api.model.Doctor
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Patient.Gender

class AppointmentReminderServiceTests extends GroovyTestCase {
	def AppointmentReminderService appointmentReminderService
	def PatientService patientService
    def AppointmentService appointmentService
    def ReminderService reminderService

	def config = ConfigurationHolder.config
	/**
	 * M is constant used to determine the start of the actual appointment window (Start = End - M)
	 */
	private int M = config.tama.m;
    private int N = config.tama.n;

	static CLINIC_ID = UUID.randomUUID().toString();
	static DOCTOR_ID = UUID.randomUUID().toString();
	static PATIENT_ID = UUID.randomUUID().toString();
	static CLINIC_PATIENT_ID = UUID.randomUUID().toString();

	def clinic
	def doctor
	def patient
	
	List<Appointment> appointments
	
	protected void setUp() {
		super.setUp()
		clinic = new Clinic(
				id: CLINIC_ID,
				name:"test-clinic"
				)

		doctor = new Doctor(
				id: DOCTOR_ID,
				clinicId:CLINIC_ID,
				name:"test-doctor"
				)

		patient = new Patient(
				id:PATIENT_ID,
				clinicPatientId:CLINIC_PATIENT_ID,
				gender:Gender.MALE,
				clinicId:CLINIC_ID,
				doctorId:DOCTOR_ID
		)
		patientService.createPatient(patient)

		// Schedule Window Reminders
		appointments = appointmentService.findByExternalId(patient.id)

		assertTrue(appointments.size() > 0)
	}

	protected void tearDown() {
		patientService.deletePatient(patient)
		super.tearDown()
	}

    void testSchedulePatientAppointmentReminders() {

        appointmentReminderService.schedulePatientAppointmentReminders(appointments)

		for(Appointment it : appointments) {
            List<Reminder> reminders = reminderService.findByAppointmentId(it.id)
            assertTrue(reminders.size() > 0)
		}
    }

    void testUnschedulePatientAppointmentReminders() {

        appointmentReminderService.unschedulePatientAppointmentReminders(appointments)

		for(Appointment it : appointments) {
            List<Reminder> reminders = reminderService.findByAppointmentId(it.id)
            assertTrue(reminders.isEmpty())
		}
    }

}
