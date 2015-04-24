package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.CsvMctsMother;

public class CsvMctsMotherTest {

    CsvMctsMother motherMctsCsv = new CsvMctsMother();

    @Test
    public void shouldSetValuesInMotherMctsCsv() {

        motherMctsCsv.setName("name");
        Assert.assertEquals("name",motherMctsCsv.getName());

        motherMctsCsv.setAadharNo("aadharNo");
        Assert.assertEquals("aadharNo",motherMctsCsv.getAadharNo());

        motherMctsCsv.setAbortion("abortion");
        Assert.assertEquals("abortion",motherMctsCsv.getAbortion());

        motherMctsCsv.setAge("age");
        Assert.assertEquals("age",motherMctsCsv.getAge());

        motherMctsCsv.setLmpDate("lmpDate");
        Assert.assertEquals("lmpDate",motherMctsCsv.getLmpDate());

        motherMctsCsv.setOutcomeNos("outcomeNos");
        Assert.assertEquals("outcomeNos",motherMctsCsv.getOutcomeNos());

    }

    @Test
    public void shouldReturnStringOfMctsIdAndStateCode() {

        String string = "Mcts Id[null] stateCode[null]";
        Assert.assertEquals(string, motherMctsCsv.toString());
    }
}
