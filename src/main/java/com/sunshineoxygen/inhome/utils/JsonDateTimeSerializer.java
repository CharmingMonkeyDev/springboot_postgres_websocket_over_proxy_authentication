package com.sunshineoxygen.inhome.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JsonDateTimeSerializer extends JsonSerializer<LocalTime> {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void serialize(final LocalTime time, final JsonGenerator gen, final SerializerProvider provider) throws IOException, JsonProcessingException {
        String dateString = format.format(time);
        gen.writeString(dateString);
    }

}
