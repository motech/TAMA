package org.motechproject.tama

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

class Clinic extends MotechAuditableDataObject{
	
	@TypeDiscriminator
	String name;

}
