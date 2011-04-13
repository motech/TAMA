package org.motechproject.tama.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.groovy.grails.web.binding.StructuredDateEditor;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
	  public void registerCustomEditors(PropertyEditorRegistry registry) {
	      registry.registerCustomEditor(Date.class, new StructuredDateEditor(new SimpleDateFormat("dd-MMM-yyyy"), true));
	  }
	} 
