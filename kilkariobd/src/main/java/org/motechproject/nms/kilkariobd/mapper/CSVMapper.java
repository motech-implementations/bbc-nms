package org.motechproject.nms.kilkariobd.mapper;


import org.apache.commons.lang.StringUtils;
import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to read and map csv files.
 */
public class CSVMapper {

    static Logger logger = LoggerFactory.getLogger(CSVMapper.class);

    /**
     * Method to read CSV file
     * @param fileName name of the csv file to be read
     * @return List of Map
     * @throws Exception
     */
    public static List<Map<String, String>> readWithCsvMapReader(String fileName) throws FileNotFoundException {

        ICsvMapReader mapReader = null;
        List<Map<String, String>> listOfCdrSummaryRecords = new ArrayList<>();
        mapReader = new CsvMapReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
        try {
            /*
             the header columns are used as the keys to the Map
              */
            final String[] header = mapReader.getHeader(true);

            Map<String, String> cdrSummary;

                while ((cdrSummary = mapReader.read(header)) != null) {
                    listOfCdrSummaryRecords.add(cdrSummary);
                }
            } catch (IOException e) {
                logger.error("IO Exception", e);
        } finally {
            if (mapReader != null) {
                try {
                    mapReader.close();
                } catch (IOException e) {
                    logger.error("IO Exception", e);
                }
            }
        }
        return listOfCdrSummaryRecords;
    }

    /**
     * Method to write on to CSV file
     * @param fileName name of the csv file on which data is to written
     * @param callRequests List<OutboundCallRequest>
     */
    public static void writeByCsvMapper(String fileName, List<OutboundCallRequest> callRequests) {
        Field[] fields = OutboundCallRequest.class.getDeclaredFields();

        List<String> callRequestFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(org.motechproject.mds.annotations.Field.class)) {
                field.setAccessible(true);
                callRequestFields.add(StringUtils.capitalize(field.getName()));
            }
        }
        /* prepare header */
        String[] header = new String[callRequestFields.size()];
        for (int index = 0; index < callRequestFields.size(); index++) {
            header[index] = callRequestFields.get(index);
        }

        Map<String, Object> callRequestMap = new HashMap<>();
        CsvMapWriter csvMapWriter = null;
        try {
            File file = new File(fileName);
            FileWriter fos = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fos);
            csvMapWriter = new CsvMapWriter(bf, CsvPreference.STANDARD_PREFERENCE);
            csvMapWriter.writeHeader(header);
            for(OutboundCallRequest request : callRequests) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(org.motechproject.mds.annotations.Field.class)) {
                        callRequestMap.put(StringUtils.capitalize(field.getName()), field.get(request));
                    }
                }
                csvMapWriter.write(callRequestMap, header);
            }

        } catch (IllegalAccessException | IllegalArgumentException | IOException ex ) {
            logger.error("Error occurred while exporting the file :" + fileName, ex.getMessage());
        } finally {
            if (csvMapWriter != null) {
                try {
                    csvMapWriter.close();
                } catch (IOException ex) {
                    logger.error("Error occurred while closing the csvMapWriter for file :" + fileName, ex.getMessage() );
                }
            }
        }
    }
}
