package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
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
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
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
    private LocationValidator locationValidator;

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
        State state = locationValidator.stateConsistencyCheck(childMctsCsv.getStateId(), stateCode);

        Long districtCode = ParseDataHelper.parseLong("District Id", childMctsCsv.getDistrictId(), true);
        District district = locationValidator.districtConsistencyCheck(childMctsCsv.getDistrictId(), state, districtCode);

        String talukaCode = ParseDataHelper.parseString("Taluka Id", childMctsCsv.getTalukaId(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(childMctsCsv.getTalukaId(), district, talukaCode);


        Long healthBlockCode = ParseDataHelper.parseLong("Block ID", childMctsCsv.getHealthBlockId(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(childMctsCsv.getHealthBlockId(), childMctsCsv.getTalukaId(), taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.parseLong("Phc Id", childMctsCsv.getPhcId(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(childMctsCsv.getPhcId(), childMctsCsv.getHealthBlockId(), healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered ID", childMctsCsv.getSubCentreId(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(childMctsCsv.getSubCentreId(), childMctsCsv.getPhcId(), healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.parseLong("Village id", childMctsCsv.getVillageId(), false);
        Village village = locationValidator.villageConsistencyCheck(childMctsCsv.getVillageId(), childMctsCsv.getTalukaId(), taluka, villageCode);

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
