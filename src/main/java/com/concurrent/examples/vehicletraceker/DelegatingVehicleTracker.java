package com.concurrent.examples.vehicletraceker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yjh on 15-12-20.
 */
public class DelegatingVehicleTracker {
    private final ConcurrentHashMap<String, FinalPoint> locations;
    private final Map<String, FinalPoint> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, FinalPoint> m) {
        locations = new ConcurrentHashMap<>(m);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    /**
     * 可以实时的反映最新信息，但存在不一致的可能
     * @return
     */
    public Map<String, FinalPoint> getLocations() {
        return unmodifiableMap;
    }

    public FinalPoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if(locations.replace(id, new FinalPoint(x, y)) == null)
            throw new IllegalArgumentException();
    }
}
