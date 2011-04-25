package org.motechproject.tama

import org.apache.commons.logging.LogFactory
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.tama.dao.ClinicDAO
import org.motechproject.tama.dao.DoctorDAO
import org.motechproject.tama.dao.PatientDAO
import org.motechproject.tama.model.Patient
import org.motechproject.tama.model.Clinic
import org.motechproject.tama.model.Doctor

class PatientService {

    static transactional = false
	
	static final log = LogFactory.getLog(this)
	
	def PatientDAO patientDao
	def DoctorDAO doctorDao
	def ClinicDAO clinicDao
	def AppointmentsDAO appointmentsDao
	
	def AppointmentScheduleService appointmentScheduleService

    def listPatients() {
		//TODO: add pagination support
		return tamaPatientDao.getAll()
    }
	
	def getPatient(String id){
		return tamaPatientDao.get(id)
	}
	
	def createPatient(Patient patient){
		if (!patient.id) {
			patient.id = generateId()
		}
		tamaPatientDao.add(patient)
		
		log.info("Created ${patient}")

		// Create Care Schedule and add Appointments to Patient database
		appointmentScheduleService.createCareSchedule(patient, new Date()).each { tamaAppointmentDao.add(it) }
		
		Clinic clinic = tamaClinicDao.get(patient.clinicId)
		Doctor doctor = tamaDoctorDao.get(patient.doctorId)
		
		return patient
	}
	
	def updatePatient(Patient patient){
		tamaPatientDao.update(patient)
		
		log.debug("Updated ${patient}")
		
		Clinic clinic = tamaClinicDao.get(patient.clinicId)
		Doctor doctor = tamaDoctorDao.get(patient.doctorId)
		
		return patient
	}

	def deletePatient(Patient patient){
		tamaAppointmentDao.findByPatientId(patient.id).each { tamaAppointmentDao.remove(it)   }
		tamaPatientDao.remove(patient)

		log.info("Deleted ${patient}")
	}
	
	def findPatientByClinicPatientId(String clinicId, String clinicPatientId) {
		return tamaPatientDao.findByClinicPatientId(clinicId, clinicPatientId);
	}
	
	//FIXME: temporary workaround due to the limitation of IVR URL length. we should remove this once we can use UUID
	private String generateId(){
		def id
		def patients = tamaPatientDao.getAll()
		if (patients) {
			try{
				def maxId = Integer.parseInt(patients.last().id)
				id = (++maxId)+""
			}catch(Exception e){}
		} else {
			id = "1"
		}
		return id
	}
}
