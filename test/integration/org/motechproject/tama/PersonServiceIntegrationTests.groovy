package org.motechproject.tama

import grails.test.*

import org.ektorp.CouchDbInstance
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.tama.dao.AppointmentDao;
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao
import org.motechproject.tama.dao.PatientDao

class PersonServiceIntegrationTests extends GroovyTestCase  {

	def CouchDbInstance couchDbInstance
	def ClinicDao tamaClinicDao
	def DoctorDao tamaDoctorDao
	def PatientService patientService
	def ARPatientDAO appointmentReminderPatientDAO
	def PatientDao tamaPatientDao
	def AppointmentDao tamaAppointmentDao

	static CLINIC_ID = "1234-test-clinic"
	static DOCTOR_ID = "1234-test-doctor"
	static PATIENT_ID = "1234-test-patient-id"
	static CLINIC_PATIENT_ID = "1234-test-clinic-patient-id"

	def clinic
	def doctor

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
	}

	protected void tearDown() {
		tamaClinicDao.remove(clinic)
		tamaDoctorDao.remove(doctor)
//		tamaAppointmentDao.getAll().each { tamaAppointmentDao.remove(it)   }
		super.tearDown()
	}

	void testPatientCRUD() {
		def patient = new Patient(
			id:PATIENT_ID,
			clinicPatientId:CLINIC_PATIENT_ID,
			gender:Gender.MALE,
			clinicId:CLINIC_ID,
			doctorId:DOCTOR_ID
			)
		patientService.createPatient(patient)
		
		// check if there are any appointments in the schedule
		assertTrue 0<tamaAppointmentDao.findByPatientId(patient.id).size()
		
		
		assertTrue tamaPatientDao.contains(PATIENT_ID)
		assertTrue appointmentReminderPatientDAO.contains(patient.id)

		def arPatient = appointmentReminderPatientDAO.get(patient.id)
		assertEquals CLINIC_ID, arPatient.clinic.id
		assertEquals DOCTOR_ID, arPatient.doctor.id
		assertEquals CLINIC_ID, arPatient.doctor.clinic.id

		patient.gender = Gender.HIJIRA
		patientService.updatePatient(patient)

		assertEquals Gender.HIJIRA, tamaPatientDao.get(patient.id).gender
		assertEquals Gender.HIJIRA.toString(), appointmentReminderPatientDAO.get(patient.id).gender

		patientService.deletePatient(patient)
		assertFalse tamaPatientDao.contains(patient.id)
		// check if there are any appointments in the schedule
		assertTrue 0==tamaAppointmentDao.findByPatientId(patient.id).size()
	}

	void testFindPatientByClinicPatientId() {
		def patient = new Patient(
			id:PATIENT_ID,
			clinicPatientId:CLINIC_PATIENT_ID,
			gender:Gender.MALE,
			clinicId:CLINIC_ID,
			doctorId:DOCTOR_ID
			)
		tamaPatientDao.add(patient)
		
		def p = patientService.findPatientByClinicPatientId(CLINIC_ID, CLINIC_PATIENT_ID)
		assertNotNull p
		assertEquals DOCTOR_ID, p.doctorId
		
		tamaPatientDao.remove(patient)
	}
}
