package org.motechproject.tama

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
}
