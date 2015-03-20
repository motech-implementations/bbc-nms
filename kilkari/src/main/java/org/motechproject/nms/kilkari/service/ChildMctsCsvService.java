package org.motechproject.nms.kilkari.service;

import java.util.List;

/**
 * Expose method to implement the logic to insert child record
 */
public interface ChildMctsCsvService {

    /**
     * This method process ChildMctsCsv
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    void processChildMctsCsv(String csvFileName, List<Long> uploadedIDs);
    
}
