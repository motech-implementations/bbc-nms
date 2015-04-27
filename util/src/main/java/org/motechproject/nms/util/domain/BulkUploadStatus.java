package org.motechproject.nms.util.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data for BulkUploadStatus records
 * which are created after every bulk upload processing completion.
 */
@Entity
public class BulkUploadStatus extends MdsEntity {

    @Field
    private String uploadedBy;

    @Field
    private String bulkUploadFileName;

    @Field
    private DateTime timeOfUpload;

    @Field
    private Integer numberOfSuccessfulRecords;

    @Field
    private Integer numberOfFailedRecords;

    public BulkUploadStatus() {
        this.numberOfSuccessfulRecords = 0;
        this.numberOfFailedRecords = 0;
    }

    public BulkUploadStatus(String uploadedBy, String bulkUploadFileName, DateTime timeOfUpload, Integer numberOfFailedRecords, Integer numberOfSuccessfulRecords) {
        this.uploadedBy = uploadedBy;
        this.bulkUploadFileName = bulkUploadFileName;
        this.timeOfUpload = timeOfUpload;
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
        this.numberOfFailedRecords = numberOfFailedRecords;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getBulkUploadFileName() {
        return bulkUploadFileName;
    }

    public void setBulkUploadFileName(String bulkUploadFileName) {
        this.bulkUploadFileName = bulkUploadFileName;
    }

    public DateTime getTimeOfUpload() {
        return timeOfUpload;
    }

    public void setTimeOfUpload(DateTime timeOfUpload) {
        this.timeOfUpload = timeOfUpload;
    }

    public Integer getNumberOfSuccessfulRecords() {
        return numberOfSuccessfulRecords;
    }

    public void setNumberOfSuccessfulRecords(Integer numberOfSuccessfulRecords) {
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
    }

    public Integer getNumberOfFailedRecords() {
        return numberOfFailedRecords;
    }

    public void setNumberOfFailedRecords(Integer numberOfFailedRecords) {
        this.numberOfFailedRecords = numberOfFailedRecords;
    }

    public void incrementSuccessCount() {
        numberOfSuccessfulRecords++;
    }

    public void incrementFailureCount() {
        numberOfFailedRecords++;
    }


    /**
     * This method creates the deep copy of BulkUploadStatus object
     * on which it is invoked.
     *
     * @return BulkUploadStatus the deep copied object
     */
    public BulkUploadStatus createDeepCopy() {
        BulkUploadStatus bulkUploadStatusCopy = new BulkUploadStatus();
        bulkUploadStatusCopy.setUploadedBy(this.uploadedBy);
        bulkUploadStatusCopy.setBulkUploadFileName(this.bulkUploadFileName);
        bulkUploadStatusCopy.setTimeOfUpload(this.timeOfUpload);
        bulkUploadStatusCopy.setNumberOfSuccessfulRecords(this.numberOfSuccessfulRecords);
        bulkUploadStatusCopy.setNumberOfFailedRecords(this.numberOfFailedRecords);
        return bulkUploadStatusCopy;
    }
}
