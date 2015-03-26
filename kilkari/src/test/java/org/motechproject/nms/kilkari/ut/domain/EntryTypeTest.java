package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.EntryType;
import org.motechproject.nms.util.helper.DataValidationException;

public class EntryTypeTest {

    @Test
    public void shouldCheckValidEntryType() {

        try {
            Assert.assertTrue(EntryType.checkValidEntryType(null));
            Assert.assertTrue(EntryType.checkValidEntryType("9"));
            Assert.assertFalse(EntryType.checkValidEntryType("5"));

        } catch (DataValidationException e) {
        }
    }
}
