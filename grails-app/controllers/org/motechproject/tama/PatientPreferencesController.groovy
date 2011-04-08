package org.motechproject.tama

class PatientPreferencesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "create", params: params)
    }

    def create = {
        def patientPreferencesInstance = new PatientPreferences()
        //patientPreferencesInstance.properties = params	
        return [patientPreferencesInstance: patientPreferencesInstance]
    }

    def save = {
        def patientPreferencesInstance = new PatientPreferences(params)
        if (patientPreferencesInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), patientPreferencesInstance.id])}"
            redirect(action: "create", id: patientPreferencesInstance.id)
        }
        else {
            render(view: "create", model: [patientPreferencesInstance: patientPreferencesInstance])
        }
    }

    def show = {
        def patientPreferencesInstance = PatientPreferences.get(params.id)
        if (!patientPreferencesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
            redirect(action: "create")
        }
        else {
            [patientPreferencesInstance: patientPreferencesInstance]
        }
    }

    def edit = {
        def patientPreferencesInstance = PatientPreferences.get(params.id)
        if (!patientPreferencesInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [patientPreferencesInstance: patientPreferencesInstance]
        }
    }

    def update = {
        def patientPreferencesInstance = PatientPreferences.get(params.id)
        if (patientPreferencesInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (patientPreferencesInstance.version > version) {
                    
                    patientPreferencesInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'patientPreferences.label', default: 'PatientPreferences')] as Object[], "Another user has updated this PatientPreferences while you were editing")
                    render(view: "edit", model: [patientPreferencesInstance: patientPreferencesInstance])
                    return
                }
            }
            patientPreferencesInstance.properties = params
            if (!patientPreferencesInstance.hasErrors() && patientPreferencesInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), patientPreferencesInstance.id])}"
                redirect(action: "show", id: patientPreferencesInstance.id)
            }
            else {
                render(view: "edit", model: [patientPreferencesInstance: patientPreferencesInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def patientPreferencesInstance = PatientPreferences.get(params.id)
        if (patientPreferencesInstance) {
            try {
                patientPreferencesInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), params.id])}"
            redirect(action: "list")
        }
    }
}
