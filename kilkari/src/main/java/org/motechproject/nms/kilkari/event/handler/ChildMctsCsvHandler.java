package org.motechproject.nms.kilkari.event.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.util.HelperMethods;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.util.service.impl.BulkUploadErrLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChildMctsCsvHandler {

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_UPDATED_IDS = CSV_IMPORT_PREFIX + "updated_ids";
    public static final String CSV_IMPORT_CREATED_COUNT = CSV_IMPORT_PREFIX + "created_count";
    public static final String CSV_IMPORT_UPDATED_COUNT = CSV_IMPORT_PREFIX + "updated_count";
    public static final String CSV_IMPORT_TOTAL_COUNT = CSV_IMPORT_PREFIX + "total_count";
    public static final String CSV_IMPORT_FAILURE_MSG = CSV_IMPORT_PREFIX + "failure_message";
    public static final String CSV_IMPORT_FAILURE_STACKTRACE = CSV_IMPORT_PREFIX + "failure_stacktrace";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";


    @Autowired
    private ChildMctsCsvService childMctsCsvService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.success")
    public void motherMctsCsvSuccess(MotechEvent uploadEvent){

        System.out.println(uploadEvent.getSubject());
        System.out.println("success");
        System.out.println(childMctsCsvService.getClass().getName());

        Map<String, Object> parameters = uploadEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);
        
        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary summary = new CsvProcessingSummary();
        BulkUploadError errorDetails = new BulkUploadError();
        
        ChildMctsCsv childMctsCsv = null;
        
        for (Long id : uploadedIDs) {

            try {
                childMctsCsv = childMctsCsvService.findRecordById(id);
                if(childMctsCsv!=null ){
                    Subscriber subscriber = childMctsToSubscriberMapper(childMctsCsv);
                    summary.incrementSuccessCount();
                }
                else {
                    summary.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                }
            }catch(DataValidationException dve) {
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                summary.incrementFailureCount();

            }catch(Exception e){
                summary.incrementFailureCount();
            }
        }
        
        childMctsCsvService.deleteAll();
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("username", csvFileName, logFile, summary);
    }

    private Subscriber childMctsToSubscriberMapper(ChildMctsCsv childMctsCsv) throws DataValidationException  {

        Subscriber childSubscriber = new Subscriber();

        Long stateCode = ParseDataHelper.parseLong("State Id", childMctsCsv.getStateId(),  true);
        State state = locationService.getStateByCode(stateCode);
        if(state == null){
            HelperMethods.raiseInvalidException("State Id", childMctsCsv.getStateId());
        }

        Long districtCode = ParseDataHelper.parseLong("District Id", childMctsCsv.getDistrictId(), true);
        District district = locationService.getDistrictByCode(state.getId(), districtCode);
        if(district == null){
            HelperMethods.raiseInvalidException( "District Id", childMctsCsv.getDistrictId());
        }

        String talukaCode = ParseDataHelper.parseString("Taluka Id", childMctsCsv.getTalukaId(), false);
        Taluka taluka = null;
        if (talukaCode!=null) {
            taluka = locationService.getTalukaByCode(district.getId(),talukaCode);
            if(taluka == null){
                HelperMethods.raiseInvalidException("Taluka Id", childMctsCsv.getTalukaId());
            }
        }


        Long healthBlockCode = ParseDataHelper.parseLong("Block ID", childMctsCsv.getHealthBlockId(), false);
        HealthBlock healthBlock = null;
        if (healthBlockCode!=null) {
            if (taluka != null) {
            healthBlock = locationService.getHealthBlockByCode(taluka.getId(),healthBlockCode);
            if(healthBlock == null){
                HelperMethods.raiseInvalidException("Block ID", childMctsCsv.getHealthBlockId());
            }
            }else {
                HelperMethods.raiseInvalidException("Taluka ID", childMctsCsv.getTalukaId());
            }
        }

        Long phcCode = ParseDataHelper.parseLong("Phc Id", childMctsCsv.getPhcId(), false);
        HealthFacility healthFacility = null;
        if (phcCode!=null) {
            healthFacility = locationService.getHealthFacilityByCode(healthBlock.getId(), phcCode);
            if(healthFacility == null){
                HelperMethods.raiseInvalidException("Phc Id", childMctsCsv.getPhcId());
            }
        }

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered ID", childMctsCsv.getSubCentreId(), false);
        HealthSubFacility healthSubFacility = null;
        if (subCenterCode!=null) {
            healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), subCenterCode);
            if(healthSubFacility == null){
                HelperMethods.raiseInvalidException("Sub centered ID", childMctsCsv.getSubCentreId());
            }
        }

        Long villageCode = ParseDataHelper.parseLong("Village id", childMctsCsv.getVillageId(), false);
        Village village = null;
        if (villageCode!=null) {
            village = locationService.getVillageByCode(taluka.getId(),villageCode);
            if(village == null){
                HelperMethods.raiseInvalidException("Village id", childMctsCsv.getVillageId());
            }
        }

        childSubscriber.setState(state);
        childSubscriber.setDistrictId(district);
        childSubscriber.setTalukaId(taluka);
        childSubscriber.setHealthBlockId(healthBlock);
        childSubscriber.setPhcId(healthFacility);
        childSubscriber.setSubCentreId(healthSubFacility);
        childSubscriber.setVillageId(village);

        childSubscriber.setMsisdn(ParseDataHelper.parseString(childMctsCsv.getWhomPhoneNo(), "Whom Phone Num", true));
        childSubscriber.setMotherMctsId(ParseDataHelper.parseString(childMctsCsv.getIdNo(), "idNo", true));
        childSubscriber.setChildDeath("Death".equalsIgnoreCase(ParseDataHelper.parseString(childMctsCsv.getEntryType(), "Entry Type", true)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        childSubscriber.setName(ParseDataHelper.parseString(childMctsCsv.getMotherName(), "Mother Name", false));

        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());

        childSubscriber.setDob(ParseDataHelper.parseDate(childMctsCsv.getBirthdate(), "Birth Date", true));
        childMctsCsv.getMotherName();
        childMctsCsv.getMotherId();
        childMctsCsv.getOperation();
        return childSubscriber;
    }

    @MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.failure")
    public void childMctsCsvFailure(MotechEvent uploadEvent){
        
        System.out.println("Inside Failure");
        
        Map<String, Object> params = uploadEvent.getParameters();
        List<Long> createdIds = (List<Long>)params.get("csv-import.created_ids");
        List<Long> updatedIds = (List<Long>)params.get("csv-import.updated_ids");
        
        for(Long id : createdIds) {
            ChildMctsCsv childMctsCsv= childMctsCsvService.findById(id);
            childMctsCsvService.delete(childMctsCsv);
        }
        
        for(Long id : updatedIds) {
            ChildMctsCsv childMctsCsv= childMctsCsvService.findById(id);
            childMctsCsvService.delete(childMctsCsv);
        }
        
    }

}
