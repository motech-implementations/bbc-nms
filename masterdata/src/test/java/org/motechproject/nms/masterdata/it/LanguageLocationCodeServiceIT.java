package org.motechproject.nms.masterdata.it;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.masterdata.service.impl.LanguageLocationCodeServiceImpl;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class LanguageLocationCodeServiceIT extends BasePaxIT {

    @Inject
    private LanguageLocationCodeDataService languageLocationCodeDataService;

    @Inject
    private CircleService circleService;

    @Inject
    private StateService stateService;

    @Inject
    private DistrictRecordsDataService districtService;

    @Inject
    private CircleDataService circleDataService;

    @Before
    public void setUp() {
        languageLocationCodeDataService.deleteAll();
        districtService.deleteAll();
        stateService.deleteAll();
        circleDataService.deleteAll();
    }

    @Test
    public void ShouldReturnLanguageLocationCodeForValidStateCodeAndDistrictCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);
        preSetUp();
        Assert.assertNotNull(llcImpl.getLanguageLocationCodeByLocationCode(1L, 1L));
    }

    @Test
    public void ShouldReturnNullForInValidStateCodeAndDistrictCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);
        preSetUp();
        Assert.assertNull(llcImpl.getLanguageLocationCodeByLocationCode(2L, 2L));
    }

    @Test
    public void ShouldReturnLanguageLocationCodeForValidCircleCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);
        preSetUp();
        Assert.assertTrue(llcImpl.getDefaultLanguageLocationCodeByCircleCode("testCode") == 123);
    }

    @Test
    public void ShouldReturnNullForInValidCircleCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);
        preSetUp();
        Assert.assertNull(llcImpl.getDefaultLanguageLocationCodeByCircleCode("xyz"));
    }

    @Test
    public void ShouldReturnLanguageLocationCodeIfUniqueForValidCircleCodeAndLanguageLocationCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);

        preSetUp();
        Assert.assertTrue(llcImpl.getLanguageLocationCodeByCircleCode("testCode") == 123);
    }

    @Test
    public void ShouldReturnNullIfNotUniqueForValidCircleCodeAndLanguageLocationCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);

        preSetUp();
        Assert.assertNull(llcImpl.getLanguageLocationCodeByCircleCode("validCode"));
    }

    @Test
    public void ShouldReturnNullForInValidCircleCodeByGetLanguageLocationCodeByCircleCode() {
        LanguageLocationCodeServiceImpl llcImpl = new LanguageLocationCodeServiceImpl(
                languageLocationCodeDataService, circleService);

        preSetUp();
        Assert.assertNull(llcImpl.getLanguageLocationCodeByCircleCode("xyz"));
    }



    public void preSetUp() {
        //create circle with code "testCode"
        Circle circle = new Circle();
        circle.setName("testCircle");
        circle.setCode("testCode");
        circle.setDefaultLanguageLocationCode(123);
        circleService.create(circle);

        //create circle with code "validCode"
        Circle circle2 = new Circle();
        circle2.setName("testCircle");
        circle2.setCode("validCode");
        circle2.setDefaultLanguageLocationCode(321);
        circleService.create(circle2);

        //create State with statecode "1"
        State state = new State();
        state.setName("testState");
        state.setStateCode(1L);
        stateService.create(state);


        //create district with districtCode "1" and stateCode "1"
        District district = new District();
        district.setStateCode(1L);
        district.setName("testDistrict");
        district.setDistrictCode(1L);
        district.setStateCode(1L);
        districtService.create(district);

        //create LanguageLocationCodeCsv record with circleCode "testCode",
        // districtCode "1" and stateCode "1"
        LanguageLocationCode record = new LanguageLocationCode();
        record.setCircleCode("testCode");
        record.setDistrictCode(1L);
        record.setStateCode(1L);
        record.setLanguageKK("LanguageKK");
        record.setLanguageMA("LanguageMA");
        record.setLanguageMK("LanguageMK");
        record.setLanguageLocationCode(123);
        record.setCircle(circle);
        record.setDistrict(district);
        record.setState(state);
        languageLocationCodeDataService.create(record);

        LanguageLocationCode record2 = new LanguageLocationCode();
        record2.setCircleCode("testCode");
        record2.setDistrictCode(1L);
        record2.setStateCode(1L);
        record2.setLanguageKK("LanguageKK");
        record2.setLanguageMA("LanguageMA");
        record2.setLanguageMK("LanguageMK");
        record2.setLanguageLocationCode(123);
        record2.setCircle(circle);
        record2.setDistrict(district);
        record2.setState(state);
        languageLocationCodeDataService.create(record2);

        LanguageLocationCode record3 = new LanguageLocationCode();
        record3.setCircleCode("validCode");
        record3.setDistrictCode(1L);
        record3.setStateCode(1L);
        record3.setLanguageKK("LanguageKK");
        record3.setLanguageMA("LanguageMA");
        record3.setLanguageMK("LanguageMK");
        record3.setLanguageLocationCode(321);
        record3.setCircle(circle2);
        record3.setDistrict(district);
        record3.setState(state);
        languageLocationCodeDataService.create(record3);

        LanguageLocationCode record4 = new LanguageLocationCode();
        record4.setCircleCode("validCode");
        record4.setDistrictCode(1L);
        record4.setStateCode(1L);
        record4.setLanguageKK("LanguageKK");
        record4.setLanguageMA("LanguageMA");
        record4.setLanguageMK("LanguageMK");
        record4.setLanguageLocationCode(12345);
        record4.setCircle(circle2);
        record4.setDistrict(district);
        record4.setState(state);
        languageLocationCodeDataService.create(record4);
    }
}
