package org.motechproject.tama

import grails.plugins.selenium.*
import org.junit.*
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*

@Mixin(SeleniumAware)
class HomePageTests extends GroovyTestCase {
    @Before void setUp() {
    }

    @After void tearDown() {
        super.tearDown()
    }

    @Test void something() {
		selenium.open "/TAMA"
		//FIXME: check some real content when we have our own UI
		assertTrue selenium.isTextPresent("Grails")
    }
}
