package org.motechproject.tama

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.tama.model.Patient

/**
 * Service responsible for generation of appointment schedule
 * @author yyonkov
 *
 */
class AppointmentScheduleService {
	static transactional = false
	def AppointmentsDAO appointmentsDao
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
				//TODO: For REGISTERED, we should set a visit date, not an appoitment date
				//appointment.setDate(cal.getTime());
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
