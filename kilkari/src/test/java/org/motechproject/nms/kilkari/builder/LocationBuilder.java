package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;

public class LocationBuilder {
    public State buildState() {
        State state = new State();
        state.setStateCode(1L);
        return state;
    }

    public District buildDistrict() {
        District district = new District();
        district.setDistrictCode(1L);
        district.setStateCode(1L);
        return district;
    }
}
