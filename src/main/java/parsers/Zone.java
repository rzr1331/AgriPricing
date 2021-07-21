package parsers;

import java.util.HashMap;
import java.util.Map;

public enum Zone {
    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west"), NORTH_EAST("north east"),
    CENTER("center"),;

    private static final Map<String, Zone> nameToZoneMap = new HashMap<>();

    static {
        for (Zone zone : Zone.values()) {
            nameToZoneMap.put(zone.getName(), zone);
        }
    }

    private final String name;

    Zone(String name) {
        this.name = name;
    }

    public static Zone getZone(String name) {
        if (name == null) {
            return null;
        }

        return nameToZoneMap.get(name.trim().toLowerCase());
    }

    public String getName() {
        return name;
    }
}
