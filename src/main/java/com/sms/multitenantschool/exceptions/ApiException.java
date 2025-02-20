package com.sms.multitenantschool.exceptions;


public class ApiException extends RuntimeException{

    public ApiException(String message){
        super(message);
    }
    public ApiException(){

    }
}
