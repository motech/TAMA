package org.motechproject.tama

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*
import grails.plugins.selenium.*

import org.junit.*

@Mixin(SeleniumAware)
class PatientPageTests extends GroovyTestCase {
    @Before void setUp() {
    }

    @After void tearDown() {
        super.tearDown()
    }

    @Test void patientCRUD() {
		selenium.open("/TAMA/patient/list")
		assertTrue(selenium.isTextPresent("Patients"))
		
		//CREATE
        selenium.clickAndWaitAndWait("link=Register a new patient")
		def clinicPatientId = UUID.randomUUID().toString()
        selenium.type("clinicPatientId", clinicPatientId)
        selenium.type("phoneNumber", "1234123412")
        selenium.type("confirmPhoneNumber", "1234123412")
        selenium.type("dateOfBirth", "03-02-2000")
        selenium.type("passcode", "222222")
        selenium.type("confirmPasscode", "222222")
        selenium.select("doctorId", "label=Doctor Y")
        selenium.clickAndWaitAndWait("id=create-btn")
		assertTrue(selenium.isTextPresent("Patient ${clinicPatientId} created"))
		selenium.clickAndWaitAndWait("link=Patients")
		
		//READ
		selenium.clickAndWaitAndWait("link=${clinicPatientId}")
		selenium.isTextPresent(clinicPatientId)
		selenium.isTextPresent("1234123412")
		selenium.isTextPresent("03-02-2000")
		
		//UPDATE
		selenium.clickAndWaitAndWait("_action_edit")
		selenium.select("gender", "label=Hijira")
		selenium.clickAndWaitAndWait("_action_update")
		assertTrue(selenium.isTextPresent("Patient ${clinicPatientId} updated"))
		assertTrue(selenium.isTextPresent("Hijira"))
		
		//DELETE
		selenium.clickAndWaitAndWait("_action_delete")
		selenium.getConfirmation()
		assertTrue(selenium.isTextPresent("Patient ${clinicPatientId} deleted"))
		selenium.open("/TAMA/patient/list")
		assertFalse(selenium.isTextPresent("${clinicPatientId}"))
    }
}
