package org.motechproject.nms.kilkari.event.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
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
import org.motechproject.nms.util.service.impl.BulkUploadErrLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotherMctsCsvHandler {

	private static final String CSV_IMPORT_PREFIX = "csv-import.";
	public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
	public static final String CSV_IMPORT_UPDATED_IDS = CSV_IMPORT_PREFIX + "updated_ids";
	public static final String CSV_IMPORT_CREATED_COUNT = CSV_IMPORT_PREFIX + "created_count";
	public static final String CSV_IMPORT_UPDATED_COUNT = CSV_IMPORT_PREFIX + "updated_count";
	public static final String CSV_IMPORT_TOTAL_COUNT = CSV_IMPORT_PREFIX + "total_count";
	public static final String CSV_IMPORT_FAILURE_MSG = CSV_IMPORT_PREFIX + "failure_message";
	public static final String CSV_IMPORT_FAILURE_STACKTRACE = CSV_IMPORT_PREFIX + "failure_stacktrace";

	@Autowired
	private MotherMctsCsvService motherMctsCsvService;

	@Autowired
	private ChildMctsCsvService childMctsCsvService;

	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private LocationService locationService;
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.success")
	public void motherMctsCsvSuccess(MotechEvent uploadEvent){
		
		Map<String, Object> parameters = uploadEvent.getParameters();
		String logFile = BulkUploadError.createBulkUploadErrLogFileName((String) parameters.get(""));
		BulkUploadErrLogService logger = new BulkUploadErrLogServiceImpl();
		BulkUploadError errorDetails = new BulkUploadError();
		logger.writeBulkUploadErrLog("logger.txt", errorDetails);

		System.out.println(uploadEvent.getSubject());
		System.out.println("success");
		System.out.println(motherMctsCsvService.getClass().getName());
		System.out.println(childMctsCsvService.getClass().getName());
		

		List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
		int failedRecordCount = 0;
		int successRecordCount = 0;
		MotherMctsCsv motherMctsCsv = null;
		
		for (Long id : uploadedIDs) {
			try {
				motherMctsCsv = motherMctsCsvService.findRecordById(id);
				if(motherMctsCsv!=null ){
					Subscriber subscriber = motherMctsToSubscriberMapper(motherMctsCsv);
					successRecordCount++;
				}
				else {
					failedRecordCount++;
					errorDetails.setRecordDetails(id.toString());
					errorDetails.setErrorCategory("Record_Not_Found");
					errorDetails.setErrorDescription("Record not in database");
				}
			}catch(DataValidationException dve) {
				errorDetails.setRecordDetails(motherMctsCsv.toString());
				errorDetails.setErrorCategory(dve.getErrorCode());
				errorDetails.setErrorDescription(dve.getErroneousField());
				logger.writeBulkUploadErrLog(logFile, errorDetails);
				failedRecordCount++;
				
			}catch(Exception e){
				failedRecordCount++;
			}
		}
		CsvProcessingSummary summary = new CsvProcessingSummary(successRecordCount, failedRecordCount);
		logger.writeBulkUploadProcessingSummary("username", "csv file name", logFile, summary);
	}

	private Subscriber motherMctsToSubscriberMapper(MotherMctsCsv motherMctsCsv) throws DataValidationException  {
		
		Subscriber subscriber = new Subscriber();
		
		Long stateCode = ParseDataHelper.parseLong(motherMctsCsv.getStateId(), "State Id", true);
		State state = locationService.getStateByCode(stateCode);
		
		Long districtCode = ParseDataHelper.parseLong(motherMctsCsv.getDistrictId(), "District Id", true);
		District district = locationService.getDistrictByCode(state.getId(), districtCode);
		
		String talukaCode = ParseDataHelper.parseString(motherMctsCsv.getTalukaId(), "Taluka Id", false);
		Taluka taluka = locationService.getTalukaByCode(district.getId(), talukaCode);
		
		Long healthBlockCode = ParseDataHelper.parseLong(motherMctsCsv.getHealthBlockId(), "Block ID", false);
		HealthBlock healthBlock = locationService.getHealthBlockByCode(taluka.getId(), healthBlockCode);
		
		Long phcCode = ParseDataHelper.parseLong(motherMctsCsv.getPhcId(), "Phc Id", false);
		HealthFacility healthFacility  = locationService.getHealthFacilityByCode(healthBlock.getId(), phcCode);
		
		Long subCenterCode = ParseDataHelper.parseLong(motherMctsCsv.getSubCentreId(), "Sub centered ID", false);
		HealthSubFacility healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), subCenterCode);
		
		Long villageCode = ParseDataHelper.parseLong(motherMctsCsv.getVillageId(), "Village id", false);
		Village village = locationService.getVillageByCode(taluka.getId(), villageCode);

		subscriber.setState(state);
		subscriber.setDistrictId(district);
		subscriber.setTalukaId(taluka);
		subscriber.setHealthBlockId(healthBlock);
		subscriber.setPhcId(healthFacility);
		subscriber.setSubCentreId(healthSubFacility);
		subscriber.setVillageId(village);
		
		subscriber.setMsisdn(ParseDataHelper.parseString(motherMctsCsv.getIdNo(), "idNo", true));
		subscriber.setMotherMctsId(ParseDataHelper.parseString(motherMctsCsv.getWhomPhoneNo(), "Whom Phone Num", true));
		subscriber.setAge(ParseDataHelper.parseInt(motherMctsCsv.getAge(), "Age", false));
		subscriber.setAadharNumber(ParseDataHelper.parseString(motherMctsCsv.getAadharNo(), "AAdhar Num", true));

		subscriber.setLmp(ParseDataHelper.parseDate(motherMctsCsv.getLmpDate(), "Lmp Date", true));
		subscriber.setStillBirth("0".equalsIgnoreCase(motherMctsCsv.getOutcomeNos()));
		subscriber.setAbortion(!"NONE".equalsIgnoreCase(ParseDataHelper.parseString(motherMctsCsv.getAbortion(), "Abortion", true)));
		subscriber.setMotherDeath("Death".equalsIgnoreCase(ParseDataHelper.parseString(motherMctsCsv.getEntryType(), "Entry Type", true)));
		subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
		return subscriber;
	}

	@MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.failure")
	public void motherMctsCsvFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}

}
