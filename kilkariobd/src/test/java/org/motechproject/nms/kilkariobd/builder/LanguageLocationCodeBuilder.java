package org.motechproject.nms.kilkariobd.builder;

import org.motechproject.nms.masterdata.domain.*;

public class LanguageLocationCodeBuilder {
    public LanguageLocationCode buildLLCCode(
            Long stateCode, Long districtCode, String code, String circleCode) {
        LanguageLocationCode llcCode = new LanguageLocationCode();
        llcCode.setStateCode(stateCode);
        llcCode.setDistrictCode(districtCode);
        llcCode.setLanguageLocationCode(code);
        llcCode.setCircleCode(circleCode);
        return llcCode;
    }

    public String getLLCcodeByStateDistrict(Long stateCode, Long districtCode, String code) {
        return buildLLCCode(stateCode, districtCode, code, null).getLanguageLocationCode();
    }

    public String getLLCCodeByCircleCode(String circleCode, String llcCode) {
        return buildLLCCode(null, null, llcCode, circleCode).getLanguageLocationCode();
    }

    public Circle buildCircle (String defaultLlcCode, String code, String name) {
        Circle circle = new Circle();
        circle.setDefaultLanguageLocationCode(defaultLlcCode);
        circle.setCode(code);
        circle.setName(name);
        return circle;
    }

    public Operator buildOperator(String code,String name) {
        Operator operator = new Operator();
        operator.setCode(code);
        operator.setName(name);
        return operator;
    }

    public String getDefaultLLCCodeByCircleCode(String defaultLlcCode, String circleCode) {
        return buildCircle(defaultLlcCode, circleCode, "test").getDefaultLanguageLocationCode();
    }

    public LanguageLocationCode buildLLCCode(State state, Circle circle, District district, String code) {
        LanguageLocationCode llcCode = new LanguageLocationCode();
        llcCode.setStateCode(state.getStateCode());
        llcCode.setDistrictCode(district.getDistrictCode());
        llcCode.setCircleCode(circle.getCode());
        llcCode.setLanguageKK("LanguageKK");
        llcCode.setLanguageMA("LanguageMA");
        llcCode.setLanguageMK("LanguageMK");
        llcCode.setLanguageLocationCode(code);
        llcCode.setState(state);
        llcCode.setDistrict(district);
        llcCode.setCircle(circle);
        return llcCode;
    }
}
