package org.motechproject.nms.mobileacademy.web;


public class InternalException extends Exception {
    
    private String message;

    public InternalException(String message) {
        super();
        this.message = message;
    } 
    
    public String getMessage(){
        return message;
    }

}
