package org.motechproject.nms.kilkariobd.domain;

public enum CallFlowStatus {
    /* Event Received to start preparing OBD Target File */
    OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED,

    /* Previous day CDR Files processing failure statuses */
    CDR_SUMMARY_PROCESSING_FAILED,
    CDR_DETAIL_PROCESSING_FAILED,

    /* OBD Call request file created and copied to shared location */
    OUTBOUND_CALL_REQUEST_FILE_CREATED,
    OUTBOUND_CALL_REQUEST_FILE_COPIED,

    /* Event Received to send Target file notification to IVR */
    NOTIFY_OUTBOUND_FILE_EVENT_RECEIVED,
    
    /* Target File Notification sent to IVR */
    OBD_FILE_NOTIFICATION_SENT_TO_IVR,
    
    /* Status for OBD file Processing at IVR */
    OUTBOUND_CALL_REQUEST_FILE_PROCESSED_AT_IVR,
    OUTBOUND_CALL_REQUEST_FILE_PROCESSING_FAILED_AT_IVR,

    /* CDR File Notification Recived */
    CDR_FILE_NOTIFICATION_RECEIVED,

    /* CDR Files Processing Failed */
    CDR_FILES_PROCESSING_FAILED,

    /* CDR File processed --> End of Call Flow */
    CDR_FILES_PROCESSED
}
