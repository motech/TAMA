package org.motechproject.tama

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.pool.impl.GenericKeyedObjectPool.Config;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
 * Service responsible for generation of appointment schedule
 * @author yyonkov
 *
 */
class AppointmentScheduleService {
	static transactional = false
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
		List<Integer> offsets = Arrays.asList(7,4*7,12*7,24*7,36*7,48*7);
		List<Appointment> careSchedule = new ArrayList<Appointment>();
		for(int days : offsets) {
			Appointment appointment = new Appointment();
			appointment.setPatientId(patientId);
			Calendar cal = Calendar.getInstance();
			cal.setTime(registrationDate);
			// TODO should we set the time to 12:00 AM
			//			cal.set(Calendar.HOUR, 0);
			//			cal.set(Calendar.MINUTE, 0);
			cal.add(Calendar.DATE, +days);
			appointment.setReminderWindowEnd(cal.getTime());
			cal.add(Calendar.DATE, -N);
			appointment.setReminderWindowStart(cal.getTime());
			careSchedule.add(appointment);
		}
		return careSchedule;
	}

	/**
	 * Service method
	 * @param patient
	 * @param registrationDate
	 * @return
	 */
	def createCareSchedule(patient, Date registrationDate) {
		return createCareSchedule(patient.id, registrationDate)
	}
}
