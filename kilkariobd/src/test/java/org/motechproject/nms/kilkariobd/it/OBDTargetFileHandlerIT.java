package org.motechproject.nms.kilkariobd.it;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.builder.*;
import org.motechproject.nms.kilkariobd.domain.*;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.event.handler.OBDTargetFileHandler;
import org.motechproject.nms.kilkariobd.repository.ConfigurationDataService;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.inject.Inject;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OBDTargetFileHandlerIT extends BasePaxIT {

    @Inject
    private OutboundCallRequestService requestService;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private LanguageLocationCodeService llcService;

    @Inject
    private OutboundCallFlowService callFlowService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private OutboundCallDetailService callDetailService;

    @Inject
    private ContentUploadService contentUploadService;

    private SettingsFacade kilkariObdSettings;

    @Inject
    private MotechSchedulerService motechSchedulerService;

    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private CircleService circleService;

    @Inject
    private LanguageLocationCodeDataService llcCodeService;

    @Inject
    private SubscriberService subscriberService;

    @Inject
    private SubscriptionDataService subscriptionDataService;

    @Inject
    private ConfigurationDataService configurationDataService;

    private Settings settings;

    @Before
    public void preSetUp() {
        subscriptionDataService.deleteAll();
        subscriberService.deleteAll();
        callFlowService.deleteAll();
        requestService.deleteAll();
        llcCodeService.deleteAll();
        stateService.deleteAll();
        circleService.deleteAll();

        kilkariObdSettings = new SettingsFacade();
        List<Resource> configFiles = new ArrayList<>();
        configFiles.add(new ClassPathResource("kilkariobd.properties"));
        kilkariObdSettings.setConfigFiles(configFiles);
        settings = new Settings(kilkariObdSettings);
    }

    Logger logger = LoggerFactory.getLogger(OBDTargetFileHandlerIT.class);
    @Test
    public void testCsvFileForOutboundCallFlowRecordWillBeCreatedWhenPrepareOBDTargetEventIsRaised() {

        OBDTargetFileHandler obdFileHandler = new OBDTargetFileHandler(requestService, configurationService,
                llcService, callFlowService, subscriptionService, callDetailService, contentUploadService,
                kilkariObdSettings, motechSchedulerService);
        createNewConfiguration();
        OutboundCallBuilder callBuilder = new OutboundCallBuilder();
        String oldObdFileName = getCsvFileName(DateTime.now().minusDays(1).toDateMidnight().toDate());
        String freshObdFileName = getCsvFileName(DateTime.now().toDateMidnight().toDateTime().toDate());

        OutboundCallFlow oldCallFlow = callBuilder.buildCallFlow(oldObdFileName, null, null, null, null, null, null, null);
        callFlowService.create(oldCallFlow);
        createSubscription();
        obdFileHandler.prepareOBDTargetEventHandler();


        File file = new File(settings.getObdFileLocalPath() + "/" + freshObdFileName);
        Assert.assertTrue(file.exists());
    }

    private String getCsvFileName(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return "OBD_NMS_" + sdf.format(date) + ".csv";
    }

    private void createSubscription() {
        LocationBuilder locationBuilder = new LocationBuilder();
        ContentBuilder contentBuilder = new ContentBuilder();
        LanguageLocationCodeBuilder locationCodeBuilder = new LanguageLocationCodeBuilder();
        SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();

        /* Prepare location info for subscriber*/
        State state = locationBuilder.buildState(1L);
        District district = locationBuilder.buildDistrict(1L, 1L);
        Circle circle = locationCodeBuilder.buildCircle("29", "testName", "testCode");
        LanguageLocationCode llcCode = locationCodeBuilder.buildLLCCode(state, circle, district, "29");

        circleService.create(circle);

        state = stateService.create(state);
        state.getDistrict().add(district);
        stateService.update(state);

        //districtService.create(district);

        llcCodeService.create(llcCode);

        DateTime lmp = DateTime.now().minusMonths(3);
        Subscriber subscriber = subscriptionBuilder.buildSubscriber("7894561230", "29", state, district,
                BeneficiaryType.MOTHER, lmp);
        Long startDate = DateTime.now().toDateMidnight().getMillis();
        Subscription subscription = subscriptionBuilder.buildSubscription(
                "7894561230", Channel.IVR, Status.ACTIVE, startDate);

        subscriber = subscriberService.create(subscriber);
        subscription  = subscriptionDataService.create(subscription);
        subscription.setSubscriber(subscriber);
        subscriptionDataService.update(subscription);

        contentUploadService.create(contentBuilder.buildContent("29", "W1_1"));

    }

    private void createNewConfiguration() {
        configurationDataService.deleteAll();
        Configuration configuration = new Configuration();
        configuration.setIndex(1L);
        configuration.setFreshObdServiceId("1");
        configuration.setRetryDay1ObdServiceId("1");
        configuration.setRetryDay2ObdServiceId("1");
        configuration.setRetryDay3ObdServiceId("1");
        configuration.setFreshObdPriority(1);
        configuration.setRetryDay1ObdPriority(1);
        configuration.setRetryDay2ObdPriority(1);
        configuration.setRetryDay3ObdPriority(1);
        configuration.setObdFileServerIp("127.0.0.1");
        configuration.setObdFilePathOnServer("/usr/share/nms");
        configuration.setObdFileServerSshUsername("nms");
        configuration.setObdIvrUrl("http://10.10.10.10:8080/obdmanager");
        configuration.setObdCreationEventCronExpression("0 1 0 * * ?");
        configuration.setObdNotificationEventCronExpression("0 0 8 * * ?");
        configuration.setPurgeRecordsEventCronExpression("0 0 21 * * ?");
        configuration.setRetryIntervalForObdPreparationInMins(60);
        configuration.setMaxObdPreparationRetryCount(0);
        configurationService.createConfiguration(configuration);
    }
}
