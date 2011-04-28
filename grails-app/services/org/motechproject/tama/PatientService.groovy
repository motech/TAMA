package org.motechproject.tama

import org.apache.commons.logging.LogFactory
import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.appointmentreminder.model.Clinic as ARClinic
import org.motechproject.appointmentreminder.model.Doctor as ARDoctor
import org.motechproject.appointmentreminder.model.Patient as ARPatient
import org.motechproject.tama.dao.AppointmentDao
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao
import org.motechproject.tama.dao.PatientDao
import org.motechproject.tama.model.Patient
import org.motechproject.tama.model.Clinic
import org.motechproject.tama.model.Doctor


class PatientService {

    static transactional = false
	
	static final log = LogFactory.getLog(this)
	
	def PatientDao tamaPatientDao
	def DoctorDao tamaDoctorDao
	def ClinicDao tamaClinicDao
	def AppointmentDao tamaAppointmentDao
	
	def ARPatientDAO appointmentReminderPatientDAO
	def AppointmentScheduleService appointmentScheduleService

    def listPatients() {
		//TODO: add pagination support
		return tamaPatientDao.getAll()
    }
	
	def getPatient(String id){
		return tamaPatientDao.get(id)
	}
	
	def createPatient(Patient patient){
		if (patient.id == null) {
			patient.id = generateId()
		}
		tamaPatientDao.add(patient)
		
		log.info("Created ${patient}")

		// Create Care Schedule and add Appointments to Patient database
		appointmentScheduleService.createCareSchedule(patient, new Date()).each { tamaAppointmentDao.add(it) }
		
		Clinic clinic = tamaClinicDao.get(patient.clinicId)
		Doctor doctor = tamaDoctorDao.get(patient.doctorId)
		
		createPatientInAppointmentReminder(patient, clinic, doctor)
		
		return patient
	}
	
	private createPatientInAppointmentReminder(Patient patient, Clinic clinic, Doctor doctor){
		ARClinic arClinic = new ARClinic(id:clinic.id, name:clinic.name)
		ARDoctor arDoctor = new ARDoctor(id:doctor.id, name:doctor.name, clinic:arClinic)
		
		ARPatient arPatient = new ARPatient(
			id:patient.id,
			clinicPatientId:patient.clinicPatientId,
			gender:patient.gender.toString(),
			clinic:arClinic,
			doctor:arDoctor,
			phoneNumber:patient.phoneNumber
			)
		
		appointmentReminderPatientDAO.add(arPatient)
		
		log.debug("Created AR patient [id=${patient.id}]")
		
		return arPatient
	}
		
	def updatePatient(Patient patient){
		tamaPatientDao.update(patient)
		
		log.debug("Updated ${patient}")
		
		Clinic clinic = tamaClinicDao.get(patient.clinicId)
		Doctor doctor = tamaDoctorDao.get(patient.doctorId)
		
		updatePatientInAppointmentReminder(patient, clinic, doctor)
		return patient
	}
	
	private updatePatientInAppointmentReminder(Patient patient, Clinic clinic, Doctor doctor){
		ARClinic arClinic = new ARClinic(id:clinic.id, name:clinic.name)
		ARDoctor arDoctor = new ARDoctor(id:doctor.id, name:doctor.name, clinic:arClinic)
		
		ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id);
		arPatient.clinicPatientId = patient.clinicPatientId
		arPatient.gender = patient.gender.toString()
		arPatient.clinic = arClinic
		arPatient.doctor = arDoctor
		arPatient.phoneNumber = patient.phoneNumber
		appointmentReminderPatientDAO.update(arPatient)
		
		log.debug("Updated AR patient [id=${patient.id}]")
		
		return arPatient
	}
	
	def deletePatient(Patient patient){
		tamaAppointmentDao.findByPatientId(patient.id).each { tamaAppointmentDao.remove(it)   }
		tamaPatientDao.remove(patient)
		log.info("Deleted ${patient}")
		ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id)
		appointmentReminderPatientDAO.remove(arPatient)
		log.debug("Deleted AR patient [id=${patient.id}]")
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
