package org.motechproject.nms.mobilekunji.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobilekunji.domain.StateCapMapping;
import org.motechproject.nms.mobilekunji.domain.StateCapMappingCsv;
import org.motechproject.nms.mobilekunji.event.mapper.EntityMapper;
import org.motechproject.nms.mobilekunji.repository.StateCapMappingCsvRecordDataService;
import org.motechproject.nms.mobilekunji.repository.StateCapMappingRecordDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StateCapMappingCsvHandler {


    @Autowired
    private StateCapMappingCsvRecordDataService stateCapMappingCsv;

    @Autowired
    private StateCapMappingRecordDataService stateCapMapping;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;


    private static Logger logger = LoggerFactory.getLogger(StateCapMappingCsvHandler.class);


    @MotechListener(subjects = {"mds.crud.mobilekunji.StateCapMappingCsv.csv-import.success" })
    public void stateCapMappingCsvSuccess(MotechEvent event) {

        String errorFileName = "StateCapMappingCsv_" + new Date().toString();
        try {
            Map<String, Object> params = event.getParameters();;
            ProcessResult result = processCsvRecords(params, errorFileName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, result.getSuccessCount(), result.getFailureCount());
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = {"mds.crud.mobilekunji.StateCapMappingCsv.csv-import.failed" })
    public void stateCapMappingCsvFailure(MotechEvent event) {

        stateCapMappingCsv.deleteAll();

        logger.info("Upload data successfully deleted");
    }

    private ProcessResult processCsvRecords(Map<String, Object> params, String errorFileName) {
        String entityName = (String)params.get("entityName");

       List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
       List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            for(Long id : updatedIds) {
                StateCapMappingCsv record =  stateCapMappingCsv.findById(id);
                BulkUploadErrRecordDetails error = EntityMapper.validateLanguageLocationCodeCsv(record);
                if(error == null) {
                    StateCapMapping newRecord = EntityMapper.mapLanguageLocationCodeFrom(record);
                    stateCapMapping.update(newRecord);
                    stateCapMappingCsv.delete(record);
                    successCount++;
                }else {
                    logErrorRecord(error, errorFileName, record.getIndex());
                }
            }

            for(Long id : createdIds) {
                StateCapMappingCsv record =  stateCapMappingCsv.findById(id);
                BulkUploadErrRecordDetails error = EntityMapper.validateStateCapMappingCsv(record);
                if(error == null) {
                    StateCapMapping newRecord = EntityMapper.mapStateCapMappingFrom(record);
                    stateCapMapping.create(newRecord);
                    stateCapMappingCsv.delete(record);
                    successCount++;
                }else {
                    logErrorRecord(error, errorFileName, record.getIndex());
                }
            }

        private void logErrorRecord(BulkUploadErrRecordDetails error, String errorFileName, Long index) {
            error.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(index.toString()));
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, error);
            failCount++;
        }


    }




}
