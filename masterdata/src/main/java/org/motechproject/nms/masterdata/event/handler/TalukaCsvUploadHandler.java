package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.TalukaCsv;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abhishek on 29/1/15.
 */
@Component
public class TalukaCsvUploadHandler {

    @Autowired
    private TalukaCsvRecordsDataService talukaCsvRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    private static Logger logger = LoggerFactory.getLogger(TalukaCsvUploadHandler.class);

    @MotechListener(subjects = {"mds.crud.masterdata.TalukaCsv.csv-import.success"})
    public void talukaCsvSuccess(MotechEvent event) {

        List<TalukaCsv> talukaCsvList = talukaCsvRecordsDataService.retrieveAll();

        for (TalukaCsv talukaCsvData:  talukaCsvList) {

            if (talukaCsvData.getStateCode() != null) {

                Long stateCode = getLongId(talukaCsvData.getStateCode());

                Long districtCode = getLongId(talukaCsvData.getDistrictCode());

                Long talukaCode = getLongId(talukaCsvData.getTalukaCode());

                if (null == stateCode || districtCode == null || talukaCode == null) {

                    logger.error("State/District/Taluka Code is Missing");

                } else {

                    State stateData = stateRecordsDataService.findRecordByStateCode(stateCode);

                    District districtData = districtRecordsDataService.findRecordByDistrictCodeAndStateCode(districtCode,stateCode);

                    if (stateData != null && districtData != null) {

                            setTalukaList(talukaCsvData);
                    }
                }
            }
        }

        talukaCsvRecordsDataService.deleteAll();

        logger.info("Taluka successfully deleted from temporary tables");
    }

    private void setTalukaList(TalukaCsv talukaCsvData){

        Taluka existTalukaData = talukaRecordsDataService.findRecordByState_District_TalukaCode(
                Long.parseLong(talukaCsvData.getStateCode()),
                Long.parseLong(talukaCsvData.getDistrictCode()),
                Long.parseLong(talukaCsvData.getTalukaCode()));

        if(existTalukaData != null) {

            existTalukaData.setName(talukaCsvData.getName());

            talukaRecordsDataService.update(existTalukaData);

        }else {

            Taluka talukaData = getTalukaData(talukaCsvData);

            talukaRecordsDataService.create(talukaData);
        }
    }

    @MotechListener(subjects = {"mds.crud.masterdata.TalukaCsv.csv-import.failed"})
    public void talukaCsvFailed(MotechEvent event){

        talukaCsvRecordsDataService.deleteAll();

        logger.info("Taluka successfully deleted from temporary tables");
    }

    private Taluka getTalukaData(TalukaCsv talukaCsvData)
    {
        Taluka data= new Taluka(talukaCsvData.getName(),talukaCsvData.getTalukaCode(),
                null,null,Long.parseLong(talukaCsvData.getDistrictCode()),Long.parseLong(talukaCsvData.getStateCode()));

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
