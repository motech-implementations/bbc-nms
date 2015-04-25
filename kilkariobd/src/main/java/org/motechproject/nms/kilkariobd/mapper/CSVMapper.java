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

    public static List<Map<String, Object>> readWithCsvMapReader(String fileName) throws Exception {

        ICsvMapReader mapReader = null;
        List<Map<String, Object>> listOfCdrSummaryRecords = new ArrayList<>();
        try {
            mapReader = new CsvMapReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

            /*
             the header columns are used as the keys to the Map
              */
            final String[] header = mapReader.getHeader(true);

            Map<String, Object> cdrSummary;
            while ((cdrSummary = mapReader.read(header, null)) != null) {
                listOfCdrSummaryRecords.add(cdrSummary);
            }
        } finally {
            if (mapReader != null) {
                mapReader.close();
            }
        }
        return listOfCdrSummaryRecords;
    }

    public static void writeByCsvMapper(String fileName, List<OutboundCallRequest> callRequests) {
        Field[] fields = OutboundCallRequest.class.getDeclaredFields();
        String[] header = new String[fields.length];
        for (int index=0; index <= fields.length; index++) {
            header[index] = StringUtils.capitalize(fields[index].getName());
        }
        Map<String, Object> callRequestMap = new HashMap<>();
        try {
            File file = new File(fileName);
            FileWriter fos = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fos);
            CsvMapWriter csvMapWriter = new CsvMapWriter(bf, CsvPreference.STANDARD_PREFERENCE);
            csvMapWriter.writeHeader(header);
            for(OutboundCallRequest request : callRequests) {
                for (int index=0; index <= fields.length; index++) {
                    callRequestMap.put(fields[index].getName(), fields[index].get(request));
                }
                csvMapWriter.write(callRequestMap, header);
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            logger.error(ex.getMessage());
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
        };
        return processors;
    }
}
