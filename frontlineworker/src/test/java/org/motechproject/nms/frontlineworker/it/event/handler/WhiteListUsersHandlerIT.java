package org.motechproject.nms.frontlineworker.it.event.handler;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.CsvWhiteListUsers;
import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;
import org.motechproject.nms.frontlineworker.event.handler.WhiteListUsersUploadHandler;
import org.motechproject.nms.frontlineworker.repository.WhiteListUsersRecordDataService;
import org.motechproject.nms.frontlineworker.service.CsvWhiteListUsersService;
import org.motechproject.nms.frontlineworker.service.WhiteListUsersService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This Class models the integration testing of WhiteListUsersUploadHandler.
 */


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class WhiteListUsersHandlerIT extends BasePaxIT {

    private static boolean setUpIsDone = false;
    Map<String, Object> parameters = new HashMap<>();
    List<Long> uploadedIds = new ArrayList<Long>();
    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;
    @Inject
    private WhiteListUsersService whiteListUsersService;
    @Inject
    private CsvWhiteListUsersService csvWhiteListUsersService;
    @Inject
    private WhiteListUsersRecordDataService whiteListUsersRecordDataService;

    private WhiteListUsersUploadHandler whiteListUsersUploadHandler;

    @Before
    public void setUp() {

        whiteListUsersUploadHandler = new WhiteListUsersUploadHandler(bulkUploadErrLogService,
                whiteListUsersService, csvWhiteListUsersService, whiteListUsersRecordDataService
        );
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(whiteListUsersService);
        assertNotNull(csvWhiteListUsersService);
        assertNotNull(whiteListUsersRecordDataService);

        CsvWhiteListUsers csvWhiteListUsers;
        CsvWhiteListUsers csvdbWhiteListUsers;


        // Record 1 testWhiteListUsersValidDataGetByPhnNo

        csvWhiteListUsers = new CsvWhiteListUsers("9876598765");

        csvWhiteListUsers.setCreator("Rashi");
        csvWhiteListUsers.setModifiedBy("Rashi");
        csvWhiteListUsers.setOwner("Rashi");

        csvdbWhiteListUsers = csvWhiteListUsersService.createWhiteListUsersCsv(csvWhiteListUsers);
        assertNotNull(csvdbWhiteListUsers);
        uploadedIds.add(csvdbWhiteListUsers.getId());

        // Record 2 testWhiteListUsersValidDataLargerPhnNo

        csvWhiteListUsers = new CsvWhiteListUsers("98765987650");

        csvdbWhiteListUsers = csvWhiteListUsersService.createWhiteListUsersCsv(csvWhiteListUsers);
        assertNotNull(csvdbWhiteListUsers);
        uploadedIds.add(csvdbWhiteListUsers.getId());

        // Record 3 testWhiteListUsersValidDataSmallerPhnNo

        csvWhiteListUsers = new CsvWhiteListUsers("98765");

        csvdbWhiteListUsers = csvWhiteListUsersService.createWhiteListUsersCsv(csvWhiteListUsers);
        assertNotNull(csvdbWhiteListUsers);
        uploadedIds.add(csvdbWhiteListUsers.getId());

        // Record 4 testWhiteListUsers contactNo not present already present in db.

        csvWhiteListUsers = new CsvWhiteListUsers("9876598765");

        csvdbWhiteListUsers = csvWhiteListUsersService.createWhiteListUsersCsv(csvWhiteListUsers);
        assertNotNull(csvdbWhiteListUsers);
        uploadedIds.add(csvdbWhiteListUsers.getId());

        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "WhiteListUsers.csv");
    }


    @Test
    public void testWhiteListUsersAll() {

        MotechEvent motechEvent = new MotechEvent(ConfigurationConstants.WLU_UPLOAD_SUCCESS, parameters);
        whiteListUsersUploadHandler.wluDataHandlerSuccess(motechEvent);

        WhiteListUsers whiteListUser;

        // Record 1 testWhiteListUsersValidDataGetByPhnNo

        whiteListUser = whiteListUsersService.findContactNo("9876598765");
        assertNotNull(whiteListUser);
        assertEquals("9876598765", whiteListUser.getContactNo());

        // Record 2 testWhiteListUsers LargerPhnNo

        whiteListUser = whiteListUsersService.findContactNo("98765987650");
        assertNull(whiteListUser);

        // Record 3 testWhiteListUsers SmallerPhnNo

        whiteListUser = whiteListUsersService.findContactNo("98765");
        assertNull(whiteListUser);

        // Record 4 testWhiteListUsers contactNo not already present in db.

        whiteListUser = whiteListUsersService.findContactNo("9876598765");
        assertNotNull(whiteListUser);


        List<CsvWhiteListUsers> listWluCsv = csvWhiteListUsersService.retrieveAllFromCsv();
        assertTrue(listWluCsv.size() == 0);

    }
}



