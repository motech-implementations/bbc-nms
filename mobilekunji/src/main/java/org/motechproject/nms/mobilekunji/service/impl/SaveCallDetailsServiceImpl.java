package org.motechproject.nms.mobilekunji.service.impl;


import org.joda.time.DateTime;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
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

import java.util.Iterator;

/**
 * This class provides the implementation of SaveCallDetailsService
 */

@Service("saveCallDetailsService")
public class SaveCallDetailsServiceImpl implements SaveCallDetailsService {

    private CallDetailService callDetailService;

    private FlwDetailService flwDetailService;

    private UserProfileDetailsService userProfileDetailsService;

    private Logger logger = LoggerFactory.getLogger(SaveCallDetailsServiceImpl.class);

    @Autowired
    public SaveCallDetailsServiceImpl(CallDetailService callDetailService, FlwDetailService flwDetailService, UserProfileDetailsService userProfileDetailsService) {
        this.callDetailService = callDetailService;
        this.flwDetailService = flwDetailService;
        this.userProfileDetailsService = userProfileDetailsService;
    }

    /**
     * Saves Call details of the user
     *
     * @param saveCallDetailApiRequest
     * @throws org.motechproject.nms.util.helper.DataValidationException
     */
    @Override
    public void saveCallDetails(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        logger.info("Save CallDetail Entered successfully.");

        userProfileDetailsService.validateOperator(saveCallDetailApiRequest.getOperator());

        Long nmsId = updateFlwDetail(saveCallDetailApiRequest);

        setCallDetail(nmsId,saveCallDetailApiRequest);

        logger.info("Save CallDetail executed successfully.");
    }

    private void setCallDetail(Long nmsId,SaveCallDetailApiRequest saveCallDetailApiRequest) {

        CallDetail callDetail = new CallDetail(saveCallDetailApiRequest.getCallId(),nmsId,saveCallDetailApiRequest.getCircle(),
                saveCallDetailApiRequest.getCallStartTime(),saveCallDetailApiRequest.getCallEndTime());

        setCardContent(callDetail, saveCallDetailApiRequest);
        callDetailService.create(callDetail);

        logger.info("CallDetail created successfully.");
    }

    private void setCardContent(CallDetail callDetail, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        for (Iterator<CardDetail> itr = saveCallDetailApiRequest.getContent().iterator(); itr.hasNext(); ) {
            CardDetail element = itr.next();
            callDetail.getCardDetail().add(element);
        }
        logger.info("CardDetail added successfully.");
    }

    private Long updateFlwDetail(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        FlwDetail flwDetail = flwDetailService.findServiceConsumptionByMsisdn(saveCallDetailApiRequest.getCallingNumber());

        if (null != flwDetail) {
            updateFlwDetail(flwDetail, saveCallDetailApiRequest);
        } else {
            ParseDataHelper.raiseInvalidDataException("CallingNumber ",saveCallDetailApiRequest.getCallingNumber().toString());
        }
        return flwDetail.getNmsFlwId();
    }

    private void updateFlwDetail(FlwDetail flw, SaveCallDetailApiRequest saveCallDetailApiRequest) {


       flw.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter() + flw.getEndOfUsagePrompt());
       flw.setCurrentUsageInPulses(saveCallDetailApiRequest.getCallDurationInPulses() + flw.getCurrentUsageInPulses());
       flw.setWelcomePromptFlag(saveCallDetailApiRequest.getWelcomeMessagePromptFlag());
       flw.setLastAccessDate(new DateTime(saveCallDetailApiRequest.getCallStartTime()));
       flwDetailService.update(flw);

       logger.info("FlwDetail updated successfully.");
    }

}
