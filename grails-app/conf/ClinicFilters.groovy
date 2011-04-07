class ClinicFilters{

	def filters = {
		injectClinic(controller:'*', action:'*'){
			before={
				//FIXME: remove this when have login UI
				session.clinicId=BootStrap.CLINIC_ID
				return true
			}	
		}	
	}	
}
