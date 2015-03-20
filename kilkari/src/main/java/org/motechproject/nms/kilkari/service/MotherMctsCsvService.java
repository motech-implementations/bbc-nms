package org.motechproject.nms.kilkari.service;

import java.util.List;

/**
 * Expose method to implement the logic to insert mother record
 */
public interface MotherMctsCsvService {


    /**
     * this method process the mother record
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    void processMotherMctsCsv(String csvFileName, List<Long> uploadedIDs);

}
