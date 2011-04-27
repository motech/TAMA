package org.motechproject.tama

import org.motechproject.appointments.api.dao.AppointmentsDAO
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient

class AppointmentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def AppointmentsDAO appointmentsDao
    def PatientDAO patientDao
	def PatientService patientService
	def AppointmentScheduleService appointmentScheduleService
	def AppointmentReminderService appointmentReminderService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [appointmentInstanceList: Appointment.list(params), appointmentInstanceTotal: Appointment.count()]
		def clinicId = session.clinicId
		def clinicPatientId = params.id
		def patient = patientService.findPatientByClinicIdPatientId(clinicId, clinicPatientId)
		def appointments = appointmentScheduleService.findByPatient(patient)
        [appointmentInstanceList: appointments, appointmentInstanceTotal: appointments.size(), patientInstance: patient]
    }

	/**
	 * ajax call to save/fix an appointment
	 */
    def saveAppointmentDate = {
		appointmentReminderService.saveAppointmentScheduledDate(params.id, params.date)

        // If reminders are enabled I need to update them
        Appointment appointment = appointmentDao.get(params.id)
        Patient patient = patientDao.get(appointment.externalId)

        if (patient.preferences.appointmentReminderEnabled) {
            appointmentReminderService.createRemindersForAppointment(appointment)
        }

		//error handling
		render Boolean.TRUE;
    }
	
	/**
	 * ajax call to delete a fixed appointment
	 */
    def deleteAppointmentDate = {
    	appointmentReminderService.deleteAppointmentDate(params.id)
    	render Boolean.TRUE;
    }
}
