package org.motechproject.tama

import grails.test.GrailsUnitTestCase

class AppointmentScheduleServiceTests extends GrailsUnitTestCase {
    void testSomething() {
		Patient p = new Patient();
		p.id = "0001";
		AppointmentScheduleService appointmentScheduleService = new AppointmentScheduleService();
		def apps = appointmentScheduleService.createCareSchedule(p, Calendar.instance.time);
		assertNotNull(apps);
		assertTrue(apps.size()>0);
		for(a in apps) {
			assertTrue(a.patientId==p.id);
			assertTrue(a.reminderWindowStart.before(a.reminderWindowEnd));
		}
    }
}
