package com.ems.servicefinder.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserializes dates. String dates formatted as MM/dd/yyyy into java.util.Date
 *
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    /* (non-Javadoc)
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        return parse(parser.getText(), "MM/dd/yyyy", false);
    }
    
    /**
     * Parses the string date.
     *
     * @param dateRep the date as string
     * @param pattern the pattern
     * @param lenient the lenient
     * @return the date
     */
    public Date parse(String dateRep, String pattern, boolean lenient) {
		Date date = null;
		if (dateRep != null) {
			DateFormat df = null;
			if (pattern != null) {
				df = new SimpleDateFormat(pattern);
			} else {
				df = DateFormat.getInstance();
			}
			try {
				df.setLenient(lenient);
				date = new Date(df.parse(dateRep).getTime());
			} catch (Exception e) { 
				throw new RuntimeException(e);
			}
		}
		return date;
	}
}
