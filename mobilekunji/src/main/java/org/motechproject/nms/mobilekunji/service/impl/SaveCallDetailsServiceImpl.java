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

        setCallDetail(nmsId, saveCallDetailApiRequest);

        logger.info("Save CallDetail executed successfully.");
    }

    /**
     * Saves Call details of the user
     *
     * @param saveCallDetailApiRequest
     * @param nmsId
     */
    private void setCallDetail(Long nmsId, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        CallDetail callDetail = new CallDetail(saveCallDetailApiRequest.getCallId(), nmsId, saveCallDetailApiRequest.getCircle(),
                saveCallDetailApiRequest.getCallStartTime(), saveCallDetailApiRequest.getCallEndTime());

        setCardDetail(callDetail, saveCallDetailApiRequest);
        callDetailService.create(callDetail);

        logger.info("CallDetail created successfully.");
    }

    /**
     * set CardContent of the CallDetail
     *
     * @param callDetail
     * @param saveCallDetailApiRequest
     * @throws org.motechproject.nms.util.helper.DataValidationException
     */
    private void setCardDetail(CallDetail callDetail, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        for (CardDetail element : saveCallDetailApiRequest.getContent()) {
            callDetail.getCardDetail().add(element);
        }
        logger.info("CardDetail added successfully.");
    }

    /**
     * Update FlwDetail
     *
     * @param saveCallDetailApiRequest
     * @throws org.motechproject.nms.util.helper.DataValidationException
     */
    private Long updateFlwDetail(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        FlwDetail flwDetail = flwDetailService.findFlwDetailByMsisdn(ParseDataHelper.validateAndTrimMsisdn(
                "CallingNumber", saveCallDetailApiRequest.getCallingNumber()));

        if (null != flwDetail) {
            updateFlwDetail(flwDetail, saveCallDetailApiRequest);
        } else {
            ParseDataHelper.raiseInvalidDataException("CallingNumber ", saveCallDetailApiRequest.getCallingNumber());
        }
        return flwDetail.getNmsFlwId();
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
