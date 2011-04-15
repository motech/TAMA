package org.motechproject.tama

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*
import grails.plugins.selenium.*

import org.junit.*

@Mixin(SeleniumAware)
class ClinicVisitPageTests extends GroovyTestCase {
	
	def clinicPatientId = UUID.randomUUID().toString()

	@Before void setUp() {
		selenium.open("/TAMA/patient/list")
    	
    	//set up a test patient
    	selenium.clickAndWaitAndWait("link=Register a new patient")
    	selenium.type("clinicPatientId", clinicPatientId)
    	selenium.type("phoneNumber", "1234123412")
    	selenium.type("confirmPhoneNumber", "1234123412")
    	selenium.type("dateOfBirth", "03-Feb-2000")
    	selenium.type("passcode", "222222")
    	selenium.type("confirmPasscode", "222222")
    	selenium.select("doctorId", "label=Doctor Y")
    	selenium.clickAndWaitAndWait("id=create-btn")
    }

    @After void tearDown() {
		//delete the test patient
    	selenium.open("/TAMA/patient/show/" + clinicPatientId)
    	selenium.clickAndWaitAndWait("_action_delete")
    	selenium.getConfirmation()
        super.tearDown()
    }

    @Test void testScheduling() {
    	selenium.open("/TAMA/appointment/list/"+clinicPatientId)
		
		def xpathToDateInput="//div[@id='visit-list']/table/tbody/tr[2]/td[4]/div/input[@type='text']"
		def xpathToSave="//div[@id='visit-list']/table/tbody/tr[2]/td[4]/div/a[@class='save']"
		def xpathToDelete="//div[@id='visit-list']/table/tbody/tr[2]/td[4]/div/a[@class='delete']"
		
		//get the information of the first window
		def followup = selenium.getText("//div[@id='visit-list']/table/tbody/tr[2]/td[1]")
		def windowStarts = selenium.getText("//div[@id='visit-list']/table/tbody/tr[2]/td[2]")
		
		//get the date in month
		def startDate = windowStarts.substring(0, 2)
		if (startDate.startsWith('0')) startDate=startDate.substring(1)
		
		//click 'Schedule it now' and select the date
		selenium.click(xpathToDateInput)
		selenium.click("link=" + startDate)
		
		//click X to reset
		selenium.click(xpathToDelete)
		assertEquals("Schedule it now", selenium.getValue(xpathToDateInput))
		
		//save it this time
		selenium.click(xpathToDateInput)
		selenium.click("link=" + startDate)
		selenium.click(xpathToSave)
		
		selenium.waitForTextPresent(followup + " is set for " + windowStarts)
		
		if (!startDate.equals("30") && !startDate.equals("31")){
			//update the schedule
			selenium.click(xpathToDateInput)
			selenium.click("link=" + (startDate.toInteger() + 1))
			
			//reset again
			selenium.click(xpathToDelete)
			assertEquals(windowStarts, selenium.getValue(xpathToDateInput))
			
			//save
			selenium.click(xpathToDateInput)
			selenium.click("link=" + (startDate.toInteger() + 1))
			selenium.click(xpathToSave)
			
			selenium.waitForTextPresent(followup + " is set for " + selenium.getValue(xpathToDateInput))
		}
		
		//cancel the schedule
		selenium.click(xpathToDelete)
		selenium.click("//div[div[span='4 week follow-up']]/div[3]/div/button[1]")
		selenium.waitForTextPresent(followup + " is cancelled")
		assertEquals("Schedule it now", selenium.getValue(xpathToDateInput))
    }
}
