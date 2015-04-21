package org.motechproject.nms.kilkariobd.client.ex;

import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;

public class CDRFileException extends Exception{

    private FileProcessingStatus errorCode;

    public CDRFileException() {
        super();
    }

    public CDRFileException(FileProcessingStatus errorCode) {
        this.errorCode = errorCode;
    }
}
