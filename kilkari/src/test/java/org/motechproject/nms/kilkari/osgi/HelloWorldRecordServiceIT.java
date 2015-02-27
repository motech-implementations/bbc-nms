package org.motechproject.nms.kilkari.osgi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.event.handler.MotherMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HelloWorldRecordServiceIT extends BasePaxIT {

    @Inject
    private SubscriberService subscriberService;
    
    @Inject
    private MotherMctsCsvDataService motherMctsCsvDataService;
    
    @Inject
    private MotherMctsCsvService motherMctsCsvService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private LocationValidatorService locationValidatorService;

    @Test
    public void testHelloWorldServicePresent() throws Exception {
        System.out.println("Child Mcts");
        
        Map<String, Object> parameters = new HashMap<>();
        System.out.println("uploadCsv().size()::::::::::::::::" +uploadCsv().size());
        parameters.put("csv-import.created_ids", uploadCsv());
        parameters.put("csv-import.filename", "MotherMctsCsv.csv");
        
        MotherMctsCsvHandler motherMctsCsvHandler = new MotherMctsCsvHandler(motherMctsCsvService, 
                subscriptionService, 
                subscriberService, 
                locationValidatorService, 
                languageLocationCodeService, 
                bulkUploadErrLogService, 
                configurationService);
        
        MotechEvent motechEvent = new MotechEvent("MotherMctsCsv.csv_success", parameters);
        motherMctsCsvHandler.motherMctsCsvSuccess(motechEvent);
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    public List<Long> uploadCsv() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = null;
        int i = 0;
        
        String csvFileToRead = "/home/nms/Deepak/csv/MotherMctsCsv.csv";  
        BufferedReader br = null;  
        String line = "";  
        String [] columns;
        String splitBy = ",";  
        
        try{
            br = new BufferedReader(new FileReader(csvFileToRead));  
            while ((line = br.readLine()) != null) { 
                i = 0;
                try {
                columns = line.split(splitBy);
                
                csv = new MotherMctsCsv();
                csv.setOperation(columns[i++]);
                csv.setStateCode(columns[i++]);
                csv.setDistrictCode(columns[i++]);
                csv.setTalukaCode(columns[i++]);
                csv.setHealthBlockCode(columns[i++]);
                csv.setPhcCode(columns[i++]);
                csv.setSubCentreCode(columns[i++]);
                csv.setVillageId(columns[i++]);
                csv.setName(columns[i++]);
                csv.setWhomPhoneNo(columns[i++]);
                csv.setLmpDate(columns[i++]);
                csv.setAbortion(columns[i++]);
                csv.setOutcomeNos(columns[i++]);
                csv.setAge(columns[i++]);
                csv.setEntryType(columns[i++]);
                csv.setAadharNo(columns[i++]);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
                uploadedIds.add(dbCsv.getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uploadedIds;
    }
}
