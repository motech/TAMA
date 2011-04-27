import org.motechproject.tama.PatientService

import org.motechproject.tama.api.model.Patient.Gender
import org.motechproject.tama.api.dao.ClinicDAO
import org.motechproject.tama.api.dao.DoctorDAO
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Clinic
import org.motechproject.tama.api.model.Doctor
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Patient.InterventionProgram
import org.motechproject.tama.api.model.Patient.Status

class BootStrap {

	def PatientDAO patientDao
	def DoctorDAO doctorDao
	def ClinicDAO clinicDao

	def PatientService patientService
	static String CLINIC_ID="1"

	def init = { servletContext ->
		//FIXME: delete these once we have UI for managing doctor, clinic, etc.
		String doctorXId="1"
		String doctorYId="2"
		String patientId="10"

		if (!clinicDao.contains(CLINIC_ID)){
			Clinic clinic = new Clinic(
					id: CLINIC_ID,
					name:"Test Clinic"
					)
			clinicDao.add(clinic)
		}

		if (!doctorDao.contains(doctorXId)){
			Doctor doctorX = new Doctor(
					id: doctorXId,
					clinicId:CLINIC_ID,
					name:"Doctor X"
					)
			doctorDao.add(doctorX)
		}
		
		if (!doctorDao.contains(doctorYId)){
			Doctor doctorY = new Doctor(
					id: doctorYId,
					clinicId:CLINIC_ID,
					name:"Doctor Y"
					)
			doctorDao.add(doctorY)
		}
		
		if (!patientDao.contains(patientId)) {
			Patient patient = new Patient(
				id:patientId,
				clinicPatientId:"1234",
				gender:Gender.MALE,
				clinicId:CLINIC_ID,
				doctorId:doctorXId,
				passcode:"passwd",
				phoneNumber:"6046894123",
				interventionProgram: InterventionProgram.PROGRAM,
				dateOfBirth: new Date(),
				status:Status.ACTIVE
				)
			patientService.createPatient(patient)
		}
	}

	def destroy = {
	}
}
