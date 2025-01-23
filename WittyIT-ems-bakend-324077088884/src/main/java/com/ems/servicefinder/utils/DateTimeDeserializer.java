package com.ems.servicefinder.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserializes dates. String dates formatted as MM/dd/yyyy into org.joda.time.DateTime
 *
 */
public class DateTimeDeserializer extends JsonDeserializer<DateTime> {

    /* (non-Javadoc)
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public DateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        return parse(parser.getText(), "MM/dd/yyyy", false);
    }
    
    /**
     * Parses the.
     *
     * @param dateRep the date rep
     * @param pattern the pattern
     * @param lenient the lenient
     * @return the date time
     */
    public DateTime parse(String dateRep, String pattern, boolean lenient) {
    	DateTime date = null;
		if (dateRep != null  && !dateRep.isEmpty() ) {
			DateFormat df = null;
			if (pattern != null) {
				df = new SimpleDateFormat(pattern);
			} else {
				df = DateFormat.getInstance();
			}
			try {
				df.setLenient(lenient);
				date = new DateTime(df.parse(dateRep).getTime());
			} catch (Exception e) { 
				throw new RuntimeException(e);
			}
		}
		return date;
	} 

}
