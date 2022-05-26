package com.pje.kelompok4.payload.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseDto<T> {
    private Integer code;
    
    private String status;

    @JsonInclude(Include.NON_NULL)
    private Object messages;

    @JsonInclude(Include.NON_NULL)
    private Object data;

    public ResponseDto(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public ResponseDto(Integer code, String status, String messages) {
        this.code = code;
        this.status = status;
        this.messages = messages;
    }

    public ResponseDto(Integer code, String status, Map<String,String> messages) {

        this.code = code;
        this.status = status;
        this.messages = messages;
    }

    public ResponseDto(Integer code, String status, Iterable<T> data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public ResponseDto(Integer code, String status, String messages, Iterable<T> data) {
        this.code = code;
        this.status = status;
        this.messages = messages;
        this.data = data;
    }

    public ResponseDto(Integer code, String status, T data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessages() {
        return messages;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    
}