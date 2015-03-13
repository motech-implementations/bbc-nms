package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;

public interface UserDetailsService {

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     * @param msisdn Phone number of the beneficiary
     * @param circleCode circle code of the beneficiary
     * @return Subscriber detail response object
     */
    public SubscriberDetailApiResponse getSubscriberDetails(String msisdn, String circleCode);
}
