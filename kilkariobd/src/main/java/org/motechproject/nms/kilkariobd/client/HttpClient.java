package org.motechproject.nms.kilkariobd.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
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

import java.io.UnsupportedEncodingException;

public class HttpClient {
    private Logger logger = LoggerFactory.getLogger(HttpClient.class);

    @Autowired
    private OutboundCallFlowService callFlowService;

    @Autowired
    private ConfigurationService configurationService;

    private org.apache.commons.httpclient.HttpClient commonHttpClient = new org.apache.commons.httpclient.HttpClient();

    public void notifyTargetFile(String fileName, String checksum, Long recordsCount) {
        HttpMethod postMethod = buildTargetNotificationRequest(ivrUrl() + "/notifytargetfile", fileName, checksum, recordsCount);
        HttpClientParams params =  commonHttpClient.getParams();
        RetryStrategy retryStrategy = new RetryStrategy();
        Integer retryNumber = 0;
        Long timeOutValue = retryStrategy.getTimeOutInterval(retryNumber, null);
        while (retryStrategy.shouldRetry(retryNumber++)) {
            try {
                params.setConnectionManagerTimeout(timeOutValue);
                params.setSoTimeout(timeOutValue.intValue());
                int returnCode = commonHttpClient.executeMethod(postMethod);
                String responseBody = postMethod.getResponseBodyAsString();
            } catch (NoHttpResponseException | ConnectTimeoutException exception) {
               timeOutValue = retryStrategy.getTimeOutInterval(retryNumber, timeOutValue);
            } catch (Exception ex) {
                logger.error("Exception occurred while connecting to the server");
            } finally {
                postMethod.releaseConnection();
            }
        }

        OutboundCallFlow todayCallFlow = callFlowService.findRecordByCallStatus(CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_COPIED);
        todayCallFlow.setStatus(CallFlowStatus.OBD_FILE_NOTIFICATION_SENT_TO_IVR);
        callFlowService.update(todayCallFlow);
    }

    public void notifyCDRFileProcessedStatus(FileProcessingStatus status) {
        HttpMethod postMethod = buildCdrFileProcessedStatusRequest(ivrUrl() + "/NotifyCDRFileProcessedStatus", status);
        HttpClientParams params =  commonHttpClient.getParams();
        RetryStrategy retryStrategy = new RetryStrategy();
        Integer retryNumber = 0;
        Long timeOutValue = retryStrategy.getTimeOutInterval(retryNumber, null);
        while (retryStrategy.shouldRetry(retryNumber++)) {
            try {
                params.setConnectionManagerTimeout(timeOutValue);
                params.setSoTimeout(timeOutValue.intValue());
                int returnCode = commonHttpClient.executeMethod(postMethod);
                String responseBody = postMethod.getResponseBodyAsString();
            } catch (NoHttpResponseException | ConnectTimeoutException exception) {
                timeOutValue = retryStrategy.getTimeOutInterval(retryNumber, timeOutValue);
            } catch (Exception ex) {
                logger.error("Exception occurred while connecting to the server");
            } finally {
                postMethod.releaseConnection();
            }
        }
    }

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

    private HttpMethod buildTargetNotificationRequest(String url, String fileName, String checksum, Long recordsCount) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        TargetNotificationRequest request = new TargetNotificationRequest(fileName, checksum, recordsCount);
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    private HttpMethod buildCdrFileProcessedStatusRequest(String url, FileProcessingStatus status) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        FileProcessedStatusRequest request = new FileProcessedStatusRequest(status, "");
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    private String ivrUrl() {
        Configuration configuration = configurationService.getConfiguration();
        return String.format(configuration.getObdIvrUrl());
    }


}
