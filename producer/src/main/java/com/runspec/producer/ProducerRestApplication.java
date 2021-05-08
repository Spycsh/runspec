package com.runspec.producer;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ProducerRestApplication extends Application {

    @Override
    public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        router.attach("/runningData", ProducerCollectRunningDataRestService.class );
        router.attach("/returnTripData", ProducerReturnTripDataRestService.class);
        router.attach("/hotSpot", ProducerHotSpotRestService.class);
        return router;
    }
}
