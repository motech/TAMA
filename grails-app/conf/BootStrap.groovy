import org.motechproject.tama.Clinic
import org.motechproject.tama.Doctor
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao

class BootStrap {

	def DoctorDao tamaDoctorDao
	def ClinicDao tamaClinicDao

	def init = { servletContext ->
		//FIXME: delete these once we have UI for managing doctor and clinic
		String clinicId="1"
		String doctorXId="1"
		String doctorYId="2"

		if (!tamaClinicDao.contains(clinicId)){
			Clinic clinic = new Clinic(
					id: clinicId,
					name:"Test Clinic"
					)
			tamaClinicDao.add(clinic)
		}

		if (!tamaDoctorDao.contains(doctorXId)){
			Doctor doctorX = new Doctor(
					id: doctorXId,
					clinicId:clinicId,
					name:"Doctor X"
					)
			tamaDoctorDao.add(doctorX)
		}
		
		if (!tamaDoctorDao.contains(doctorYId)){
			Doctor doctorY = new Doctor(
					id: doctorYId,
					clinicId:clinicId,
					name:"Doctor Y"
					)
			tamaDoctorDao.add(doctorY)
		}
	}

	def destroy = {
	}
}
