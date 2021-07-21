package parsers;

import java.util.HashMap;
import java.util.Map;

public enum Region {

    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west"), NORTH_EAST("north east");

    private static final Map<String, Region> nameToRegionMap = new HashMap<>();

    static {
        for (Region zone : Region.values()) {
            nameToRegionMap.put(zone.getName(), zone);
        }
    }

    private final String name;

    Region(String name) {
        this.name = name;
    }

    public static Region getRegion(String name) {
        if (name == null) {
            return null;
        }

        return nameToRegionMap.get(name.trim().toLowerCase());
    }

    public String getName() {
        return name;
    }
}
