package org.motechproject.nms.util.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class models Bulk Upload erroneous record details
 * and the category and description of error
 */
@Entity
public class BulkUploadError extends MdsEntity{

    @Field
    private String csvName;

    @Field
    private DateTime timeOfUpload;

    @Field
    private RecordType recordType;

    @Field
    private String recordDetails;

    @Field
    private String errorCategory;

    @Field
    private String errorDescription;

    public BulkUploadError() {
    }

    public BulkUploadError(String csvName, DateTime timeOfUpload, RecordType recordType, String recordDetails, String errorCategory, String errorDescription) {
        this.csvName = csvName;
        this.timeOfUpload = timeOfUpload;
        this.recordType = recordType;
        this.recordDetails = recordDetails;
        this.errorCategory = errorCategory;
        this.errorDescription = errorDescription;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public DateTime getTimeOfUpload() {
        return timeOfUpload;
    }

    public void setTimeOfUpload(DateTime timeOfUpload) {
        this.timeOfUpload = timeOfUpload;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public String getRecordDetails() {
        return recordDetails;
    }

    public void setRecordDetails(String recordDetails) {
        this.recordDetails = recordDetails;
    }

    public String getErrorCategory() {
        return errorCategory;
    }

    public void setErrorCategory(String errorCategory) {
        this.errorCategory = errorCategory;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
