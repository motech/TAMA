package org.motechproject.tama

import java.util.Date;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;


class Appointment extends MotechAuditableDataObject {
	String patientId;
	@TypeDiscriminator
	Date reminderWindowStart;
	Date reminderWindowEnd;
	Date date;
	
	static constraints = {
		patientId(blank: false)
	}
}
