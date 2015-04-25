package org.motechproject.nms.util.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class models Bulk Upload erroneous record details
 * and the category and description of error
 */
@Entity
public class BulkUploadError extends MdsEntity{

    @UIDisplayable(position = 0)
    @Field
    private String csvName;

    @UIDisplayable(position = 1)
    @Field
    private DateTime timeOfUpload;

    @UIDisplayable(position = 2)
    @Field
    private RecordType recordType;

    @UIDisplayable(position = 3)
    @Field
    private String errorCategory;

    @UIDisplayable(position = 4)
    @Field
    private String errorDescription;

    @UIDisplayable(position = 5)
    @Field
    private String recordDetails;

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

    /**
     * This method creates the deep copy of BulkUploadError object
     * on which it is invoked.
     *
     * @return BulkUploadError the deep copied object
     */
    public BulkUploadError createDeepCopy() {
        BulkUploadError bulkUploadErrorCopy = new BulkUploadError();
        bulkUploadErrorCopy.setCsvName(this.csvName);
        bulkUploadErrorCopy.setTimeOfUpload(this.timeOfUpload);
        bulkUploadErrorCopy.setRecordType(this.recordType);
        bulkUploadErrorCopy.setRecordDetails(this.recordDetails);
        bulkUploadErrorCopy.setErrorCategory(this.errorCategory);
        bulkUploadErrorCopy.setErrorDescription(this.errorDescription);
        return bulkUploadErrorCopy;
    }

}
