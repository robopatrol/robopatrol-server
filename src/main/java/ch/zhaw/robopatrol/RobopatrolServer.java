package ch.zhaw.robopatrol;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;


public class RobopatrolServer extends ResourceConfig {

    public RobopatrolServer() {
        register(new MoxyJsonConfig().resolver()); // JSON

        packages("ch.zhaw.robopatrol.res");
        register(ExceptionListener.class);
        register(CORSFilter.class);
    }

    /** Log exceptions. */
    private static class ExceptionListener implements ApplicationEventListener {

        @Override
        public void onEvent(ApplicationEvent event) {
        }

        @Override
        public RequestEventListener onRequest(RequestEvent requestEvent) {
            return event -> {
                switch (event.getType()) {
                    case ON_EXCEPTION:
                        event.getException().printStackTrace();
                }
            };
        }
    }

}
