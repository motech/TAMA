package org.motechproject.tama

import grails.test.GrailsUnitTestCase
import org.motechproject.appointments.api.AppointmentService
import org.motechproject.appointments.api.VisitService
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.appointments.api.model.Visit
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Clinic
import org.motechproject.tama.api.model.Doctor
import org.motechproject.tama.api.model.Patient
import org.motechproject.tama.api.model.Patient.Gender

class AppointmentScheduleServiceTests extends GrailsUnitTestCase {
	def AppointmentScheduleService appointmentScheduleService
    def VisitService visitService
    def AppointmentService appointmentService
    def PatientService patientService
    def PatientDAO patientDao

    def patient

    static CLINIC_ID = UUID.randomUUID().toString();
    static DOCTOR_ID = UUID.randomUUID().toString();
    static PATIENT_ID = UUID.randomUUID().toString();
    static CLINIC_PATIENT_ID = UUID.randomUUID().toString();


    protected void setUp() {
        super.setUp()
        patient = new Patient(
                id:PATIENT_ID,
                clinicPatientId:CLINIC_PATIENT_ID,
                gender:Gender.MALE,
                clinicId:CLINIC_ID,
                doctorId:DOCTOR_ID
        )
        patientDao.add(patient)
    }

    protected void tearDown() {
        patientService.deletePatient(patient)
//		tamaAppointmentDao.getAll().each { tamaAppointmentDao.remove(it)   }
        super.tearDown()
    }

    void testCreateCareSchedule() {
        Date regDate = Calendar.instance.time
		appointmentScheduleService.createCareSchedule(patient.id, regDate);

        List<Visit> visits = visitService.findByExternalId(patient.id)
        assertNotNull(visits);
        assertTrue(visits.size() > 0);
        assertEquals(visits.get(0).visitDate, regDate)

        List<Appointment> appointments = appointmentService.findByExternalId(patient.id)
        assertNotNull(appointments);
        assertTrue(appointments.size() == 5);

		for(a in appointments) {
			assertTrue(a.externalId == patient.id);
			assertNull(a.scheduledDate);
		}
    }
}