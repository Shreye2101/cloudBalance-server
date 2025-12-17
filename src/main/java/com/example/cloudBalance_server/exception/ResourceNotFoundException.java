package com.example.cloudBalance_server.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException( String msg){
         super(msg);
    }
}
