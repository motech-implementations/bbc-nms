package org.motechproject.nms.util.ut;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.util.service.BulkUploadStatusService;
import org.motechproject.nms.util.service.impl.BulkUploadErrLogServiceImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 */
public class BulkUploadErrLogServiceTest {

    @Mock
    private BulkUploadStatusService bulkUploadStatusService;

    @InjectMocks
    private BulkUploadErrLogService bulkUploadErrLogService =  new BulkUploadErrLogServiceImpl();

    @Before
    public void setUp() {

        initMocks(this);
    }


    @Test
    public void shouldWriteBulkUploadErrLog() {

        String errDesc = String.format(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, "Name");
        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, errDesc);
        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);

        Path path = Paths.get(".", logFileName);
        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        List<String> expectedLogs = new ArrayList<>();
        expectedLogs.add("Record upload failed for : ");
        expectedLogs.add("Record Details, Mandatory Parameter Missing, Upload unsuccessful as Name is missing.");
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            Assert.assertEquals(lines.size(), expectedLogs.size());
            Assert.assertEquals(lines.get(0), expectedLogs.get(0));
            Assert.assertEquals(lines.get(1), expectedLogs.get(1));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void shouldWriteBulkUploadErrLogForInvalidData() {

        String errDesc = String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, "Code");
        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.INVALID_DATA, errDesc);
        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);

        Path path = Paths.get(".", logFileName);
        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        List<String> expectedLogs = new ArrayList<>();
        expectedLogs.add("Record upload failed for : ");
        expectedLogs.add("Record Details, Invalid Data, Upload unsuccessful as Code invalid.");
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            Assert.assertEquals(lines.size(), expectedLogs.size());
            Assert.assertEquals(lines.get(0), expectedLogs.get(0));
            Assert.assertEquals(lines.get(1), expectedLogs.get(1));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void shouldWriteBulkUploadErrLogForCsvRecordMissing() {

        String errDesc = String.format(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.CSV_RECORD_MISSING, errDesc);
        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);

        Path path = Paths.get(".", logFileName);
        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        List<String> expectedLogs = new ArrayList<>();
        expectedLogs.add("Record upload failed for : ");
        expectedLogs.add("Record Details, CSV Record Not Found, Upload unsuccessful as record is missing in CSV.");
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            Assert.assertEquals(lines.size(), expectedLogs.size());
            Assert.assertEquals(lines.get(0), expectedLogs.get(0));
            Assert.assertEquals(lines.get(1), expectedLogs.get(1));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void shouldWriteBulkUploadProcessingSummary() {
        String userName = "Aricent";
        String bulkUploadFileName = "csv-import.someFileName";
        BulkUploadError bulkUploadError = new BulkUploadError();
        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
        CsvProcessingSummary csvProcessingSummary = new CsvProcessingSummary(76,4);

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, bulkUploadFileName, logFileName, csvProcessingSummary);

        List<String> expectedLogs = new ArrayList<>();
        expectedLogs.add("\n");
        expectedLogs.add(Constants.UNDERLINE);
        expectedLogs.add(Constants.BULK_UPLOAD_SUMMARY_TITLE);
        expectedLogs.add(Constants.UNDERLINE);
        expectedLogs.add("\tUploaded by : Aricent");
        expectedLogs.add("\tNumber Of Records Successful : 76");
        expectedLogs.add("\tNumber Of Records Failed : 4");

        Path path = Paths.get(".", logFileName);
        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            Assert.assertEquals(lines.size(), expectedLogs.size());
            Assert.assertEquals(lines.get(1), expectedLogs.get(1));
            Assert.assertEquals(lines.get(2), expectedLogs.get(2));
            Assert.assertEquals(lines.get(3), expectedLogs.get(3));
            Assert.assertEquals(lines.get(4), expectedLogs.get(4));
            Assert.assertEquals(lines.get(5), expectedLogs.get(5));
            Assert.assertEquals(lines.get(6), expectedLogs.get(6));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}