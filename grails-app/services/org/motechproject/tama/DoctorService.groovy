package org.motechproject.tama

import org.motechproject.tama.dao.DoctorDao;

class DoctorService {

    static transactional = false

	def DoctorDao tamaDoctorDao
	
    def findDoctorsByClinicId(String clinicId) {
		return tamaDoctorDao.findByClinicId(clinicId);
    }
	
	def findDoctorById(String id){
		return tamaDoctorDao.get(id)
	}
}
