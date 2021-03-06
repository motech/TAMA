package org.motechproject.tama

import org.motechproject.appointments.api.AppointmentService
import org.motechproject.appointments.api.ReminderService
import org.motechproject.appointments.api.VisitService
import org.motechproject.appointments.api.model.Appointment
import org.motechproject.tama.api.dao.PatientDAO
import org.motechproject.tama.api.model.Patient

class AppointmentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def AppointmentService appointmentService
    def VisitService visitService
    def ReminderService reminderService

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
        def reminders = reminderService.findByExternalId(patient.id)

        // I need to give more thought to a datastructure here.  Bascially the UI wants to display a window, but if
        // there are no reminders then there really isn't a window.  I don't want to put this logic in the gsp, but
        // for now that is what I'm going to do.  (I'm also going to loop inside a loop.)
        [appointmentInstanceList: appointments, appointmentInstanceTotal: appointments.size(),
                patientInstance: patient, remindersList: reminders]
    }

    def list2 = {

		def clinicId = session.clinicId
		def clinicPatientId = params.id
		def patient = patientService.findPatientByClinicIdPatientId(clinicId, clinicPatientId)

		def appointments = appointmentScheduleService.findByPatient(patient)
        def reminders = reminderService.findByExternalId(patient.id)
        def visits = visitService.findByExternalId(patient.id)

        log.debug("Loaded " + reminders.size() + " reminders.")
        log.debug("Loaded " + visits.size() + " visits.")

        // Find the registration visit, it's the one without an apt id
        def visit
        visits.each { if (null == it.appointmentId) { visit = it } }

        // I need to give more thought to a datastructure here.  Bascially the UI wants to display a window, but if
        // there are no reminders then there really isn't a window.  I don't want to put this logic in the gsp, but
        // for now that is what I'm going to do.  (I'm also going to double loop inside a loop.)  I probably
        // should create a container that holds the appointment, reminders and visits in an array for the template
        [appointmentInstanceList: appointments, appointmentInstanceTotal: appointments.size(),
                patientInstance: patient, remindersList: reminders, visitList: visits, visit: visit]
    }

	/**
	 * ajax call to save/fix an appointment
	 */
    def saveAppointmentDate = {
        Appointment appointment = appointmentService.getAppointment(params.id)
        appointment.scheduledDate = new Date(params.date)
        Patient patient = patientService.getPatient(appointment.externalId)

        if (patient.preferences && patient.preferences.appointmentReminderEnabled) {
            appointmentReminderService.createRemindersForAppointment(appointment)
        }

        appointmentService.updateAppointment(appointment)

		//error handling
		render Boolean.TRUE;
    }
	
	/**
	 * ajax call to delete a fixed appointment
	 */
    def deleteAppointmentDate = {
        Appointment appointment = appointmentService.getAppointment(params.id)
        appointment.scheduledDate = null
        Patient patient = patientService.getPatient(appointment.externalId)

        if (patient.preferences && patient.preferences.appointmentReminderEnabled) {
            appointmentReminderService.createRemindersForAppointment(appointment)
        }

        appointmentService.updateAppointment(appointment)

    	render Boolean.TRUE;
    }
}
