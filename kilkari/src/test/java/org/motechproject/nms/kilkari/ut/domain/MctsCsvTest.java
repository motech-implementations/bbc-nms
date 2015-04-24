package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.CsvMcts;

public class MctsCsvTest {

    @Test
    public void shouldSetValuesInMctsCsv() {

        CsvMcts mctsCsv = new CsvMcts();

        mctsCsv.setDistrictCode("districtCode");
        Assert.assertEquals("districtCode",mctsCsv.getDistrictCode());

        mctsCsv.setEntryType("entryType");
        Assert.assertEquals("entryType",mctsCsv.getEntryType());

        mctsCsv.setHealthBlockCode("healthBlockCode");
        Assert.assertEquals("healthBlockCode",mctsCsv.getHealthBlockCode());

        mctsCsv.setIdNo("idNo");
        Assert.assertEquals("idNo",mctsCsv.getIdNo());

        mctsCsv.setPhcCode("phcCode");
        Assert.assertEquals("phcCode",mctsCsv.getPhcCode());

        mctsCsv.setStateCode("stateCode");
        Assert.assertEquals("stateCode",mctsCsv.getStateCode());

        mctsCsv.setSubCentreCode("subCentreCode");
        Assert.assertEquals("subCentreCode",mctsCsv.getSubCentreCode());

        mctsCsv.setTalukaCode("talukaCode");
        Assert.assertEquals("talukaCode",mctsCsv.getTalukaCode());

        mctsCsv.setVillageCode("villageCode");
        Assert.assertEquals("villageCode",mctsCsv.getVillageCode());

        mctsCsv.setWhomPhoneNo("whomPhoneNo");
        Assert.assertEquals("whomPhoneNo",mctsCsv.getWhomPhoneNo());

    }
}
