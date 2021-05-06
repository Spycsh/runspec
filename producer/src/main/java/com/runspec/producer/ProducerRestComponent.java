package com.runspec.producer;

import org.restlet.Component;

public class ProducerRestComponent extends Component {
    public ProducerRestComponent() {
        getDefaultHost().attach("/producer", new ProducerRestApplication());
    }
}
