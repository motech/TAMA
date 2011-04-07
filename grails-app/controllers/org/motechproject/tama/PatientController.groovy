package org.motechproject.tama

import org.motechproject.tama.util.CustomPropertyEditorRegistrar

class PatientController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def patientService
	def doctorService
	
	def CustomPropertyEditorRegistrar customPropertyEditorRegistrar

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		//patientService.serviceMethod()
        //params.max = Math.min(params.max ? params.int('max') : 10, 100)
        //[patientInstanceList: Patient.list(params), patientInstanceTotal: Patient.count()]
        //TODO: add pagination support
    	def patients = patientService.listPatients()
		[patientInstanceList: patients, patientInstanceTotal: patients.size()]
    }

    def create = {
        def patientInstance = new Patient()
		def doctors = doctorService.findDoctorsByClinicId(session.clinicId)
        return [patientInstance: patientInstance, doctors: doctors]
    }

    def save = {
        def patient = new Patient()
		bindData(patient, params)
		patient.clinicId=session.clinicId
		
		//TODO: add error handling
        if (patientService.createPatient(patient)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'patient.label', default: 'Patient'), patient.id])}"
            redirect(action: "show", id: patient.clinicId)
        }
        else {
            render(view: "create", model: [patientInstance: patient])
        }
    }

    def show = {
//        def patientInstance = Patient.get(params.id)
		//FIXME: get the patient instance from the database
		def clinicId = session.clinicId
		def clinicPatientId = params.id
		def patientInstance = patientService.findPatientByClinicPatientId(clinicId, clinicPatientId)
        if (!patientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
            redirect(action: "list")
        }
        else {
			def doctor = doctorService.findDoctorById(patientInstance.doctorId)
            [patientInstance: patientInstance, doctor:doctor]
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
