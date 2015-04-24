
package org.motechproject.nms.kilkari.ut;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.nms.kilkari.builder.LanguageLocationCodeBuilder;
import org.motechproject.nms.kilkari.domain.CsvMctsChild;
import org.motechproject.nms.kilkari.repository.CsvMctsChildDataService;
import org.motechproject.nms.kilkari.repository.CsvMctsMotherDataService;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.kilkari.service.impl.CommonValidatorServiceImpl;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Verify that HelloWorldRecordService present, functional.
 */

public class CommonValidatorServiceTest {

    @Mock
    private StateRecordsDataService stateRecordsDataService;

    @Mock
    private DistrictRecordsDataService districtRecordsDataService;

    @Mock
    private TalukaRecordsDataService talukaRecordsDataService;

    @Mock
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Mock
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Mock
    protected SubscriberService subscriberService;

    @Mock
    protected CsvMctsMotherDataService csvMctsMotherDataService;

    @Mock
    protected CsvMctsMotherService motherMctsCsvService;

    @Mock
    protected CsvMctsChildDataService csvMctsChildDataService;

    @Mock
    protected CsvMctsChildService childMctsCsvService;

    @Mock
    protected SubscriptionService subscriptionService;

    @Mock
    protected LanguageLocationCodeService languageLocationCodeService;

    @Mock
    protected BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    protected ConfigurationService configurationService;

    @Mock
    protected CommonValidatorService commonValidatorService;

    @Mock
    protected LocationService locationService;

    @InjectMocks
    CommonValidatorServiceImpl commonValidatorService1;
    
    @Mock
    private LanguageLocationCodeService llcService;

    @Before
    public void init() {

        commonValidatorService1 = new CommonValidatorServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnNullForValidStatecode() {
        State state = new State();
        Long stateCode = 1L;
        state.setStateCode(stateCode);
        state.setId(1L);
        when(locationService.getStateByCode(1L)).thenReturn(state);
        //when(ParseDataHelper.raiseInvalidDataException("State Code", stateCode.toString())).doNothing();

        State returnedState = null;

        try {
            returnedState = commonValidatorService1.checkAndGetState(1L);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertEquals(state,returnedState);
    }
    
    @Test
    public void shouldThrowErrorWhenLlcCodeIsDeterminedByStateAndDistrictIsNull() {
        initMocks(this);
        LanguageLocationCodeBuilder llcBuilder = new LanguageLocationCodeBuilder();
        //Stub the service methods
        when(llcService.getRecordByLocationCode(1L, 1L)).thenReturn(llcBuilder.buildLLCCode(1L, 1L, null, "circleCode"));

        try {
            commonValidatorService.getLLCCodeByStateDistrict(1L, 1L);
        } catch (Exception err) {
            Assert.assertTrue(err instanceof NmsInternalServerError);
            Assert.assertEquals(((NmsInternalServerError) err).getErrorCode(), ErrorCategoryConstants.INCONSISTENT_DATA);
            Assert.assertEquals(((NmsInternalServerError)err).getMessage() , "languageLocationCode could not be determined for stateCode : "
                    + 1L +" and districtCode " + 1l);
        }
    }

    @Test
    public void shouldReturnNullForInvalidStatecode() {
        State state = null;
        when(locationService.getStateByCode(1L)).thenReturn(null);

        try {
            state = commonValidatorService1.checkAndGetState(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertEquals(null,state);
    }

    @Test
    public void shouldReturnNullForInvalidDistrictCode() {
        District district = null;
        when(locationService.getDistrictByCode(1L, 1L)).thenReturn(null);
        State state = new State();
        state.setId(1L);
        try {
            district = commonValidatorService1.checkAndGetDistrict(state, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,district);
    }

    @Test
    public void shouldReturnNullForInvalidTalukaCode() {
        Taluka taluka = null;
        District district = new District();

        when(locationService.getTalukaByCode(1L, 1L)).thenReturn(null);

        try {
            taluka = commonValidatorService1.checkAndGetTaluka(district, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,taluka);
    }

    @Test
    public void talukaConsistencyCheckWithTalukaNull() {
        Taluka taluka = null;
        District district = new District();

        try {
            taluka = commonValidatorService1.checkAndGetTaluka(district, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,taluka);
    }

    @Test
    public void shouldReturnNullForInvalidHealthBlockCode() {
        HealthBlock healthBlock = null;
        Taluka taluka = new Taluka();

        when(locationService.getHealthBlockByCode(1L, 1L)).thenReturn(null);

        try {
            healthBlock = commonValidatorService1.checkAndGetHealthBlock(1L, taluka, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void healthBlockConsistencyCheckWithNullTaluka() {
        HealthBlock healthBlock = null;

        try {
            healthBlock = commonValidatorService1.checkAndGetHealthBlock(1L, null, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void healthBlockConsistencyCheckWithNullHealthBlockCode() {
        HealthBlock healthBlock = null;
        Taluka taluka = new Taluka();

        try {
            healthBlock = commonValidatorService1.checkAndGetHealthBlock(1L, taluka, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void shouldReturnNullForInvalidphcCode() {
        HealthFacility healthFacility = null;
        HealthBlock healthBlock = new HealthBlock();
        when(locationService.getHealthFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthFacility = commonValidatorService1.checkAndGetPhc(1L, healthBlock, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthFacility);
    }

    @Test
    public void phcConsistencyCheckWithHealthBlockNull() {
        HealthFacility healthFacility = null;
        when(locationService.getHealthFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthFacility = commonValidatorService1.checkAndGetPhc(1L, null, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
        Assert.assertEquals(null,healthFacility);
    }

    @Test
    public void phcConsistencyCheckwithNullphcCode() {
        HealthFacility healthFacility = null;
        HealthBlock healthBlock = new HealthBlock();

        try {
            healthFacility = commonValidatorService1.checkAndGetPhc(1L, healthBlock, null);
        } catch (Exception e) {

            Assert.fail();
        }
        Assert.assertEquals(null,healthFacility);
    }

    @Test
    public void shouldReturnNullForInvalidSubCenterCode() {
        HealthSubFacility healthSubFacility = null;
        HealthFacility healthFacility = new HealthFacility();
        when(locationService.getHealthSubFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthSubFacility = commonValidatorService1.checkAndGetSubCenter(1L, healthFacility, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthSubFacility);
    }

    @Test
    public void subCenterCodeCheckWithNullHealthFacility() {
        HealthSubFacility healthSubFacility = null;
        when(locationService.getHealthSubFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthSubFacility = commonValidatorService1.checkAndGetSubCenter(1L, null, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
        Assert.assertEquals(null,healthSubFacility);
    }

    @Test
    public void subCenterCodeCheckWithNullSubCenterCode() {
        HealthSubFacility healthSubFacility = null;
        HealthFacility healthFacility = new HealthFacility();

        try {
            healthSubFacility = commonValidatorService1.checkAndGetSubCenter(1L, healthFacility, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,healthSubFacility);
    }

    @Test
    public void shouldReturnNullForInvalidVillageCode() {
        Village village = null;
        Taluka taluka = new Taluka();
        when(locationService.getVillageByCode(1L, 1L)).thenReturn(null);

        try {
            village = commonValidatorService1.checkAndGetVillage(1L, taluka, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,village);
    }

    @Test
    public void villageConsistencyCheckWithNullTaluka() {
        Village village = null;
        when(locationService.getVillageByCode(1L, 1L)).thenReturn(null);

        try {
            village = commonValidatorService1.checkAndGetVillage(1L, null, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
        Assert.assertEquals(null,village);
    }

    @Test
    public void villageConsistencyCheckWithNullVillageCode() {
        Village village = null;
        Taluka taluka = new Taluka();


        try {
            village = commonValidatorService1.checkAndGetVillage(1L, taluka, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,village);
    }



    protected CsvMctsChild createChildMcts(CsvMctsChild csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setMotherName("motherName");
        csv.setBirthdate("2001-01-01 00:00:00");
        csv.setEntryType("Death");
        csv.setCreator("Deepak");
        csv.setOwner("Deepak");
        csv.setModifiedBy("Deepak");
        return csv;
    }
}
