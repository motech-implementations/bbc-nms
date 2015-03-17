package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.masterdata.domain.State;

public class LocationBuilder {
    public State buildState() {
        State state = new State();
        state.setStateCode(1L);
        return state;
    }
}
