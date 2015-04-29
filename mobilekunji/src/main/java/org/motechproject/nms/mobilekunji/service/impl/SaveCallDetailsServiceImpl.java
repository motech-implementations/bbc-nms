package org.motechproject.nms.mobilekunji.service.impl;


import org.joda.time.DateTime;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.mobilekunji.domain.CallDetail;
import org.motechproject.nms.mobilekunji.domain.CardDetail;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.service.CallDetailService;
import org.motechproject.nms.mobilekunji.service.FlwDetailService;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides the implementation of SaveCallDetailsService
 */

@Service("saveCallDetailsService")
public class SaveCallDetailsServiceImpl implements SaveCallDetailsService {

    private CallDetailService callDetailService;

    private FlwDetailService flwDetailService;

    private FrontLineWorkerService frontLineWorkerService;

    private Logger logger = LoggerFactory.getLogger(SaveCallDetailsServiceImpl.class);

    @Autowired
    public SaveCallDetailsServiceImpl(CallDetailService callDetailService, FlwDetailService flwDetailService, FrontLineWorkerService frontLineWorkerService) {
        this.callDetailService = callDetailService;
        this.flwDetailService = flwDetailService;
        this.frontLineWorkerService = frontLineWorkerService;
    }

    /**
     * Saves Call details of the user
     *
     * @param saveCallDetailApiRequest
     * @throws DataValidationException
     */
    @Override
    public void saveCallDetails(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        logger.info("Save CallDetail Entered successfully.");


        FlwDetail flwDetail = updateFlwDetail(saveCallDetailApiRequest);

        setCallDetail(null != flwDetail ? flwDetail.getNmsFlwId() : null, saveCallDetailApiRequest);

        logger.info("Save CallDetail executed successfully.");
    }

    /**
     * Saves Call details of the user
     *
     * @param saveCallDetailApiRequest
     * @param nmsId
     */
    private void setCallDetail(Long nmsId, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        CallDetail callDetail = new CallDetail(saveCallDetailApiRequest.getCallId(), saveCallDetailApiRequest.getCallingNumber(),
                saveCallDetailApiRequest.getOperator(), saveCallDetailApiRequest.getCallStatus(),
                saveCallDetailApiRequest.getCallDisconnectReason(), saveCallDetailApiRequest.getCircle(),
                saveCallDetailApiRequest.getCallStartTime(), saveCallDetailApiRequest.getCallEndTime(), nmsId);

        if (null != saveCallDetailApiRequest.getContent() && !saveCallDetailApiRequest.getContent().isEmpty()) {

            setCardDetail(callDetail, saveCallDetailApiRequest.getContent());
        }
        callDetailService.create(callDetail);

        logger.info("CallDetail created successfully.");
    }

    /**
     * set CardContent of the CallDetail
     *
     * @param callDetail
     * @param list
     * @throws DataValidationException
     */
    private void setCardDetail(CallDetail callDetail, List<CardDetail> list) {

        for (CardDetail element : list) {
            callDetail.getCardDetail().add(element);
        }
        logger.info("CardDetail added successfully.");
    }

    /**
     * Update FlwDetail
     *
     * @param saveCallDetailApiRequest
     * @throws DataValidationException
     */
    private FlwDetail updateFlwDetail(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        FlwDetail flwDetail = null;

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo(ParseDataHelper.validateAndTrimMsisdn(
                "CallingNumber", saveCallDetailApiRequest.getCallingNumber()));

        if (null != frontLineWorker && null != (flwDetail = flwDetailService.findFlwDetailByNmsFlwId(frontLineWorker.getFlwId()))) {
            flwDetail.setNmsFlwId(frontLineWorker.getId());
            updateFlwDetail(flwDetail, saveCallDetailApiRequest);
        }
        return flwDetail;
    }

    /**
     * Update FlwDetail
     *
     * @param saveCallDetailApiRequest
     * @param flwDetail
     */
    private void updateFlwDetail(FlwDetail flwDetail, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        flwDetail.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter() + flwDetail.getEndOfUsagePrompt());
        flwDetail.setCurrentUsageInPulses(saveCallDetailApiRequest.getCallDurationInPulses() + flwDetail.getCurrentUsageInPulses());
        flwDetail.setWelcomePromptFlag(saveCallDetailApiRequest.getWelcomeMessagePromptFlag());
        flwDetail.setLastAccessDate(new DateTime(saveCallDetailApiRequest.getCallStartTime() * 1000));
        flwDetailService.update(flwDetail);

        logger.info("FlwDetail updated successfully.");
    }

}
