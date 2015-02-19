package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mds.ex.MdsException;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.motechproject.nms.masterdata.event.mapper.EntityMapper;

import java.lang.Long;
import java.lang.NumberFormatException;
import java.util.*;

@Component
public class MasterDataCsvUploadHandler {

    public static Integer successCount = 0;
    public static Integer failCount = 0;



    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private StateCsvRecordsDataService stateCsvRecordsDataService;

    @Autowired
    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Autowired
    private CircleService circleService;

    @Autowired
    private CircleCsvService circleCsvService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorCsvService operatorCsvService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(MasterDataCsvUploadHandler.class);


    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.success"})
    public void stateCsvSuccess(MotechEvent event) {

        List<StateCsv> stateCsvList = stateCsvRecordsDataService.retrieveAll();

        for (Iterator<StateCsv> itr = stateCsvList.iterator(); itr.hasNext(); ) {

            StateCsv csvRecord = itr.next();

            if (csvRecord.getStateId() != null) {

                Long stateId = getLongId(csvRecord.getStateId());

                if (null == stateId) {

                    logger.error("stateId is invalid " + csvRecord.getStateId());

                } else {

                    State stateExistData = stateRecordsDataService.findRecordById(Long.parseLong(csvRecord.getStateId()));

                    if (null != stateExistData) {

                        setStateName(stateExistData, csvRecord);

                        stateRecordsDataService.update(stateExistData);

                    } else {

                        State stateNewData = getStateData(csvRecord);

                        stateRecordsDataService.create(stateNewData);
                    }
                }
            }
        }

        logger.info("State permanent data is successfully inserted.");

        stateCsvRecordsDataService.deleteAll();

        logger.info("State successfully deleted");
    }

    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.failed"})
    public void stateCsvFailed(MotechEvent event) {

        stateCsvRecordsDataService.deleteAll();

        logger.info("State successfully deleted");
    }

    @MotechListener(subjects = {"mds.crud.masterdata.DistrictCsv.csv-import.success"})
    public void districtCsvSuccess(MotechEvent event){

        List<DistrictCsv> districtCsvList = districtCsvRecordsDataService.retrieveAll();

        for(Iterator<DistrictCsv> itr = districtCsvList.iterator(); itr.hasNext(); ){

            DistrictCsv districtCsvData = itr.next();

            if(districtCsvData.getStateId() != null){

                long stateId = getLongId(districtCsvData.getStateId());

                if(stateId == 0){

                    logger.error("StateId is invalid " + districtCsvData.getStateId());

                } else {

                    State stateData = stateRecordsDataService.findRecordById(stateId);

                    if(stateData != null){

                        long districtId = getLongId(districtCsvData.getDistrictId());

                        if(districtId != 0){

                          setDistrictList(stateData,districtCsvData);

                        }
                        stateRecordsDataService.update(stateData);
                    }
                }
            }
        }

        districtCsvRecordsDataService.deleteAll();

        logger.info("District successfully deleted from temporary tables");
    }

    @MotechListener(subjects = {"mds.crud.masterdata.DistrictCsv.csv-import.failed"})
    public void districtCsvFailed(){

        districtCsvRecordsDataService.deleteAll();

        logger.info("District successfully deleted from temporary tables");
    }



    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.success")
    public void languageLocationCodeCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "LanguageLocationCodeCsv" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                LanguageLocationCodeCsv record =  languageLocationCodeServiceCsv.findById(id);
                BulkUploadErrRecordDetails error = EntityMapper.validateLanguageLocationCodeCsv(record);
                if (error == null) {
                    LanguageLocationCode newRecord = EntityMapper.mapLanguageLocationCodeFrom(record);
                    languageLocationCodeService.create(newRecord);
                    languageLocationCodeServiceCsv.delete(record);
                    successCount++;
                }else {
                    error.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_UNSUCCESSFUL.format(record.getIndex().toString()));
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, error);
                    failCount++;
                }
            }
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }


    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.failure")
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                LanguageLocationCodeCsv record =  languageLocationCodeServiceCsv.findById(id);
                languageLocationCodeServiceCsv.delete(record);
            }

        }catch (MdsException ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.success")
    public void circleCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "CircleCsv" + new Date().toString();

        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                CircleCsv record =  circleCsvService.findById(id);
                BulkUploadErrRecordDetails error = EntityMapper.validateCircleCsv(record);
                if (error == null) {
                    Circle newRecord = EntityMapper.mapCircleFrom(record);
                    circleService.create(newRecord);
                    circleCsvService.delete(record);
                    successCount++;
                }else {
                    error.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_UNSUCCESSFUL.format(record.getIndex().toString()));
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, error);
                    failCount++;
                }
            }
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                CircleCsv record =  circleCsvService.findById(id);
                circleCsvService.delete(record);
            }
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.success")
    public void operatorCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "CircleCsv" + new Date().toString();

        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                OperatorCsv record =  operatorCsvService.findById(id);
                BulkUploadErrRecordDetails error = EntityMapper.validateOperatorCsv(record);
                if (error == null) {
                    Operator newRecord = EntityMapper.mapOperatorFrom(record);
                    operatorService.create(newRecord);
                    operatorCsvService.delete(record);
                    successCount++;
                }else {
                    error.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_UNSUCCESSFUL.format(record.getIndex().toString()));
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, error);
                    failCount++;
                }
            }
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.failure")
    public void operatorCsvFailure(MotechEvent motechEvent) {
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : createdIds) {
                OperatorCsv record =  operatorCsvService.findById(id);
                operatorCsvService.delete(record);
            }
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.success")
    public void contentUploadKKCsvSuccess(MotechEvent motechEvent) {

        System.out.println("import successfull");

    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void contentUploadKKCsvFailure(MotechEvent motechEvent) {

        System.out.println("import failed");

    }

    private void setDistrictList(State stateData,DistrictCsv districtCsvData){

        List<District> list = stateData.getDistrict();

        boolean flag = false;

        if(list != null && !list.isEmpty()) {

            for (Iterator<District> itr = list.iterator(); itr.hasNext(); ) {

                District districtData = itr.next();

                if(districtData.getDistrictId() == getLongId(districtCsvData.getDistrictId().trim()))
                {
                setDistrictName(districtData,districtCsvData);
                flag =true;
                break;
                }
            }
        }

        if(!flag){

            list.add(getDistrictData(districtCsvData));

            stateData.setDistrict(list);
        }
    }

    private void setStateName(State data,StateCsv csvData){

        data.setName(csvData.getName());
    }

    private void setDistrictName(District districtData,DistrictCsv districtCsvData){

        districtData.setName(districtCsvData.getName());
    }

    private State getStateData(StateCsv csvData)
    {
        State data= new State(csvData.getName(),Long.parseLong(csvData.getStateId()),null);

        return data;
    }

    private District getDistrictData(DistrictCsv districtCsvData)
    {
        District data= new District(districtCsvData.getName(),getLongId(districtCsvData.getDistrictId()), null);

        return data;
    }

    private Long getLongId(String id){
        try {

            Long longId = Long.parseLong(id.trim());

            return longId;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
