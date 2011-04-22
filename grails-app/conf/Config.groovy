// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']


// TAMA configurations
tama.n = 7
tama.m = 2
tama.appointmentreminder.event.schedule.subject = "org.motechproject.server.appointmentreminder.schedule-reminder-call"
tama.appointmentreminder.event.unschedule.subject = "org.motechproject.server.appointmentreminder.unschedule-reminder-call"
tama.appointmentreminder.event.type.schedule.patientid.key = "PatientID"
tama.appointmentreminder.event.type.schedule.appointmentid.key = "AppointmentID"
tama.appointmentreminder.event.type.schedule.jobid.key = "JobID"
tama.outbox.event.schedule.ivrcall = "org.motechproject.server.outbox.schedule-ivr-call"
tama.outbox.event.unschedule.ivrcall = "org.motechproject.server.outbox.unschedule-ivr-call"
tama.outbox.event.ivrcall.besttimetocallhour.key = "org.motechproject.server.outbox.event.ivrcall.besttimetocallhour.key"
tama.outbox.event.ivrcall.besttimetocallminute.key = "org.motechproject.server.outbox.event.ivrcall.besttimetocallminute.key"

// Until spring security is integrated we need to assume that the clinic id 
// ie When you log in you are associated with a clinic and therefore that will 
//    be placed in your session.
tama.defaultClinicId = "1234"

motech.scheduler.event.type.schedule.jobid.key = "JobID"

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
	ci {
		grails.serverURL = "http://localhost:9090/${appName}"
	}
}

// log4j configuration
log4j = {
	
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d %-5p [%c] %m%n')
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate',
		   'org.motechproject',
		   'grails.app'

    warn   'org.mortbay.log',
    	   'org.motechproject',
		   'grails.app'
		   
	debug  'org.motechproject',
	       'grails.app'
}

grails.server.port.http = 8081
