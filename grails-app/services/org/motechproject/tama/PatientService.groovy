package org.motechproject.tama

import org.motechproject.tama.dao.PatientDao
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO

class PatientService {

    static transactional = false
	
	def PatientDao tamaPatientDao
	
	def ARPatientDAO appointmentReminderPatientDAO

    def listPatients() {
		//TODO: add pagination support
		return tamaPatientDao.getAll()
    }
	
	def getPatient(String id){
		return tamaPatientDao.get(id)
	}
	
	def createPatient(Patient patient){
		tamaPatientDao.add(patient)

	}
	
	def updatePatient(Patient patient){
		tamaPatientDao.update(patient)
		
	}
}
