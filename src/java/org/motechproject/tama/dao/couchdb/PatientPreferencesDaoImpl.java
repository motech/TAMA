package org.motechproject.tama.dao.couchdb;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechAuditableRepository;
import org.motechproject.tama.PatientPreferences;
import org.motechproject.tama.dao.PatientPreferencesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PatientPreferencesDaoImpl extends MotechAuditableRepository<PatientPreferences>
		implements PatientPreferencesDao {

	@Autowired
    public PatientPreferencesDaoImpl(@Qualifier("tamaPatientDatabase") CouchDbConnector db) {
        super(PatientPreferences.class, db);
        initStandardDesignDocument();
    }

	public PatientPreferences getByClinicPatientId(String clinicPatientId) {
		//TODO: implement query
        return null;

	}

}
