package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;

public class LanguageLocationCodeBuilder {
    public LanguageLocationCode buildLLCCode(Long stateCode, Long districtCode, Integer code, String circleCode) {
        LanguageLocationCode llcCode = new LanguageLocationCode();
        llcCode.setStateCode(stateCode);
        llcCode.setDistrictCode(districtCode);
        llcCode.setLanguageLocationCode(code);
        llcCode.setCircleCode(circleCode);
        return llcCode;
    }

    public Integer getLLCcodeByStateDistrict(Long stateCode, Long districtCode, Integer code) {
        return buildLLCCode(stateCode, districtCode, code, null).getLanguageLocationCode();
    }

    public Integer getLLCCodeByCircleCode(String circleCode, Integer llcCode) {
        return buildLLCCode(null, null, llcCode, circleCode).getLanguageLocationCode();
    }

    public Circle buildCircle (Integer defaultLlcCode, String code) {
        Circle circle = new Circle();
        circle.setDefaultLanguageLocationCode(defaultLlcCode);
        circle.setCode(code);
        return circle;
    }

    public Operator buildOperator(String code,String name) {
        Operator operator = new Operator();
        operator.setCode(code);
        operator.setName(name);
        return operator;
    }

    public Integer getDefaultLLCCodeByCircleCode(Integer defaultLlcCode, String circleCode) {
        return buildCircle(defaultLlcCode, circleCode).getDefaultLanguageLocationCode();
    }
}
