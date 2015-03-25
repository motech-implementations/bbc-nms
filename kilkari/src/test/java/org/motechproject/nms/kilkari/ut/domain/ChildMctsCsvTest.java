package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

public class ChildMctsCsvTest {

    ChildMctsCsv childMctsCsv = new ChildMctsCsv();

    @Test
    public void shouldSetValuesInChildMctsCsv() {

        childMctsCsv.setMotherName("motherName");
        Assert.assertEquals("motherName",childMctsCsv.getMotherName());

        childMctsCsv.setMotherId("motherId");
        Assert.assertEquals("motherId",childMctsCsv.getMotherId());

        childMctsCsv.setBirthdate("birthDate");
        Assert.assertEquals("birthDate",childMctsCsv.getBirthdate());

    }

    @Test
    public void shouldReturnStringOfMctsIdAndStateCode() {

        String actualString = childMctsCsv.toString();
        Assert.assertEquals("Mcts Id[null] stateCode[null]",actualString);
    }


}
