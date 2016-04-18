package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.res.Schedule;
import ch.zhaw.robopatrol.res.Waypoint;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by lneiva on 11/04/16.
 */
public class RobopatrolServer extends ResourceConfig {


    public RobopatrolServer() {
        packages("ch.zhaw.robopatrol.res");
        registerClasses(Schedule.class, Waypoint.class);
    }

}
