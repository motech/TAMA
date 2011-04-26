package org.motechproject.tama

import org.motechproject.tama.dao.DoctorDAO

class DoctorService {

    static transactional = false

	def DoctorDAO doctorDao
	
    def findDoctorsByClinicId(String clinicId) {
		return doctorDao.findByClinicId(clinicId);
    }
	
	def findDoctorById(String id){
		return doctorDao.get(id)
	}
}
