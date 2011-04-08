package org.motechproject.tama

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

class PatientPreferences extends MotechAuditableDataObject {

	@TypeDiscriminator
	String clinicPatientId
	String clinicId
    Boolean appointmentReminderEnabled; // Is the appointment reminder enabled
	Integer bestTimeToCallHour;
	Integer bestTimeToCallMinute;
	
    static constraints = {
    }
}
