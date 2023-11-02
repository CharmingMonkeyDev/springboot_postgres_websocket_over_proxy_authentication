package com.sunshineoxygen.inhome.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class JsonDateTimeDeserializer extends JsonDeserializer<LocalTime> {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public LocalTime deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            try {
                Date date = format.parse(jp.getText().toString());
                Instant instantSt = Instant.ofEpochMilli(date.getTime());
                LocalTime time = LocalDateTime.ofInstant(instantSt, ZoneId.systemDefault()).toLocalTime();
                return time;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
