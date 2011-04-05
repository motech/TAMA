package org.motechproject.tama

import grails.test.*

import org.ektorp.CouchDbInstance
import org.motechproject.tama.dao.ClinicDao

class PersistenceIntegrationTests extends GroovyTestCase  {
	
	def CouchDbInstance couchDbInstance
	def ClinicDao tamaClinicDao
		
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
		//couchDbInstance.deleteDatabase("tama_clinics");
		super.tearDown()
    }

    void testSomething() {
		//Clinic clinic = new Clinic(name:"testclinic")
		//tamaClinicDao.add(clinic)
    }
}
