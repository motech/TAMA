package org.motechproject.tama

import java.util.Set;

import org.ektorp.docref.CascadeType;
import org.ektorp.docref.DocumentReferences;
import org.ektorp.docref.FetchType;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

class Patient extends MotechAuditableDataObject{

    static constraints = {
    }
	
	@TypeDiscriminator
	String clinicPatientId
	String clinicId
	String doctorId
	Gender gender
	String passcode
	String phoneNumber
	InterventionProgram interventionProgram = InterventionProgram.PROGRAM
	Date dateOfBirth
	Status status = Status.ACTIVE
	@DocumentReferences(fetch = FetchType.LAZY, descendingSortOrder = true, orderBy = "windowStartDate", backReference = "patientId" )
	Set<Appointment> appointments;
	
	@Override
	public String toString() {
		return "Patient [id=" + id + ", clinicPatientId=" + clinicPatientId + ", clinicId=" + clinicId + "]";
	}
	
}
