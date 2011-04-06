package org.motechproject.tama

import org.motechproject.tama.dao.PatientDao
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.appointmentreminder.model.Patient as ARPatient

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
		
		ARPatient arPatient = new ARPatient(
			clinicPatientId:patient.clinicPatientId,
			gender:patient.gender.toString()
			)
		appointmentReminderPatientDAO.add(arPatient)
	}
	
	def updatePatient(Patient patient){
		tamaPatientDao.update(patient)
		
	}
}
