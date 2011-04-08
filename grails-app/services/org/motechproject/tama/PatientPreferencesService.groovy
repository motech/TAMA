package org.motechproject.tama

import org.motechproject.tama.dao.PatientDao;
import org.motechproject.tama.dao.PatientPreferencesDao;

class PatientPreferencesService {

    static transactional = false

	def PatientPreferencesDao tamaPatientPreferencesDao
	
	/**
	 * Create a non-existant patient preferences object
	 * @param patientPreferences object to be stored
	 * @return stored object with populated id
	 */
	def createPatientPreferences(PatientPreferences patientPreferences) {
		tamaPatientPreferencesDao.add(patientPreferences)
		return patientPreferences
	}
	
	/**
	 * Update an existing PatientPreferences object
	 * @param patientPreferences object to be updated
	 * @return updated object
	 */
	def updatePatientPreferences(PatientPreferences patientPreferences) {
		tamaPatientPreferencesDao.update(patientPreferences)
		return patientPreferences
	}
	
	/**
	 * Remove an existing PatientPreferences object
	 * @param patientPreferences
	 */
	def deletePatientPreferences(PatientPreferences patientPreferences) {
		tamaPatientPreferencesDao.remove(patientPreferences)
	}
	
	/**
	 * Find a patient's preferences base upon their clinic id and clinic patient id
	 * @param clinicId the id of the clinic the patient resides within
	 * @param clinicPatientId the clinic id of the patient
	 * @return An instance of the requested PatientPreferences object
	 */
    def findByClinicPatientId(String clinicId, String clinicPatientId) {
		return tamaPatientPreferencesDao.findByClinicPatientId(clinicId, clinicPatientId)
    }
}
