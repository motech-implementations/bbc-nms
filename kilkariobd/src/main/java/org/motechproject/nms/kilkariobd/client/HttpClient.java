package org.motechproject.nms.kilkariobd.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
import org.motechproject.nms.kilkariobd.dto.request.TargetNotificationRequest;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;

/**
 * This class provides method to make http request to a server
 */
public class HttpClient {
    private Logger logger = LoggerFactory.getLogger(HttpClient.class);

    @Autowired
    private OutboundCallFlowService callFlowService;

    @Autowired
    private ConfigurationService configurationService;

    private org.apache.commons.httpclient.HttpClient commonHttpClient = new org.apache.commons.httpclient.HttpClient();

    /**
     * This method makes an http request to notify the server of the target file has been copied.
     * @param fileName name of the file that has been copied
     * @param checksum MD5checksum of the copied file
     * @param recordsCount records count of the copied file
     */
    public void notifyTargetFile(String fileName, String checksum, Long recordsCount) {
        logger.debug("notifyTargetFile Notification : started");
        HttpMethod postMethod = buildTargetNotificationRequest(ivrUrl() + "/notifytargetfile", fileName, checksum, recordsCount);
        postHttpRequestAndRetry(postMethod);
        logger.debug("notifyTargetFile Notification : ended");
    }

    /**
     * This method makes an http request to a server to notify the CDR file processed status.
     * @param status status of the processed files.
     */
    public void notifyCDRFileProcessedStatus(FileProcessingStatus status, String fileName) {
        logger.debug("notifyCDRFileProcessedStatus Notification : started");
        HttpMethod postMethod = buildCdrFileProcessedStatusRequest(ivrUrl() + "/NotifyCDRFileProcessedStatus", status, fileName);
        postHttpRequestAndRetry(postMethod);
        logger.debug("notifyCDRFileProcessedStatus Notification : ended");
    }

    /*
    This method make post request to the http server and retry if failed.
     */
    private void postHttpRequestAndRetry(HttpMethod postMethod) {
        Integer retryNumber = Constants.FIRST_ATTEMPT;
        Long timeOutValue = HttpRetryStrategy.getTimeOutInterval(retryNumber, null);
        HttpClientParams params =  commonHttpClient.getParams();
        while (HttpRetryStrategy.shouldRetry(retryNumber++)) {
            try {
                params.setConnectionManagerTimeout(timeOutValue);
                params.setSoTimeout(timeOutValue.intValue());
                int returnCode = commonHttpClient.executeMethod(postMethod);
                String responseBody = postMethod.getResponseBodyAsString();
                if (returnCode == HttpStatus.BAD_REQUEST.value()) {
                    logger.error("Http post failed. Response returned : " + responseBody);
                }
            } catch (NoHttpResponseException | ConnectTimeoutException exception) {
                timeOutValue = HttpRetryStrategy.getTimeOutInterval(retryNumber, timeOutValue);
            } catch (Exception ex) {
                logger.error("Exception occurred while connecting to the server", ex);
            } finally {
                postMethod.releaseConnection();
            }
        }
    }

    /*
    This method builds the http post request with url and request body
     */
    private HttpMethod buildRequest(String url, String requestBody) {
        PostMethod postMethod  = new PostMethod(url);
        StringRequestEntity stringEntity = null;
        try {
            stringEntity = new StringRequestEntity(requestBody, "application/json", null);
        } catch (UnsupportedEncodingException e) {
            logger.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }
        postMethod.setRequestEntity(stringEntity);
        return postMethod;
    }

    /*
    This method build the TargetNotificationRequestBody
     */
    private HttpMethod buildTargetNotificationRequest(String url, String fileName, String checksum, Long recordsCount) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        TargetNotificationRequest request = new TargetNotificationRequest(fileName, checksum, recordsCount);
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    /*
    This method build the CdrFileProcessedStatusRequest
     */
    private HttpMethod buildCdrFileProcessedStatusRequest(String url, FileProcessingStatus status, String fileName) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        FileProcessedStatusRequest request = new FileProcessedStatusRequest(status, fileName);
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    private String ivrUrl() {
        Configuration configuration = configurationService.getConfiguration();
        return String.format(configuration.getObdIvrUrl());
    }


}
