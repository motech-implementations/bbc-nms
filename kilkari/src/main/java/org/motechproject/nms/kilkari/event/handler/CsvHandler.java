package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.util.Constants;
import org.motechproject.nms.kilkari.util.HelperMethod;
import org.motechproject.nms.kilkari.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsvHandler {

	private static final String CSV_IMPORT_PREFIX = "csv-import.";
	public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";

	@Autowired
	private MotherMctsCsvService motherMctsCsvService;

	@Autowired
	private ChildMctsCsvService childMctsCsvService;

	@MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.success")
	public void motherMctsCsvSuccess(MotechEvent uploadEvent){
		System.out.println(uploadEvent.getSubject());
		System.out.println("success");
		System.out.println(motherMctsCsvService.getClass().getName());
		System.out.println(childMctsCsvService.getClass().getName());
		Message message = null;
		Map<String, Object> parameters = uploadEvent.getParameters();

		List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);

		for (Long id : uploadedIDs) {

			MotherMctsCsv motherMctsCsv = motherMctsCsvService.findRecordById(id);

			if(motherMctsCsv!=null ){

				message = mandetoryCheck(motherMctsCsv);

				if(Constants.SUCCESS.equals(message.getStatus())){

					message = sizeCheck(motherMctsCsv);

					if(Constants.SUCCESS.equals(message.getStatus())){
						Object object = conversionCheck(motherMctsCsv);
						if(object instanceof Message){
							message = (Message) object;
						} 
						else{
							Subscriber subscriber = (Subscriber) object;
							//message = consistencyCheck(subscriber);
						}

					}
				}

			}
		}
	}

	private Object conversionCheck(MotherMctsCsv motherMctsCsv) {
		Subscriber subscriber = new Subscriber();
		subscriber.setMsisdn(motherMctsCsv.getIdNo());
		subscriber.setMotherMctsId(motherMctsCsv.getWhomPhoneNo());
		subscriber.setAge(parseInt(motherMctsCsv.getAge()));
		Long stateId = parseLong(motherMctsCsv.getStateId());
		if(stateId!=null){
			subscriber.setStateId(stateId);
		}else{
			return new Message(Constants.FAILURE, "Invalid Data", "Wrong stateId Data");
		}
		Long districtId = parseLong(motherMctsCsv.getDistrictId());
		if(stateId!=null){
			subscriber.setDistrictId(districtId);
		}else{
			return new Message(Constants.FAILURE, "Invalid Data", "Wrong Mcts Data");
		}
		subscriber.setTalukaId(motherMctsCsv.getTalukaId());
		subscriber.setHealthBlockId(parseLong(motherMctsCsv.getHealthBlockId()));
		subscriber.setPhcId(parseLong(motherMctsCsv.getPhcId()));
		subscriber.setSubCentreId(parseLong(motherMctsCsv.getSubCentreId()));
		subscriber.setVillageId(parseLong(motherMctsCsv.getVillageId()));
		Integer aadharNo = parseInt(motherMctsCsv.getAadharNo());
		if(aadharNo!=null){
			subscriber.setAadharNumber(aadharNo);
		}else{
			return new Message(Constants.FAILURE, "Invalid Data", "Wrong Mcts Data");
		}
		return subscriber;
	}

	private Integer parseInt(String value){

		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Long parseLong(String value){
		
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	private Message sizeCheck(MotherMctsCsv motherMctsCsv) {

		Message message;
		String mctsId = motherMctsCsv.getIdNo();
		if(mctsId.length()>18){
			message = new Message(Constants.FAILURE, "Invalid Data", "Wrong Mcts Data");
			return message;
		}

		String msidn = motherMctsCsv.getWhomPhoneNo();
		if(msidn.length()<10){
			message = new Message(Constants.FAILURE, "Invalid Data", "Wrong Msidn Data");
			return message;
		}

		String aadharNo = motherMctsCsv.getAadharNo();
		if(aadharNo.length()>12){
			message = new Message(Constants.FAILURE, "Invalid Data", "Wrong AadharNum Data");
			return message;
		}

		return new Message(Constants.SUCCESS, Constants.SUCCESS, Constants.SUCCESS);
	}

	private Message mandetoryCheck(MotherMctsCsv motherMctsCsv) {

		Message message = new Message(Constants.FAILURE,"Mandatory Parameter Missing", "");

		if(HelperMethod.isNullOrEmpty(motherMctsCsv.getIdNo())){
			message.setMsgDesc("IdNo is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getWhomPhoneNo())){
			message.setMsgDesc("WhomPhoneNo is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getLmpDate())){
			message.setMsgDesc("LmpDate is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getAbortion())){
			message.setMsgDesc("Abortion is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getEntryType())){
			message.setMsgDesc("EntryType is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getStateId())){
			message.setMsgDesc("StateId is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getDistrictId())){
			message.setMsgDesc("DistrictId is missing");
		}

		else if(HelperMethod.isNullOrEmpty(motherMctsCsv.getAadharNo())){
			message.setMsgDesc("AadharNo is missing");
		}

		else{
			message = new Message(Constants.SUCCESS, Constants.SUCCESS, Constants.SUCCESS);
		}
		return message;
	}


	@MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.failure")
	public void motherMctsCsvFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}

	@MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.success")
	public void childMctsCsvSuccess(MotechEvent uploadEvent){
		System.out.println("Inside Success");
	}

	@MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.failure")
	public void childMctsCsvFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.Subscriber.csv-import.success")
	public void subscriberSuccess(MotechEvent uploadEvent){
		System.out.println("Inside Success");
	}
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.Subscriber.csv-import.failure")
	public void subscriberFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}
	
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.Subscription.csv-import.success")
	public void subscriptionSuccess(MotechEvent uploadEvent){
		System.out.println("Inside Success");
	}
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.Subscription.csv-import.failure")
	public void subscriptionFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.SubscriptionPack.csv-import.success")
	public void subscriptionPackSuccess(MotechEvent uploadEvent){
		System.out.println("Inside Success");
	}
	
	@MotechListener(subjects = "mds.crud.kilkarimodule.SubscriptionPack.csv-import.failure")
	public void subscriptionPackFailure(MotechEvent uploadEvent){
		System.out.println("Inside Failure");
	}
	
}
