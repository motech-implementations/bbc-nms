package org.motechproject.nms.kilkariobd.client.ex;

import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;

public class CDRFileProcessingFailedException extends Exception{

    private FileProcessingStatus errorCode;

    public CDRFileProcessingFailedException() {
        super();
    }

    public CDRFileProcessingFailedException(FileProcessingStatus errorCode) {
        this.errorCode = errorCode;
    }
}
