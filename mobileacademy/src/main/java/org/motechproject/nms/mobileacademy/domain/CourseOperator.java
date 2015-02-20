package org.motechproject.nms.mobileacademy.domain;


/**
 * Created by nitin on 2/13/15.
 */
public enum CourseOperator {
    ADD,
    MOD,
    DEL;

    public static CourseOperator getFor(String type){
        for(CourseOperator operation: CourseOperator.values()){
            if(operation.equals(type)){
                return operation;
            }
        }
        return null;
    }
}
