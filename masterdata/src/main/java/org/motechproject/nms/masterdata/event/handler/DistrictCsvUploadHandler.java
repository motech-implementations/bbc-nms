package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abhishek on 29/1/15.
 */

@Component
public class DistrictCsvUploadHandler {

    @Autowired
    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);


    @MotechListener(subjects = {"mds.crud.masterdata.DistrictCsv.csv-import.success"})
    public void districtCsvSuccess(MotechEvent event) {

        List<DistrictCsv> districtCsvList = districtCsvRecordsDataService.retrieveAll();

        for (Iterator<DistrictCsv> itr = districtCsvList.iterator(); itr.hasNext(); ) {

            DistrictCsv districtCsvData = itr.next();

            if (districtCsvData.getStateCode() != null) {

                Long stateCode = getLongId(districtCsvData.getStateCode());

                if (null == stateCode) {

                    logger.error("StateId is invalid " + districtCsvData.getStateCode());

                } else {

                    State stateData = stateRecordsDataService.findRecordByStateCode(stateCode);

                    if (stateData != null) {

                        Long districtCode = getLongId(districtCsvData.getDistrictCode());

                        if (null != districtCode) {

                            setDistrictList(districtCsvData);
                        }
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

    private void setDistrictList(DistrictCsv districtCsvData){

        District dist = districtRecordsDataService.findRecordByDistrictCodeAndStateCode(
                Long.parseLong(districtCsvData.getDistrictCode()),
                Long.parseLong(districtCsvData.getStateCode()));

        if(dist != null) {

            dist.setName(districtCsvData.getName());

            districtRecordsDataService.update(dist);

        }else {
            District updateDist = getDistrictData(districtCsvData);

            districtRecordsDataService.create(updateDist);
        }
    }

    private District getDistrictData(DistrictCsv districtCsvData)
    {
        District data= new District(districtCsvData.getName(),getLongId(districtCsvData.getDistrictCode()), null,getLongId(districtCsvData.getStateCode()));

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
