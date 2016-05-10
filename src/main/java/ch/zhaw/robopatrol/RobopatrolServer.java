package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.store.RobopatrolStoreProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;


public class RobopatrolServer extends ResourceConfig {

    public RobopatrolServer() {
        register(EventListener.class);
        register(CORSFilter.class);
        register(new MoxyJsonConfig().resolver()); // JSON

        register(new DependencyInjection());
        packages("ch.zhaw.robopatrol.res");
    }

    private static class DependencyInjection extends AbstractBinder {

        @Override
        protected void configure() {
            bind(new RobopatrolStoreProvider()).to(RobopatrolStoreProvider.class);
        }

    }

    /** Log exceptions. */
    private static class EventListener implements ApplicationEventListener {

        @Override
        public void onEvent(ApplicationEvent event) { }

        @Override
        public RequestEventListener onRequest(RequestEvent requestEvent) {
            return event -> {
                switch (event.getType()) {
                    case ON_EXCEPTION: event.getException().printStackTrace();
                }
            };
        }

    }

}

