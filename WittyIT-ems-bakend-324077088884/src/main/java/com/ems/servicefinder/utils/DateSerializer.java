package com.ems.servicefinder.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes dates. java.util.Date into a String date formatted as MM/dd/yyyy 
 *
 */
public class DateSerializer extends JsonSerializer<Date> {
	  
  	/* (non-Javadoc)
  	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
  	 */
  	@Override
	  public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
	    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    jgen.writeString(formatter.format(value));
	  }
}
