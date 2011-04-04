package org.motechproject.tama

class PatientController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        //[patientInstanceList: Patient.list(params), patientInstanceTotal: Patient.count()]
        //FIXME
		[patientInstanceList: [new Patient(clinicPatientId:'1001', dateOfBirth:new Date())], patientInstanceTotal: 1]
    }

    def create = {
        def patientInstance = new Patient()
        patientInstance.properties = params
        return [patientInstance: patientInstance]
    }

    def save = {
        def patientInstance = new Patient(params)
        if (patientInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'patient.label', default: 'Patient'), patientInstance.id])}"
            redirect(action: "show", id: patientInstance.id)
        }
        else {
            render(view: "create", model: [patientInstance: patientInstance])
        }
    }

    def show = {
//        def patientInstance = Patient.get(params.id)
		//FIXME: get the patient instance from the database
		def patientInstance = new Patient(clinicPatientId:'1001', dateOfBirth:new Date())
        if (!patientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
            redirect(action: "list")
        }
        else {
            [patientInstance: patientInstance]
        }
    }

    def edit = {
        def patientInstance = Patient.get(params.id)
        if (!patientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [patientInstance: patientInstance]
        }
    }

    def update = {
        def patientInstance = Patient.get(params.id)
        if (patientInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (patientInstance.version > version) {
                    
                    patientInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'patient.label', default: 'Patient')] as Object[], "Another user has updated this Patient while you were editing")
                    render(view: "edit", model: [patientInstance: patientInstance])
                    return
                }
            }
            patientInstance.properties = params
            if (!patientInstance.hasErrors() && patientInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'patient.label', default: 'Patient'), patientInstance.id])}"
                redirect(action: "show", id: patientInstance.id)
            }
            else {
                render(view: "edit", model: [patientInstance: patientInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def patientInstance = Patient.get(params.id)
        if (patientInstance) {
            try {
                patientInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
            redirect(action: "list")
        }
    }
}
