package org.motechproject.nms.kilkari.service;

import java.util.List;

/**
 * Expose method to implement the logic to insert child record
 */
public interface CsvMctsChildService {

    /**
     * This method process the child csv records under transaction means
     * if single records fails whole transaction is rolled back 
     * if all records process successfully data is committed
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    void processChildMctsCsv(String csvFileName, List<Long> uploadedIDs);
    
}
