package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;

public class SubscriptionBuilder {
    Subscriber subscriber = new Subscriber();

    public Subscriber buildSubscriber(Object... args) {
        subscriber.setName("motechUser");
        subscriber.setMsisdn((String) args[0]);
        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        subscriber.setLanguageLocationCode((Integer) args[1]);
        return subscriber;
    }

    public Subscription buildSubscription(String msisdn) {
        Subscription subscription = new Subscription();
        subscription.setMsisdn(msisdn);
        return subscription;
    }

//    public SubscriptionPack buildSubscriptionPack(String name, Integer duration, Integer NumOfMsg, Integer startWeek) {
////        SubscriptionPack pack = new SubscriptionPack("");
////        pack.setName(name);
////        return pack;
////    }
//    }
}
