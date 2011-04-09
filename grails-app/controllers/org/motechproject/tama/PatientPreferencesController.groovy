package org.motechproject.tama

import org.codehaus.groovy.grails.commons.ConfigurationHolder;


class PatientPreferencesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	PatientPreferencesService patientPreferencesService
	PatientService patientService
	def config = ConfigurationHolder.config
	
    def index = {
        redirect(action: "create", params: params)
    }

    def create = {
		// Check that the patient exists (not url hacking)
		def patientInstance = patientService.findPatientByClinicPatientId(session.clinicId, params.id)
		
		if (!patientInstance) {
			flash.message = "${message(code: 'default.patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {

			// Retrieve the patient preferences if any
	        def patientPreferencesInstance = patientPreferencesService.findByClinicPatientId(session.clinicId, params.id)
			if (patientPreferencesInstance == null) {
				patientPreferencesInstance = new PatientPreferences()
				patientPreferencesInstance.clinicId = session.clinicId // Set the clinic Id
				patientPreferencesInstance.clinicPatientId = params.id // Set the patient Id
			}
	        return [patientPreferencesInstance: patientPreferencesInstance]
		}
    }

    def save = {
		// Check that the patient exists (not url hacking)
		def patientInstance = patientService.findPatientByClinicPatientId(session.clinicId, params.clinicPatientId)
		
		if (!patientInstance) {
			flash.message = "${message(code: 'default.patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {
			PatientPreferences patientPreferencesInstance = patientPreferencesService.findByClinicPatientId(session.clinicId, params.id)
			if (patientPreferencesInstance == null) {
				patientPreferencesInstance = new PatientPreferences()
				
				// Set provided patient & clinic ids
				patientPreferencesInstance.clinicId = session.clinicId
				patientPreferencesInstance.clinicPatientId = params.clinicPatientId
			}
			
			// Copy over preferences
			patientPreferencesInstance.appointmentReminderEnabled = params.appointmentReminderEnabled
			patientPreferencesInstance.bestTimeToCallHour =  new Integer(params.bestTimeToCallHour) 
			patientPreferencesInstance.bestTimeToCallMinute =  new Integer(params.bestTimeToCallMinute) 
	
			// Check if we are doing a create or an update
			if ( (patientPreferencesInstance.id == null && patientPreferencesService.createPatientPreferences( patientPreferencesInstance )) ||
				 (patientPreferencesInstance.id != null && patientPreferencesService.updatePatientPreferences( patientPreferencesInstance )) ) {
	
				// TODO GLUE CODE for integrating with modules (Appointment Reminder, Pill Reminder, etc...)	
				
	            flash.message = "${message(code: 'default.created.message', args: [message(code: 'patientPreferences.label', default: 'PatientPreferences'), patientPreferencesInstance.id])}"
	            redirect(action: "create", id: patientPreferencesInstance.clinicPatientId)
	        }
	        else {
	            render(view: "create", model: [patientPreferencesInstance: patientPreferencesInstance])
	        }
		}
    }

/*    def show = {
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
*/
}
