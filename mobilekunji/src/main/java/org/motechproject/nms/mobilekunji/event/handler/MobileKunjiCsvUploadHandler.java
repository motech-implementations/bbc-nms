package org.motechproject.nms.mobilekunji.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobilekunji.domain.MobileKunjiContentUpload;
import org.motechproject.nms.mobilekunji.domain.MobileKunjiContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.MobileKunjiContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.repository.MobileKunjiContentUploadRecordDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class MobileKunjiCsvUploadHandler {


    @Autowired
    MobileKunjiContentUploadCsvRecordDataService mobileKunjiContentUploadCsv;

    @Autowired
    MobileKunjiContentUploadRecordDataService mobileKunjiContentUpload;


    private static Logger logger = LoggerFactory.getLogger(MobileKunjiCsvUploadHandler.class);


    @MotechListener(subjects = {"mds.crud.mobilekunji.MobileKunjiContentUploadCsv.csv-import.success"})
    public void mobileKunjiContentUploadCsvSuccess(MotechEvent event) {

        List<MobileKunjiContentUploadCsv> mobileKunjiContentUploadCsvList = mobileKunjiContentUploadCsv.retrieveAll();

        for (Iterator<MobileKunjiContentUploadCsv> itr = mobileKunjiContentUploadCsvList.iterator(); itr.hasNext(); ) {

            MobileKunjiContentUploadCsv csvRecord = itr.next();

            if (true == csvRecord.validateParameters()) {


                MobileKunjiContentUpload kunjiData = mobileKunjiContentUpload.findRecordByContentId(Integer.parseInt(csvRecord.getContentId()));

                if (null != kunjiData) {


                } else {


                }

            }


        }
    }

    @MotechListener(subjects = {"mds.crud.mobilekunji.MobileKunjiContentUploadCsv.csv-import.failed"})
    public void mobileKunjiContentUploadCsvFailure(MotechEvent event) {

            mobileKunjiContentUploadCsv.deleteAll();

        logger.info("Upload data successfully deleted");
    }




}
