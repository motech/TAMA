package org.motechproject.tama.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechAuditableRepository;
import org.motechproject.tama.Doctor;
import org.motechproject.tama.dao.DoctorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DoctorDaoImpl extends MotechAuditableRepository<Doctor> implements DoctorDao {

	@Autowired
    public DoctorDaoImpl(@Qualifier("tamaDoctorDatabase") CouchDbConnector db) {
        super(Doctor.class, db);
        initStandardDesignDocument();
    }
	
	
}
