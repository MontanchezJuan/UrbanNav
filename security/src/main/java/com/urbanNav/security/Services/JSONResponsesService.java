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
    private String data;
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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

    public String writeToJSON(Object object) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(object);
    }

    public String getFinalJSON() {
        String finalJSON = "{";
        if (message != null && message.equals("") == false) {
            finalJSON = finalJSON + "\"message\":\"" + message + "\",";
            message = "";
        }
        if (data != null && data.equals("") == false) {
            finalJSON = finalJSON + "\"data\":\"" + data + "\",";
            data = "";
        }
        if (error != null && error.equals("") == false) {
            finalJSON = finalJSON + "\"error\":\"" + error + "\",";
            error = "";
        }
        return finalJSON + "}";
    }
}
