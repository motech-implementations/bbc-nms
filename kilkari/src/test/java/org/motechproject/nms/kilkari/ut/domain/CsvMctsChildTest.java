package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.CsvMctsChild;

public class CsvMctsChildTest {

    CsvMctsChild csvMctsChild = new CsvMctsChild();

    @Test
    public void shouldSetValuesInCsvMctsChild() {

        csvMctsChild.setMotherName("motherName");
        Assert.assertEquals("motherName",csvMctsChild.getMotherName());

        csvMctsChild.setMotherId("motherId");
        Assert.assertEquals("motherId",csvMctsChild.getMotherId());

        csvMctsChild.setBirthdate("birthDate");
        Assert.assertEquals("birthDate",csvMctsChild.getBirthdate());

    }

    @Test
    public void shouldReturnStringOfMctsIdAndStateCode() {

        String actualString = csvMctsChild.toString();
        Assert.assertEquals("Mcts Id[null] stateCode[null]",actualString);
    }


}
