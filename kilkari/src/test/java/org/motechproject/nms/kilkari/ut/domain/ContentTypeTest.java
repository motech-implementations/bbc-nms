package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.ContentType;

public class ContentTypeTest {

    @Test
    public void shouldFindByName() {

        ContentType contentType = ContentType.CONTENT;

        Assert.assertEquals(null, ContentType.findByName(null));
        Assert.assertEquals(null, ContentType.findByName("invalidValue"));
        Assert.assertEquals(contentType, ContentType.findByName("Content"));
    }

}
