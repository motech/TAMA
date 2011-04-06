package org.motechproject.tama

import grails.test.*

import org.ektorp.CouchDbInstance
import org.motechproject.tama.dao.ClinicDao

class PersistenceIntegrationTests extends GroovyTestCase  {
	
	def CouchDbInstance couchDbInstance
	def ClinicDao tamaClinicDao
	def PatientService patientService
		
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
		//couchDbInstance.deleteDatabase("tama_patients");
		super.tearDown()
    }

    void testSomething() {
		//Patient patient = new Patient(clinicPatientId:"1234", gender:Gender.MALE)
		//patientService.createPatient(patient)
		
		//Clinic clinic = new Clinic(name:"testclinic")
		//tamaClinicDao.add(clinic)
    }
}
