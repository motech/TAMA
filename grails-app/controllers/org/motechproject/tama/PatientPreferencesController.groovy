package org.motechproject.tama

import org.motechproject.tama.api.model.Preferences

class PatientPreferencesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	PreferencesService preferencesService
	PatientService patientService
	
    def index = {
        redirect(action: "create", params: params)
    }

    def create = {
		// Check that the patient exists (not url hacking)
		def patientInstance = patientService.findPatientByClinicIdPatientId(session.clinicId, params.id)
		
		if (!patientInstance) {
			flash.message = "${message(code: 'patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {

			// Retrieve the patient preferences if any
	        def preferencesInstance = preferencesService.findByClinicPatientId(session.clinicId, params.id)
			if (preferencesInstance == null) {
				preferencesInstance = new Preferences()
				preferencesInstance.clinicId = session.clinicId // Set the clinic Id
				preferencesInstance.clinicPatientId = params.id // Set the patient Id
			}
	        return [patientPreferencesInstance: preferencesInstance]
		}
    }

    def save = {
		// Check that the patient exists (not url hacking)
		def patientInstance = patientService.findPatientByClinicIdPatientId(session.clinicId, params.clinicPatientId)
		
		if (!patientInstance) {
			flash.message = "${message(code: 'patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {
			Preferences preferencesInstance = preferencesService.findByClinicIdPatientId(session.clinicId, params.clinicPatientId)
			if (preferencesInstance == null) {
				preferencesInstance = new Preferences()
				
				// Set provided patient & clinic ids
				preferencesInstance.clinicId = session.clinicId
				preferencesInstance.clinicPatientId = params.clinicPatientId
			}
			
			// Copy over preferences
			preferencesInstance.appointmentReminderEnabled = (params.appointmentReminderEnabled != null ? params.appointmentReminderEnabled : Boolean.FALSE)
			preferencesInstance.bestTimeToCallHour =  new Integer(params.bestTimeToCallHour)
			preferencesInstance.bestTimeToCallMinute =  new Integer(params.bestTimeToCallMinute)
	
			// Check if we are doing a create or an update
			if ( (preferencesInstance.id == null && preferencesService.createPreferences( preferencesInstance )) ||
				 (preferencesInstance.id != null && preferencesService.updatePreferences( preferencesInstance )) ) {
	
	            flash.message = "${message(code: 'preferences.updated')}"
	            redirect(action: "create", id: preferencesService.clinicPatientId)
	        }
	        else {
	            render(view: "create", model: [patientPreferencesInstance: preferencesInstance])
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
