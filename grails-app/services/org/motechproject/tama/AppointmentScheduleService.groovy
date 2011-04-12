package org.motechproject.tama

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.motechproject.tama.dao.AppointmentDao;
import org.motechproject.tama.Patient;

/**
 * Service responsible for generation of appointment schedule
 * @author yyonkov
 *
 */
class AppointmentScheduleService {
	static transactional = false
	def AppointmentDao tamaAppointmentDao
	def config = ConfigurationHolder.config
	/**
	 * N is constant used to determine the start of the window (Start = End - N)
	 */
	private int N = config.tama.n;
	/**
	 * Creates a predefined Care Schedule based on the registration date
	 * @param patientId
	 * @param registrationDate
	 */
	private List<Appointment> createCareSchedule(String patientId, Date registrationDate) {
		Appointment.Followup.values().collect {
			Appointment appointment = new Appointment();
			appointment.setPatientId(patientId);
			appointment.setFollowup(it);
			Calendar cal = Calendar.getInstance();
			cal.setTime(DateUtils.truncate(registrationDate, Calendar.DATE));
			if(it==Appointment.Followup.REGISTERED) {
				appointment.setDate(cal.getTime());
			} else {
				cal.add(Calendar.DATE, it.days);
				appointment.setReminderWindowEnd(cal.getTime());
				cal.add(Calendar.DATE, -N);
				appointment.setReminderWindowStart(cal.getTime());
			}
			appointment
		}
	}

	/**
	 * Service method
	 * @param patient
	 * @param registrationDate
	 * @return
	 */
	def createCareSchedule(Patient patient, Date registrationDate) {
		return createCareSchedule(patient.id, registrationDate)
	}
	def findByPatient(Patient patient) {
		return tamaAppointmentDao.findByPatientId(patient.id)
	}
}
