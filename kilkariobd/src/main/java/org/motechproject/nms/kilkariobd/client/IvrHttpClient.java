package org.motechproject.nms.kilkariobd.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.dto.request.CdrFileProcessedStatusRequest;
import org.motechproject.nms.kilkariobd.dto.request.TargetNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class IvrHttpClient {
    private Logger logger = LoggerFactory.getLogger(IvrHttpClient.class);

    private HttpClient commonHttpClient = new HttpClient();

    public void notifyTargetFile() {
        HttpMethod postMethod = buildTargetNotificationRequest(ivrUrl());
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

    public void notifyCDRFileProcessedStatus(FileProcessingStatus status) {
        HttpMethod postMethod = buildCdrFileProcessedStatusRequest(ivrUrl(), status);
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

    private HttpMethod buildTargetNotificationRequest(String url) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        TargetNotificationRequest request = new TargetNotificationRequest("", "", 0);
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    private HttpMethod buildCdrFileProcessedStatusRequest(String url, FileProcessingStatus status) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        CdrFileProcessedStatusRequest request = new CdrFileProcessedStatusRequest(status.ordinal(), "");
        String requestBody = gson.toJson(request);
        return buildRequest(url, requestBody);
    }

    private String ivrUrl() {
        return String.format("http://%s/%s/obdmanager/notifytargetfile", "", "");
    }


}
