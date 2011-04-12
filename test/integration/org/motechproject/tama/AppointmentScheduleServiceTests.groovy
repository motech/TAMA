package org.motechproject.tama

import grails.test.GrailsUnitTestCase

class AppointmentScheduleServiceTests extends GrailsUnitTestCase {
	def AppointmentScheduleService appointmentScheduleService
    void testSomething() {
		Patient p = new Patient();
		p.id = "0001";
		def apps = appointmentScheduleService.createCareSchedule(p, Calendar.instance.time);
		assertNotNull(apps);
		assertTrue(apps.size()>0);
		for(a in apps) {
			assertTrue(a.patientId==p.id);
			if (a.followup==Appointment.Followup.REGISTERED) {
				assertNotNull(a.date);
				assertNull(a.reminderWindowStart);
				assertNull(a.reminderWindowEnd);
			} else {
				assertNull(a.date);
				assertTrue(a.reminderWindowStart.before(a.reminderWindowEnd));
			}
		}
    }
}
