package org.motechproject.tama

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

class Doctor extends MotechAuditableDataObject{
	
	@TypeDiscriminator
	String name;
	String clinicId;
	
}
