package org.motechproject.tama

import java.util.Date;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;


class Appointment extends MotechAuditableDataObject {
	String patientId;
	Date reminderWindowStart;
	Date reminderWindowEnd;
	Date date;
	public static enum Followup {
		REGISTERED("Registered",7),
		WEEK4("4 week follow-up",4*7),
		WEEK12("12 week follow-up",4*12),
		WEEK24("24 week follow-up",4*24),
		WEEK36("36 week follow-up",4*36),
		WEEK48("48 week follow-up",4*48)

		final String value
		final int days

		Followup(String value, int days) {
			this.value=value
			this.days=days
		}
		@Override
		String toString(){
			value
		}
		String getKey(){
			name()
		}
		int getDays() {
			this.days
		}
	}
	@TypeDiscriminator
	Followup followup;
	
	static constraints = {
		patientId(blank: false)
	}
}
