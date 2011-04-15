package org.motechproject.tama

import grails.converters.JSON

import org.motechproject.tama.dao.AppointmentDao

class AppointmentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def AppointmentDao tamaAppointmentDao
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
		def patient = patientService.findPatientByClinicPatientId(clinicId, clinicPatientId)
		def appointments = appointmentScheduleService.findByPatient(patient)
        [appointmentInstanceList: appointments, appointmentInstanceTotal: appointments.size(), patientInstance: patient]
    }

	/**
	 * ajax call to save/fix an appointment
	 */
    def saveAppointmentDate = {
        def appointmentInstance = new Appointment()
		bindData(appointmentInstance, params)
		appointmentReminderService.saveAppointmentDate(appointmentInstance.id, appointmentInstance.date)
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
