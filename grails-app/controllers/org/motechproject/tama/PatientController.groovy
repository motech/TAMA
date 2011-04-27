package org.motechproject.tama

import org.motechproject.tama.util.CustomPropertyEditorRegistrar

import org.motechproject.tama.api.model.Patient
import org.slf4j.LoggerFactory

class PatientController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	static final log = LoggerFactory.getLogger(this)

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
		def doctors = doctorService.findDoctorsByClinicId(session.clinicId)
		[patientInstanceList: patients, patientInstanceTotal: patients.size(), doctors: doctors]
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
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'patient.label', default: 'Patient'), patient.clinicPatientId])}"
            redirect(action: "show", id: patient.clinicPatientId)
        }
        else {
            render(view: "create", model: [patientInstance: patient])
        }
    }

    def show = {
		def clinicId = session.clinicId
		def clinicPatientId = params.id
		def patientInstance = patientService.findPatientByClinicIdPatientId(clinicId, clinicPatientId)
		if (!patientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.clinicPatientId])}"
            redirect(action: "list")
        }
        else {
			def doctor = doctorService.findDoctorById(patientInstance.doctorId)
            [patientInstance: patientInstance, doctor:doctor]
        }
    }

    def edit = {
		def clinicId = session.clinicId
		def clinicPatientId = params.clinicPatientId
		def patientInstance = patientService.findPatientByClinicIdPatientId(clinicId, clinicPatientId)
        if (!patientInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.clinicPatientId])}"
            redirect(action: "list")
        }
        else {
			def doctors = doctorService.findDoctorsByClinicId(session.clinicId)
            return [patientInstance: patientInstance, doctors: doctors]
        }
    }

    def update = {
		def patientInstance = new Patient()
		bindData(patientInstance, params)
		patientInstance.clinicId=session.clinicId
		patientService.updatePatient(patientInstance)
		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'patient.label', default: 'Patient'), patientInstance.clinicPatientId])}"
		redirect(action: "show", id: patientInstance.clinicPatientId)

		//TODO: add error handling
//		def clinicId = session.clinicId
//		def clinicPatientId = params.clinicPatientId
//		def patientInstance = patientService.findPatientByClinicPatientId(clinicId, clinicPatientId)
//        if (patientInstance) {
//            if (params.version) {
//                def version = params.version.toLong()
//                if (patientInstance.version > version) {
//                    
//                    patientInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'patient.label', default: 'Patient')] as Object[], "Another user has updated this Patient while you were editing")
//                    render(view: "edit", model: [patientInstance: patientInstance])
//                    return
//                }
//            }
//            patientInstance.properties = params
//            if (!patientInstance.hasErrors() && patientInstance.save(flush: true)) {
//                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'patient.label', default: 'Patient'), patientInstance.id])}"
//                redirect(action: "show", id: patientInstance.id)
//            }
//            else {
//                render(view: "edit", model: [patientInstance: patientInstance])
//            }
//        }
//        else {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
//            redirect(action: "list")
//        }
    }

    def delete = {
		def clinicId = session.clinicId
		def clinicPatientId = params.clinicPatientId
		def patientInstance = patientService.findPatientByClinicIdPatientId(clinicId, clinicPatientId)
        if (patientInstance) {
			patientService.deletePatient(patientInstance)
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'patient.label', default: 'Patient'), params.clinicPatientId])}"
			redirect(action: "list")

			//TODO: add error handling
//            try {
//                patientInstance.delete(flush: true)
//                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
//                redirect(action: "list")
//            }
//            catch (org.springframework.dao.DataIntegrityViolationException e) {
//                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'patient.label', default: 'Patient'), params.id])}"
//                redirect(action: "show", id: params.id)
//            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient'), params.clinicPatientId])}"
            redirect(action: "list")
        }
    }
}