package org.motechproject.nms.mobilekunji.ut.domain;

import org.junit.Test;
import org.motechproject.nms.mobilekunji.domain.ContentType;
import static org.junit.Assert.assertEquals;
/**
 * This Class provides the UT cases for Content Upload.
 */
public class ContentTypeTest {

    @Test
    public void TestValidValue() {
        ContentType contentType;
        contentType = ContentType.of("ABC");
        assertEquals(null, contentType);
    }

    @Test
    public void TestValidValue2() {
        ContentType contentType;
        contentType = ContentType.of("CONTENT");
        assertEquals(ContentType.CONTENT,contentType);
    }
}
