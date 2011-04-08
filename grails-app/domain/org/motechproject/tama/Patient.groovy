package org.motechproject.tama

import java.util.Set;

import org.ektorp.docref.CascadeType;
import org.ektorp.docref.DocumentReferences;
import org.ektorp.docref.FetchType;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.appointmentreminder.model.Appointment;
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
<<<<<<< HEAD
	Status status = Status.ACTIVE
=======
	Status status
	@DocumentReferences(fetch = FetchType.LAZY, descendingSortOrder = true, orderBy = "windowStartDate", backReference = "patientId" )
	Set<Appointment> appointments;
>>>>>>> 7129df390a417ecad9d2a5a5b2ae829612ab7c60
}
