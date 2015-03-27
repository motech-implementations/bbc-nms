package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;

import java.util.Set;

public class LocationBuilder {
    public State buildState(Long stateCode, Set<District> districts) {
        State state = new State();
        state.setStateCode(stateCode);
        state.setName("testState");
        state.setDistrict(districts);
        return state;
    }

    public District buildDistrict(Long stateCode, Long districtCode) {
        District district = new District();
        district.setDistrictCode(districtCode);
        district.setStateCode(stateCode);
        district.setName("testDistrict");
        return district;
    }
}
