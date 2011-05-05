package org.motechproject.tama

import org.apache.commons.logging.LogFactory
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.tama.api.dao.ClinicDAO
import org.motechproject.tama.api.dao.DoctorDAO
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Clinic
import org.motechproject.tama.api.model.Doctor
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
		return patientDao.getAll()
    }
	
	def getPatient(String id){
		return patientDao.get(id)
	}
	
	def createPatient(Patient patient){
		if (!patient.id) {
			patient.id = generateId()
		}
		patientDao.add(patient)
		
		log.info("Created ${patient}")

		// Create Care Schedule and add Appointments to Patient database
		appointmentScheduleService.createCareSchedule(patient, new Date())
		
		Clinic clinic = clinicDao.get(patient.clinicId)
		Doctor doctor = doctorDao.get(patient.doctorId)
		
		return patient
	}
	
	def updatePatient(Patient patient){
		patientDao.update(patient)
		
		log.debug("Updated ${patient}")

		return patient
	}

	def deletePatient(Patient patient){
		appointmentsDao.findByPatientId(patient.id).each { appointmentsDao.removeAppointment(it)   }
		patientDao.remove(patient)

		log.info("Deleted ${patient}")
	}
	
	def findPatientByClinicIdPatientId(String clinicId, String clinicPatientId) {
		return patientDao.findByClinicIdPatientId(clinicId, clinicPatientId);
	}
	
	//FIXME: temporary workaround due to the limitation of IVR URL length. we should remove this once we can use UUID
	private String generateId(){
		def id
		def patients = patientDao.getAll()
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
