package org.motechproject.nms.kilkariobd.mapper;


import org.apache.commons.lang.StringUtils;
import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

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
    public static List<Map<String, String>> readWithCsvMapReader(String fileName) throws Exception {

        ICsvMapReader mapReader = null;
        List<Map<String, String>> listOfCdrSummaryRecords = new ArrayList<>();
        try {
            mapReader = new CsvMapReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

            /*
             the header columns are used as the keys to the Map
              */
            final String[] header = mapReader.getHeader(true);

            Map<String, String> cdrSummary;
            while ((cdrSummary = mapReader.read(header)) != null) {
                listOfCdrSummaryRecords.add(cdrSummary);
            }
        } finally {
            if (mapReader != null) {
                mapReader.close();
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
        String[] header = new String[fields.length];
        for (int index=0; index < fields.length; index++) {
            header[index] = StringUtils.capitalize(fields[index].getName());
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
                    callRequestMap.put(StringUtils.capitalize(field.getName()), field.get(request));
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
