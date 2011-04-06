package org.motechproject.tama

import grails.test.*

import org.ektorp.CouchDbInstance
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
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
	
	static CLINIC_ID = "1234-test-clinic"
	static DOCTOR_ID = "1234-test-doctor"
		
    protected void setUp() {
		super.setUp()
		
		Clinic clinic = new Clinic(
			id: CLINIC_ID,
			name:"test-clinic"
			)
		tamaClinicDao.add(clinic)
		
		Doctor doctor = new Doctor(
			id: DOCTOR_ID,
			clinicId:CLINIC_ID,
			name:"test-doctor"
			)
		tamaDoctorDao.add(doctor)
    }

    protected void tearDown() {
		couchDbInstance.deleteDatabase("tama_patients");
		couchDbInstance.deleteDatabase("tama_clinics");
		couchDbInstance.deleteDatabase("tama_doctors");
		couchDbInstance.deleteDatabase("appointments");
		super.tearDown()
    }

    void testPatientCRUD() {
		Patient patient = new Patient(
			clinicPatientId:"1234", 
			gender:Gender.MALE,
			clinicId:CLINIC_ID,
			doctorId:DOCTOR_ID
			)
		patientService.createPatient(patient)
		assertTrue tamaPatientDao.contains(patient.id)
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
    }
	
}
