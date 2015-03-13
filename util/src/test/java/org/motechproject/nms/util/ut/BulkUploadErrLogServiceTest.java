package org.motechproject.nms.util.ut;

import org.junit.Before;
import org.junit.Ignore;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class contains test cases to verify that an error log is
 * written to log file for every failed record in csv and
 * a summary containing the count of passed and failed records is
 * written after bulk upload completion.
 */
public class BulkUploadErrLogServiceTest {

    //@Mock
    //private BulkUploadStatusService bulkUploadStatusService;

    //@InjectMocks
    //private BulkUploadErrLogService bulkUploadErrLogService =  new BulkUploadErrLogServiceImpl(bulkUploadStatusService);

    @Before
    public void setUp() {

        initMocks(this);
    }

    /**
     * This test case tests that a Bulk upload error log is
     * written to the log file if a csv record contains invalid data.
     */
    @Ignore
    public void shouldWriteBulkUploadErrLog() {
//
//        String errDesc = String.format(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, "Name");
//        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, errDesc);
//        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
//        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);
//
//        Path path = Paths.get(".", logFileName);
//        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
//
//        List<String> expectedLogs = new ArrayList<>();
//        expectedLogs.add("Record upload failed for : ");
//        expectedLogs.add("Record Details, Mandatory Parameter Missing, Upload unsuccessful as Name is missing.");
//        try {
//            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
//            Assert.assertTrue(lines.contains(expectedLogs.get(0)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(1)));
//            Files.delete(path);
//        } catch (IOException e) {
//            Assert.fail();
//        }
    }

    /**
     * This test case tests that a Bulk upload error log is
     * written to the log file if a csv record contains invalid data.
     */
    @Ignore
    public void shouldWriteBulkUploadErrLogForInvalidData() {
//
//        String errDesc = String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, "Code");
//        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.INVALID_DATA, errDesc);
//        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
//        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);
//
//        Path path = Paths.get(".", logFileName);
//        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
//
//        List<String> expectedLogs = new ArrayList<>();
//        expectedLogs.add("Record upload failed for : ");
//        expectedLogs.add("Record Details, Invalid Data, Upload unsuccessful as Code is invalid.");
//        try {
//            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
//            Assert.assertTrue(lines.contains(expectedLogs.get(0)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(1)));
//            Files.delete(path);
//        } catch (IOException e) {
//            Assert.fail();
//        }
    }

    /**
     * This test case tests that a Bulk upload error log is
     * written to the log file if a csv record is missing.
     */
    @Ignore
    public void shouldWriteBulkUploadErrLogForCsvRecordMissing() {
//
//        String errDesc = String.format(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
//        BulkUploadError bulkUploadError = new BulkUploadError("Record Details", ErrorCategoryConstants.CSV_RECORD_MISSING, errDesc);
//        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
//        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, bulkUploadError);
//
//        Path path = Paths.get(".", logFileName);
//        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
//
//        List<String> expectedLogs = new ArrayList<>();
//        expectedLogs.add("Record upload failed for : ");
//        expectedLogs.add("Record Details, CSV Record Not Found, Upload unsuccessful as record is missing in CSV.");
//        try {
//            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
//            Assert.assertTrue(lines.contains(expectedLogs.get(0)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(1)));
//            Files.delete(path);
//        } catch (IOException e) {
//            Assert.fail();
//        }
    }

    /**
     * This test case tests that the Summary is written in
     * the log file after every bulk upload.
     */
    @Ignore
    public void shouldWriteBulkUploadProcessingSummary() {
//        String userName = "Aricent";
//        String bulkUploadFileName = "csv-import.someFileName";
//        BulkUploadError bulkUploadError = new BulkUploadError();
//        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
//        CsvProcessingSummary csvProcessingSummary = new CsvProcessingSummary(76,4);
//
//        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, bulkUploadFileName, logFileName, csvProcessingSummary);
//
//        List<String> expectedLogs = new ArrayList<>();
//        expectedLogs.add("\n");
//        expectedLogs.add(Constants.UNDERLINE);
//        expectedLogs.add(Constants.BULK_UPLOAD_SUMMARY_TITLE);
//        expectedLogs.add(Constants.UNDERLINE);
//        expectedLogs.add("\tUploaded by : Aricent");
//        expectedLogs.add("\tNumber Of Records Successful : 76");
//        expectedLogs.add("\tNumber Of Records Failed : 4");
//
//        Path path = Paths.get(".", logFileName);
//        Assert.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
//
//        try {
//            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
//            Assert.assertEquals(lines.size(), expectedLogs.size());
//            Assert.assertTrue(lines.contains(expectedLogs.get(1)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(2)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(3)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(4)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(5)));
//            Assert.assertTrue(lines.contains(expectedLogs.get(6)));
//            Files.delete(path);
//        } catch (IOException e) {
//            Assert.fail();
//        }
    }
}