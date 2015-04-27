package org.motechproject.nms.kilkariobd.ut.helper;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.helper.MD5Checksum;

import java.net.URL;

public class MD5ChecksumTest {

    @Test
    public void shouldFindChecksum() {

        String checksum;
        URL url = this.getClass().getClassLoader().getResource("MD5ChecksumTestfile");
        String filePath = url.getPath();

        checksum = MD5Checksum.findChecksum(filePath);

        Assert.assertEquals("22c86cb7ad66bb1ec593b0693cec68c0", checksum);

    }

    @Test
    public void shouldReturnNullIfFileNotFound() {

        String checksum;

        checksum = MD5Checksum.findChecksum("wrongPath");

        Assert.assertNull(checksum);

    }

}
