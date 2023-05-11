package jp.co.axa.apidemo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

    private ObjectMapper mapper ;

    public JsonConverter() {
        mapper = new ObjectMapper();
    }

    public String convert(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
