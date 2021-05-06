package com.runspec.producer;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ProducerRestApplication extends Application {

    @Override
    public Restlet createInboundRoot(){
        Router router = new Router(getContext());
        router.attach("/runningData", ProducerRestService.class );
        return router;
    }
}
