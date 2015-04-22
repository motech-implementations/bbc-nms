package org.motechproject.nms.kilkariobd.domain;

/**
 * Enum to define different file processing statuses.
 */
public enum FileProcessingStatus {
    FILE_PROCESSED_SUCCESSFULLY(8000),
    FILE_NOT_ACCESSIBLE(8001),
    FILE_CHECKSUM_ERROR(8002),
    FILE_RECORDCOUNT_ERROR(8003),
    FILE_OUTSIDE_SOCIALHOURS(8004),
    FILE_ERROR_IN_FILE_FORMAT(8005);

    private int value;

    private FileProcessingStatus(int value) {
        this.value = value;
    }
}
