package org.motechproject.tama

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

class PatientPreferences extends MotechAuditableDataObject {

	@TypeDiscriminator
	String clinicPatientId
    boolean appointmentReminderEnabled; // Is the appointment reminder enabled

    static constraints = {
    }
}
