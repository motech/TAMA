package org.motechproject.tama

import org.ektorp.CouchDbInstance
import org.motechproject.appointments.api.AppointmentService
import org.motechproject.appointments.api.VisitService
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.appointments.api.model.Visit
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Patient.Gender

class PatientServiceIntegrationTests extends GroovyTestCase  {

	def CouchDbInstance couchDbInstance
	def PatientService patientService
    def AppointmentService appointmentService
    def VisitService visitService
    def PatientDAO patientDAO

	static CLINIC_ID = UUID.randomUUID().toString();
	static DOCTOR_ID = UUID.randomUUID().toString();
	static PATIENT_ID = UUID.randomUUID().toString();
	static CLINIC_PATIENT_ID = UUID.randomUUID().toString();

    void testCreateDeletePatient() {
        def patient = new Patient(
            id:PATIENT_ID,
            clinicPatientId:CLINIC_PATIENT_ID,
            gender:Gender.MALE,
            clinicId:CLINIC_ID,
            doctorId:DOCTOR_ID
            )
        patientService.createPatient(patient)

        List<Appointment> appointments = appointmentService.findByExternalId(patient.id)
        assertNotNull(appointments);
        assertTrue(appointments.size() == 5);

		for(a in appointments) {
			assertTrue(a.externalId == patient.id);
			assertNull(a.scheduledDate);
		}

        patientService.deletePatient(patient)
        List<Visit> visits = visitService.findByExternalId(patient.id)
        assertNotNull(visits);
        assertTrue(visits.isEmpty());

        appointments = appointmentService.findByExternalId(patient.id)
        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

	void testFindPatientByClinicIdPatientId() {
		def patient = new Patient(
			id:PATIENT_ID,
			clinicPatientId:CLINIC_PATIENT_ID,
			gender:Gender.MALE,
			clinicId:CLINIC_ID,
			doctorId:DOCTOR_ID
			)
		patientService.createPatient(patient)
		
		def p = patientService.findPatientByClinicIdPatientId(CLINIC_ID, CLINIC_PATIENT_ID)
		assertNotNull p
		assertEquals DOCTOR_ID, p.doctorId
		
		patientService.deletePatient(patient)
	}
}
