package org.motechproject.tama

enum Gender {
	MALE("Male"), FEMALE("Female"), HIJIRA("Hijira")
	
	final String value
	
	Gender(String value) {this.value=value}
	@Override
	String toString(){value}
	String getKey(){name()}
}
