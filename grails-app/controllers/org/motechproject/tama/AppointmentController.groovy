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

    def create = {
        def appointmentInstance = new Appointment()
        appointmentInstance.patientId = params.id
		tamaAppointmentDao.add(appointmentInstance)
        return [appointmentInstance: appointmentInstance]
    }

	/**
	 * ajax call
	 */
    def saveAppointmentDate = {
        def appointmentInstance = new Appointment()
		bindData(appointmentInstance, params)
		appointmentReminderService.saveAppointmentDate(appointmentInstance.id, appointmentInstance.date)
		//error handling
		render Boolean.TRUE;
    }
	
    def show = {
        def appointmentInstance = Appointment.get(params.id)
        if (!appointmentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "list")
        }
        else {
            [appointmentInstance: appointmentInstance]
        }
    }

    def edit = {
        def appointmentInstance = Appointment.get(params.id)
        if (!appointmentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [appointmentInstance: appointmentInstance]
        }
    }

    def update = {
        def appointmentInstance = Appointment.get(params.id)
        if (appointmentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (appointmentInstance.version > version) {
                    
                    appointmentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'appointment.label', default: 'Appointment')] as Object[], "Another user has updated this Appointment while you were editing")
                    render(view: "edit", model: [appointmentInstance: appointmentInstance])
                    return
                }
            }
            appointmentInstance.properties = params
            if (!appointmentInstance.hasErrors() && appointmentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'appointment.label', default: 'Appointment'), appointmentInstance.id])}"
                redirect(action: "show", id: appointmentInstance.id)
            }
            else {
                render(view: "edit", model: [appointmentInstance: appointmentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def appointmentInstance = Appointment.get(params.id)
        if (appointmentInstance) {
            try {
                appointmentInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'appointment.label', default: 'Appointment'), params.id])}"
            redirect(action: "list")
        }
    }
}
