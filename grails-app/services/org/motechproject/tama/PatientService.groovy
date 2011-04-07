package org.motechproject.tama

import org.motechproject.appointmentreminder.dao.PatientDAO as ARPatientDAO
import org.motechproject.appointmentreminder.model.Doctor as ARDoctor
import org.motechproject.appointmentreminder.model.Patient as ARPatient
import org.motechproject.appointmentreminder.model.Clinic as ARClinic
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao
import org.motechproject.tama.dao.PatientDao

class PatientService {

    static transactional = false
	
	def PatientDao tamaPatientDao
	def DoctorDao tamaDoctorDao
	def ClinicDao tamaClinicDao
	
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
		return arPatient
	}
		
	def updatePatient(Patient patient){
		tamaPatientDao.update(patient)
		
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
		arPatient.phoneNumber = arPatient.phoneNumber
		appointmentReminderPatientDAO.update(arPatient)
		return arPatient
	}
	
	def deletePatient(Patient patient){
		tamaPatientDao.remove(patient)
		ARPatient arPatient = appointmentReminderPatientDAO.get(patient.id)
		appointmentReminderPatientDAO.remove(arPatient)
	}
	
	def findPatientByClinicPatientId(String clinicId, String clinicPatientId) {
		return tamaPatientDao.findByClinicPatientId(clinicId, clinicPatientId);
	}
}
