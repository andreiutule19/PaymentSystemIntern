package com.example.paymentsystem.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND )
@JsonIgnoreProperties(value={"stackTrace","suppressed","cause","localizedMessage"}) //scoatem field-urile mostenite care nu ne intereseaza
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private HttpStatus status;
    public ResourceNotFoundException(){

    }
    @Builder
    public ResourceNotFoundException(String resourceName,String fieldName,Object fieldValue,HttpStatus status){
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        this.resourceName=resourceName;
        this.status=status;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
