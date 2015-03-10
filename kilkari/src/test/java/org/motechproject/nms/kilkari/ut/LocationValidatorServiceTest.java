
package org.motechproject.nms.kilkari.ut;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.event.handler.ChildMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.kilkari.service.impl.LocationValidatorServiceImpl;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Verify that HelloWorldRecordService present, functional.
 */

public class LocationValidatorServiceTest {

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
    protected MotherMctsCsvDataService motherMctsCsvDataService;

    @Mock
    protected MotherMctsCsvService motherMctsCsvService;

    @Mock
    protected ChildMctsCsvDataService childMctsCsvDataService;

    @Mock
    protected ChildMctsCsvService childMctsCsvService;

    @Mock
    protected SubscriptionService subscriptionService;

    @Mock
    protected LanguageLocationCodeService languageLocationCodeService;

    @Mock
    protected BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    protected ConfigurationService configurationService;

    @Mock
    protected LocationValidatorService locationValidatorService;

    @Mock
    protected LocationService locationService;

    @InjectMocks
    LocationValidatorServiceImpl locationValidtorService;

    @Before
    public void init() {

        locationValidtorService = new LocationValidatorServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateCircleAndLLC() {

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(50l);
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ChildMctsCsvUT.csv");

        ChildMctsCsvHandler childMctsCsvHandler = new ChildMctsCsvHandler(childMctsCsvService,
                subscriptionService,
                subscriberService,
                locationValidatorService,
                languageLocationCodeService,
                bulkUploadErrLogService,
                configurationService);

        MotechEvent motechEvent = new MotechEvent("ChildMctsCsv.csv_success", parameters);
        childMctsCsvHandler.childMctsCsvSuccess(motechEvent);


    }

    @Test
    public void shouldReturnNullForInvalidStatecode() {
        State state = null;
        Mockito.when(locationService.getStateByCode(1L)).thenReturn(null);

        try {
            state = locationValidtorService.stateConsistencyCheck(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertEquals(null,state);
    }

    @Test
    public void shouldReturnNullForInvalidDistrictCode() {
        District district = null;
        Mockito.when(locationService.getDistrictByCode(1L, 1L)).thenReturn(null);
        State state = new State();
        state.setId(1L);
        try {
            district = locationValidtorService.districtConsistencyCheck(state, 1L);
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

        Mockito.when(locationService.getTalukaByCode(1L,"abc")).thenReturn(null);

        try {
            taluka = locationValidtorService.talukaConsistencyCheck(district,"abc");
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
            taluka = locationValidtorService.talukaConsistencyCheck(district,null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,taluka);
    }

    @Test
    public void shouldReturnNullForInvalidHealthBlockCode() {
        HealthBlock healthBlock = null;
        Taluka taluka = new Taluka();
        String talukaCode = new String();

        Mockito.when(locationService.getHealthBlockByCode(1L, 1L)).thenReturn(null);

        try {
            healthBlock = locationValidtorService.healthBlockConsistencyCheck(talukaCode, taluka, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void healthBlockConsistencyCheckWithNullTaluka() {
        HealthBlock healthBlock = null;
        String talukaCode = new String();


        try {
            healthBlock = locationValidtorService.healthBlockConsistencyCheck(talukaCode, null, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void healthBlockConsistencyCheckWithNullHealthBlockCode() {
        HealthBlock healthBlock = null;
        String talukaCode = new String();
        Taluka taluka = new Taluka();

        try {
            healthBlock = locationValidtorService.healthBlockConsistencyCheck(talukaCode, taluka, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,healthBlock);
    }

    @Test
    public void shouldReturnNullForInvalidphcCode() {
        HealthFacility healthFacility = null;
        HealthBlock healthBlock = new HealthBlock();
        Mockito.when(locationService.getHealthFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthFacility = locationValidtorService.phcConsistencyCheck(1L, healthBlock, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthFacility);
    }

    @Test
    public void phcConsistencyCheckWithHealthBlockNull() {
        HealthFacility healthFacility = null;
        Mockito.when(locationService.getHealthFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthFacility = locationValidtorService.phcConsistencyCheck(1L, null, 1L);
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
            healthFacility = locationValidtorService.phcConsistencyCheck(1L, healthBlock, null);
        } catch (Exception e) {

            Assert.fail();
        }
        Assert.assertEquals(null,healthFacility);
    }

    @Test
    public void shouldReturnNullForInvalidSubCenterCode() {
        HealthSubFacility healthSubFacility = null;
        HealthFacility healthFacility = new HealthFacility();
        Mockito.when(locationService.getHealthSubFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthSubFacility = locationValidtorService.subCenterCodeCheck(1L, healthFacility, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,healthSubFacility);
    }

    @Test
    public void subCenterCodeCheckWithNullHealthFacility() {
        HealthSubFacility healthSubFacility = null;
        Mockito.when(locationService.getHealthSubFacilityByCode(1L, 1L)).thenReturn(null);

        try {
            healthSubFacility = locationValidtorService.subCenterCodeCheck(1L, null, 1L);
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
            healthSubFacility = locationValidtorService.subCenterCodeCheck(1L, healthFacility, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,healthSubFacility);
    }

    @Test
    public void shouldReturnNullForInvalidVillageCode() {
        Village village = null;
        Taluka taluka = new Taluka();
        String talukaCode = new String();
        Mockito.when(locationService.getVillageByCode(1L, 1L)).thenReturn(null);

        try {
            village = locationValidtorService.villageConsistencyCheck(talukaCode, taluka, 1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        Assert.assertEquals(null,village);
    }

    @Test
    public void villageConsistencyCheckWithNullTaluka() {
        Village village = null;
        String talukaCode = new String();
        Mockito.when(locationService.getVillageByCode(1L, 1L)).thenReturn(null);

        try {
            village = locationValidtorService.villageConsistencyCheck(talukaCode, null, 1L);
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
        String talukaCode = new String();

        try {
            village = locationValidtorService.villageConsistencyCheck(talukaCode, taluka, null);
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(null,village);
    }



    protected ChildMctsCsv createChildMcts(ChildMctsCsv csv) {
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
