package com.pedidosya.test.bootstrap;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.Collections;

@Data
public class ApiException extends RuntimeException {

    private String message;
    private int status;

    public ApiException(int code, String message) {
        this.message = message;
        this.status = code;

    }

    public String getJsonMessage() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.toJson(Collections.singletonMap("Message", this.message));
    }

}
