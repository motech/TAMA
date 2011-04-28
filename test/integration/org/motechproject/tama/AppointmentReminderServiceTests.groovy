package org.motechproject.tama

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.tama.dao.AppointmentDao;
import org.motechproject.tama.dao.ClinicDao;
import org.motechproject.tama.dao.DoctorDao;
import org.motechproject.tama.dao.PatientDao;
import org.motechproject.appointmentreminder.model.Appointment
import org.motechproject.tama.model.Appointment as TamaAppointment
import org.motechproject.tama.model.Clinic
import org.motechproject.tama.model.Patient
import org.motechproject.tama.model.Doctor
import org.motechproject.tama.model.Gender
import grails.test.*

class AppointmentReminderServiceTests extends GroovyTestCase {
	def AppointmentReminderService appointmentReminderService
	def PatientService patientService
	def PatientDao tamaPatientDao
	def AppointmentDao tamaAppointmentDao
	def ClinicDao tamaClinicDao
	def DoctorDao tamaDoctorDao
	def ARPatientDAO appointmentReminderPatientDAO
	def config = ConfigurationHolder.config
	/**
	 * M is constant used to determine the start of the actual appointment window (Start = End - M)
	 */
	private int M = config.tama.m;

	static CLINIC_ID = UUID.randomUUID().toString();
	static DOCTOR_ID = UUID.randomUUID().toString();
	static PATIENT_ID = UUID.randomUUID().toString();
	static CLINIC_PATIENT_ID = UUID.randomUUID().toString();

	def clinic
	def doctor
	def patient
	
	List<Appointment> arAppointments
	
	protected void setUp() {
		super.setUp()
		clinic = new Clinic(
				id: CLINIC_ID,
				name:"test-clinic"
				)
		tamaClinicDao.add(clinic)

		doctor = new Doctor(
				id: DOCTOR_ID,
				clinicId:CLINIC_ID,
				name:"test-doctor"
				)
		tamaDoctorDao.add(doctor)
		patient = new Patient(
				id:PATIENT_ID,
				clinicPatientId:CLINIC_PATIENT_ID,
				gender:Gender.MALE,
				clinicId:CLINIC_ID,
				doctorId:DOCTOR_ID
		)
		patientService.createPatient(patient)

		// Schedule Window Reminders
		List<TamaAppointment> appointments = tamaAppointmentDao.findByPatientId(patient.id)
		assertTrue(appointments.size()>0)
		arAppointments = appointmentReminderService.convertToAppointmentReminderAppointments(appointments)
		assertTrue(arAppointments.size()>0)
		appointmentReminderService.schedulePatientAppointmentReminders(arAppointments)
	}

	protected void tearDown() {
		appointmentReminderService.unschedulePatientAppointmentReminders(patient.id)
		patientService.deletePatient(patient)
		tamaClinicDao.remove(clinic)
		tamaDoctorDao.remove(doctor)
		super.tearDown()
	}

    void testCreateDeleteAppointmentDate() {

		for(Appointment it : arAppointments) {
			// Create Concrete Appointments
			TamaAppointment ta = tamaAppointmentDao.getAt(it.id)
			Date now = DateUtils.truncate(new Date(), Calendar.DATE)
			ta.date = now
			appointmentReminderService.saveAppointmentDate(ta)
			Appointment arAppointment = appointmentReminderPatientDAO.getAppointment(it.id)
			assertEquals(now, arAppointment.date)
			assertEquals(now, arAppointment.reminderWindowEnd)
			assertEquals(DateUtils.addDays(now, -M), arAppointment.reminderWindowStart)
			// Delete Concrete Appointment
			ta.date = null
			appointmentReminderService.saveAppointmentDate(ta)
			arAppointment = appointmentReminderPatientDAO.getAppointment(it.id)
			assertEquals(null, arAppointment.date)
			assertEquals(ta.reminderWindowEnd, arAppointment.reminderWindowEnd)
			assertEquals(ta.reminderWindowStart, arAppointment.reminderWindowStart)
		}
    }
}
