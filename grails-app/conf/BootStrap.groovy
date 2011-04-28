import org.motechproject.tama.model.Clinic
import org.motechproject.tama.model.Doctor
import org.motechproject.tama.model.Gender
import org.motechproject.tama.model.InterventionProgram
import org.motechproject.tama.model.Patient
import org.motechproject.tama.PatientService
import org.motechproject.tama.model.Status
import org.motechproject.tama.dao.ClinicDao
import org.motechproject.tama.dao.DoctorDao
import org.motechproject.tama.dao.PatientDao

class BootStrap {

	def DoctorDao tamaDoctorDao
	def ClinicDao tamaClinicDao
	def PatientDao tamaPatientDao
	def PatientService patientService
	static String CLINIC_ID="1"

	def init = { servletContext ->
		//FIXME: delete these once we have UI for managing doctor, clinic, etc.
		String doctorXId="1"
		String doctorYId="2"
		String patientId="10"

		if (!tamaClinicDao.contains(CLINIC_ID)){
			Clinic clinic = new Clinic(
					id: CLINIC_ID,
					name:"Test Clinic"
					)
			tamaClinicDao.add(clinic)
		}

		if (!tamaDoctorDao.contains(doctorXId)){
			Doctor doctorX = new Doctor(
					id: doctorXId,
					clinicId:CLINIC_ID,
					name:"Doctor X"
					)
			tamaDoctorDao.add(doctorX)
		}
		
		if (!tamaDoctorDao.contains(doctorYId)){
			Doctor doctorY = new Doctor(
					id: doctorYId,
					clinicId:CLINIC_ID,
					name:"Doctor Y"
					)
			tamaDoctorDao.add(doctorY)
		}
		
		if (!tamaPatientDao.contains(patientId)) {
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
