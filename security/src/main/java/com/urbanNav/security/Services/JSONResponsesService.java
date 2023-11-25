package com.urbanNav.security.Services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class JSONResponsesService {
    private ObjectMapper objectMapper;
    private String message;
    private Object data;
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public JSONResponsesService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public String writeToJSON(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public String getFinalJSON() {
        String finalJSON = "{";
        if (message != null && message.equals("") == false) {
            finalJSON = finalJSON + "\"message\":\"" + message + "\",";
            message = null;
        }
        if (data != null) {
            if (data instanceof String && data.equals("") == false) {
                finalJSON = finalJSON + "\"data\":\"" + data + "\",";
            } else {
                String dataJSON = this.writeToJSON(data);
                finalJSON = finalJSON + "\"data\":" + dataJSON + ",";
            }
            data = null;
        }
        if (error != null && error.equals("") == false) {
            finalJSON = finalJSON + "\"error\":\"" + error + "\",";
            error = null;
        }
        return finalJSON + "}";
    }
}
