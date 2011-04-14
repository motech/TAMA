package org.motechproject.tama

import java.util.List;
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.tama.dao.AppointmentDao;
import org.motechproject.tama.dao.ClinicDao;
import org.motechproject.tama.dao.DoctorDao;
import org.motechproject.tama.dao.PatientDao;
import org.motechproject.appointmentreminder.model.Appointment
import org.motechproject.tama.Appointment as TamaAppointment
import grails.test.*

class AppointmentReminderServiceTests extends GroovyTestCase {
	def AppointmentReminderService appointmentReminderService
	def PatientService patientService
	def PatientDao tamaPatientDao
	def AppointmentDao tamaAppointmentDao
	def ClinicDao tamaClinicDao
	def DoctorDao tamaDoctorDao
	def ARPatientDAO appointmentReminderPatientDAO

	static CLINIC_ID = "1234-test-clinic"
	static DOCTOR_ID = "1234-test-doctor"
	static PATIENT_ID = "1234-test-patient-id"
	static CLINIC_PATIENT_ID = "1234-test-clinic-patient-id"

	def clinic
	def doctor
	def patient
	
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
	}

	protected void tearDown() {
		patientService.deletePatient(patient)
		tamaClinicDao.remove(clinic)
		tamaDoctorDao.remove(doctor)
		super.tearDown()
	}

    void testSaveAppointmentDate() {
		// Schedule Window Reminders
		List<TamaAppointment> appointments = tamaAppointmentDao.findByPatientId(patient.id)
		assertTrue(appointments.size()>0)
		List<Appointment> arAppointments = appointmentReminderService.convertToAppointmentReminderAppointments(appointments)
		assertTrue(arAppointments.size()>0)
//		assertTrue(appointments.size()>arAppointments.size())
		appointmentReminderService.schedulePatientAppointmentReminders(arAppointments)

		// Create Concrete Appointments
		for(Appointment it : arAppointments) {
			TamaAppointment ta = tamaAppointmentDao.getAt(it.id)
			Date now = new Date()
			ta.date = now
			appointmentReminderService.saveAppointmentDate(ta)
			Appointment arAppointment = appointmentReminderPatientDAO.getAppointment(it.id)
			assertEquals(now, arAppointment.date)
		}
    }
}
