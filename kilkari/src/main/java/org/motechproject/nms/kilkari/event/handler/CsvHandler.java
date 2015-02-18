package org.motechproject.nms.kilkari.event.handler;

import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsvHandler {

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
		Map parameters = uploadEvent.getParameters();
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
