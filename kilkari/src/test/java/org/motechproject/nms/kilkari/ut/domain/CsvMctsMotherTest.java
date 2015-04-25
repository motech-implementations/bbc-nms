package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.CsvMctsMother;

public class CsvMctsMotherTest {

    CsvMctsMother csvMctsMother = new CsvMctsMother();

    @Test
    public void shouldSetValuesInCsvMctsMother() {

        csvMctsMother.setName("name");
        Assert.assertEquals("name",csvMctsMother.getName());

        csvMctsMother.setAadharNo("aadharNo");
        Assert.assertEquals("aadharNo",csvMctsMother.getAadharNo());

        csvMctsMother.setAbortion("abortion");
        Assert.assertEquals("abortion",csvMctsMother.getAbortion());

        csvMctsMother.setAge("age");
        Assert.assertEquals("age",csvMctsMother.getAge());

        csvMctsMother.setLmpDate("lmpDate");
        Assert.assertEquals("lmpDate",csvMctsMother.getLmpDate());

        csvMctsMother.setOutcomeNos("outcomeNos");
        Assert.assertEquals("outcomeNos",csvMctsMother.getOutcomeNos());

    }

    @Test
    public void shouldReturnStringOfMctsIdAndStateCode() {

        String string = "Mcts Id[null] stateCode[null]";
        Assert.assertEquals(string, csvMctsMother.toString());
    }
}
