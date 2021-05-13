package com.runspec.producer;

import com.runspec.producer.restService.ProducerCollectRunningDataRestService;
import com.runspec.producer.restService.ProducerHistoryTripDataRestService;
import com.runspec.producer.restService.ProducerHotSpotRestService;
import com.runspec.producer.restService.ProducerReturnTripDataRestService;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ProducerRestApplication extends Application {

    //assign route information
    @Override
    public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        router.attach("/runningData", ProducerCollectRunningDataRestService.class );
        router.attach("/returnTripData", ProducerReturnTripDataRestService.class);
        router.attach("/hotSpot", ProducerHotSpotRestService.class);
        router.attach("/returnHistoryTripData", ProducerHistoryTripDataRestService.class);
        return router;
    }
}
