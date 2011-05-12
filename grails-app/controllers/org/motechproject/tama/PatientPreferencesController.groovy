package org.motechproject.tama

import org.motechproject.tama.api.model.Preferences

class PatientPreferencesController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	PatientService patientService
    PreferencesService preferencesService
	
    def index = {
        redirect(action: "create", params: params)
    }

    def create = {
		// Check that the patient exists (not url hacking)
		def patient = patientService.findPatientByClinicIdPatientId(session.clinicId, params.id)
		
		if (!patient) {
			flash.message = "${message(code: 'patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {

			// Retrieve the patient preferences if any
	        def preferencesInstance = patient.preferences
			if (preferencesInstance == null) {
				preferencesInstance = new Preferences()
                patient.preferences = preferencesInstance

                patientService.updatePatient(patient)
			}
	        return [patientPreferencesInstance: preferencesInstance, patient: patient]
		}
    }

    def save = {
		// Check that the patient exists (not url hacking)
		def patient = patientService.findPatientByClinicIdPatientId(session.clinicId, params.clinicPatientId)
		
		if (!patient) {
			flash.message = "${message(code: 'patient.notfound', args: [], defaultMessage: 'Patient Not Found')}"
			redirect(controller: "patient", action: "index" )
		} else {

            def preferencesInstance = patient.preferences
			if (preferencesInstance == null) {
				preferencesInstance = new Preferences()
				
                patient.preferences = preferencesInstance
			}
			
			// Copy over preferences
			preferencesInstance.appointmentReminderEnabled = (params.appointmentReminderEnabled != null ? params.appointmentReminderEnabled : Boolean.FALSE)
			preferencesInstance.bestTimeToCallHour =  new Integer(params.bestTimeToCallHour)
			preferencesInstance.bestTimeToCallMinute =  new Integer(params.bestTimeToCallMinute)
			preferencesInstance.language = Preferences.Language.valueOf(params.language)
			def lang = params.language

            preferencesService.preferencesUpdated(patient)

			// Check if we are doing a create or an update
			if (patientService.updatePatient(patient)) {
	
	            flash.message = "${message(code: 'preferences.updated', args: [], defaultMessage: 'Preferences Updated')}"
	            redirect(action: "create", id: patient.id)
	        }
	        else {
	            render(view: "create", model: [patientPreferencesInstance: preferencesInstance, patient: patient])
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
