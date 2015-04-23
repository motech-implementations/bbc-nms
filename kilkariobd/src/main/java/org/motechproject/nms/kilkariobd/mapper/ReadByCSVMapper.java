package org.motechproject.nms.kilkariobd.mapper;


import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to read and map csv files.
 */
public class ReadByCSVMapper {

    public static List<Map<String, Object>> readWithCsvMapReader(String fileName) throws Exception {

        ICsvMapReader mapReader = null;
        List<Map<String, Object>> listOfCdrSummaryRecords = new ArrayList<>();
        try {
            mapReader = new CsvMapReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

            /*
             the header columns are used as the keys to the Map
              */
            final String[] header = mapReader.getHeader(true);
            //final CellProcessor[] processors = getProcessors();

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

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
        };
        return processors;
    }
}
