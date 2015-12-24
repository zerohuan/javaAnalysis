package com.concurrent.examples.vehicletraceker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yjh on 15-12-20.
 */
public class PublishingVehicleTracker {
    private final ConcurrentHashMap<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiedMap;

    public PublishingVehicleTracker(Map<String, SafePoint> m) {
        locations = new ConcurrentHashMap<>(m);
        unmodifiedMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiedMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        SafePoint loc = locations.get(id);
        if(loc == null)
            throw new IllegalArgumentException();
        loc.set(x, y);
    }
}
