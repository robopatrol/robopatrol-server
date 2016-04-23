package ch.zhaw.robopatrol.store;

import java.io.Serializable;
import java.util.UUID;

public interface Entity extends Serializable {

    public String getId();

    public void setId(String string);

    public default void generateId() {
        setId(UUID.randomUUID().toString());
    }

}
