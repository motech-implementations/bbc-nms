package org.motechproject.nms.mobileacademy.commons;


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
