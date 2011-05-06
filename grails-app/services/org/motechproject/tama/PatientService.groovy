package org.motechproject.tama

import org.apache.commons.logging.LogFactory
import org.motechproject.appointments.api.AppointmentService
import org.motechproject.appointments.api.ReminderService
import org.motechproject.appointments.api.VisitService
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient

class PatientService {

    static transactional = false
	
	static final log = LogFactory.getLog(this)
	
	def PatientDAO patientDao
	def AppointmentService appointmentService
    def VisitService visitService
    def ReminderService reminderService
	
	def AppointmentScheduleService appointmentScheduleService

    def listPatients() {
		//TODO: add pagination support
		return patientDao.getAll()
    }
	
	def getPatient(String id){
		return patientDao.get(id)
	}
	
	def createPatient(Patient patient){
		patientDao.add(patient)
		
		log.info("Created ${patient}")

		// Create Care Schedule and add Appointments to Patient database
		appointmentScheduleService.createCareSchedule(patient.id, new Date())
		
		return patient
	}
	
	def updatePatient(Patient patient){
		patientDao.update(patient)
		
		log.debug("Updated ${patient}")

		return patient
	}

	def deletePatient(Patient patient){
		appointmentService.findByExternalId(patient.id).each { appointmentService.removeAppointment(it)   }
        visitService.findByExternalId(patient.id).each { visitService.removeVisit(it)   }
        reminderService.findByAppointmentId(patient.id).each { reminderService.removeVisit(it)   }

		patientDao.remove(patient)

		log.info("Deleted ${patient}")
	}
	
	def findPatientByClinicIdPatientId(String clinicId, String clinicPatientId) {
		return patientDao.findByClinicIdPatientId(clinicId, clinicPatientId);
	}
}
