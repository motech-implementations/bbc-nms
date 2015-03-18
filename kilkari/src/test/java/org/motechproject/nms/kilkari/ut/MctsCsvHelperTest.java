package org.motechproject.nms.kilkari.ut;

import org.junit.Test;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.event.handler.MctsCsvHelper;
import org.motechproject.nms.masterdata.domain.State;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MctsCsvHelperTest {

    @Test
    public void shouldPopulateSubscriptionWithStatusDeactivated() {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn("987654321");
        State state = new State();
        state.setStateCode(1L);
        subscriber.setState(state);
        subscriber.setModifiedBy("modifierName");
        subscriber.setCreator("Creator");
        subscriber.setOwner("Owner");

        Subscription dbSubscription = new Subscription();

        MctsCsvHelper mctsCsvHelper = new MctsCsvHelper();

        Method method = null;
        try {
            method = mctsCsvHelper.getClass().getDeclaredMethod("populateDbSubscription", Subscriber.class, Subscription.class, boolean.class);
            method.setAccessible(true);
            method.invoke(mctsCsvHelper, subscriber, dbSubscription, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
