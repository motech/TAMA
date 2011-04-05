package org.motechproject.tama.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechAuditableRepository;
import org.motechproject.tama.Patient;
import org.motechproject.tama.dao.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PatientDaoImpl extends MotechAuditableRepository<Patient> implements PatientDao {

	@Autowired
    public PatientDaoImpl(@Qualifier("tamaPatientDatabase") CouchDbConnector db) {
        super(Patient.class, db);
        initStandardDesignDocument();
    }
	
	
}
