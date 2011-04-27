package org.motechproject.tama

import org.apache.commons.lang.time.DateUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.dao.VisitsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.appointments.api.model.Visit
import org.motechproject.tama.api.model.AppointmentSchedule
import org.motechproject.tama.api.model.Patient

/**
 * Service responsible for generation of appointment schedule
 * @author yyonkov
 *
 */
class AppointmentScheduleService {
	static transactional = false
	def AppointmentsDAO appointmentsDao
    def VisitsDAO visitDao

	def config = ConfigurationHolder.config

	/**
	 * Creates a predefined Care Schedule based on the registration date
	 * @param patientId
	 * @param registrationDate
	 */
	private List<Appointment> createCareSchedule(String patientId, Date registrationDate) {
		AppointmentSchedule.Followup.values().collect {

			if(it==AppointmentSchedule.Followup.REGISTERED) {
                Visit visit = new Visit()
                visit.externalId = patientId
                visit.visitDate = registrationDate
                visit.title = it

                visitDao.addVisit(visit)
			} else {
                Appointment appointment = new Appointment()
                appointment.externalId = patientId
                appointment.title = it

                Calendar cal = Calendar.getInstance();
                cal.setTime(DateUtils.truncate(registrationDate, Calendar.DATE));

                cal.add(Calendar.DATE, it.days);
				appointment.setDueDate(cal.getTime());

                appointmentsDao.addAppointment(appointment)

                appointment
			}
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
		return appointmentsDao.findByExternalId(patient.id)
	}
}
