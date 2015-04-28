package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;

public class FileProcessingStatusTest {

    @Test
    public void shouldReturnFileProcessingStatusByValue() {

        Assert.assertEquals(FileProcessingStatus.FILE_CHECKSUM_ERROR, FileProcessingStatus.findByValue(8002));
        Assert.assertNull(FileProcessingStatus.findByValue(0));
    }
}
