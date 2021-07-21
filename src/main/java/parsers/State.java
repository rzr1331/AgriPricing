package parsers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public enum State {

    ANDHRA_PRADESH("Andhra Pradesh", "AP", "AP",Zone.SOUTH, Region.SOUTH,37),
    ARUNACHAL_PRADESH("Arunachal Pradesh", "AR", "AR",Zone.NORTH_EAST, Region.NORTH_EAST,12),
    ASSAM("Assam", "AS","AS", Zone.NORTH_EAST, Region.NORTH_EAST,18),
    BIHAR("Bihar", "BR","BR", Zone.EAST, Region.EAST,10),
    CHHATTISGARH("Chhattisgarh", "CT","CG", Zone.EAST, Region.EAST,22),
    DELHI("Delhi", "DL","DL", Zone.NORTH, Region.NORTH,7),
    GOA("Goa", "GA","GA", Zone.WEST, Region.WEST,30),
    GUJARAT("Gujarat", "GJ","GJ", Zone.WEST, Region.WEST,24),
    HARYANA("Haryana", "HR","HR", Zone.NORTH, Region.NORTH,6),
    HIMACHAL_PRADESH("Himachal Pradesh", "HP", "HP", Zone.NORTH, Region.NORTH,2),
    JAMMU_AND_KASHMIR("Jammu and Kashmir", "JK","JK", Zone.NORTH, Region.NORTH,1),
    JHARKHAND("Jharkhand", "JH","JH", Zone.CENTER, Region.EAST,20),
    KARNATAKA("Karnataka", "KA","KA", Zone.SOUTH, Region.SOUTH,29),
    KERALA("Kerala", "KL","KL", Zone.SOUTH, Region.SOUTH,32),
    MADHYA_PRADESH("Madhya Pradesh", "MP","MP", Zone.CENTER, Region.EAST,23),
    MAHARASHTRA("Maharashtra", "MH","MH", Zone.WEST, Region.WEST,27),
    MANIPUR("Manipur", "MN","MN", Zone.NORTH_EAST, Region.NORTH_EAST,14),
    MEGHALAYA("Meghalaya", "ML","ML", Zone.NORTH_EAST, Region.NORTH_EAST,17),
    MIZORAM("Mizoram", "MZ","MZ", Zone.NORTH_EAST, Region.NORTH_EAST,15),
    NAGALAND("Nagaland", "NL","NL", Zone.NORTH_EAST, Region.NORTH_EAST,13),
    ODISHA("Odisha", "OR","OR", Zone.SOUTH, Region.SOUTH,21),
    PUNJAB("Punjab", "PB","PJ", Zone.NORTH, Region.NORTH,3),
    RAJASTHAN("Rajasthan", "RJ","JP", Zone.NORTH, Region.NORTH,8),
    SIKKIM("Sikkim", "SK","SK", Zone.NORTH_EAST, Region.NORTH_EAST,11),
    TAMIL_NADU("Tamil Nadu", "TN","TN", Zone.SOUTH, Region.SOUTH,33),
    TELANGANA("Telangana", "TG","TS", Zone.SOUTH, Region.SOUTH,36),
    TRIPURA("Tripura", "TR","TR", Zone.NORTH_EAST, Region.NORTH_EAST,16),
    UTTARAKHAND("Uttarakhand", "UT","UT", Zone.NORTH, Region.NORTH,5),
    UTTAR_PRADESH("Uttar Pradesh", "UP","UP", Zone.NORTH, Region.NORTH,9),
    WEST_BENGAL("West Bengal", "WB","WB", Zone.EAST, Region.EAST,19),
    CHANDIGARH("Chandigarh", "CH","CH", Zone.NORTH, Region.NORTH,4),
    PUDUCHERRY("Puducherry", "PY","PY", Zone.SOUTH, Region.SOUTH,34),
    ANDAMAN_AND_NICOBAR_ISLANDS("Andaman and Nicobar Islands", "AN","AN", Zone.SOUTH, Region.SOUTH,35),
    DADRA_AND_NAGAR_HAVELI("Dadra and Nagar Haveli", "DH","DH", Zone.WEST, Region.WEST,26),
    DAMAN_AND_DIU("Daman and Diu", "DD","DD", Zone.WEST, Region.WEST,25),
    LAKSHADWEEP("Lakshadweep", "LD","LD", Zone.EAST, Region.SOUTH,31),
    ALL("All", "ALL","ALL", null, null,0),
    UNKNOWN("Unknown", "","", null, null, -1);

    private final String displayName;
    private final String abbreviation;
    private final String invoiceAbbreviation;
    private final Zone zone;
    private Region region;
    private int stateCode;
    private static final Map<String, State> STATES_BY_ABBR = new HashMap<String, State>();

    private static final List<State> ACTIVE_STATES = new ArrayList<>();

    private static final LinkedHashSet<String> STATES_ABBR_IN_ORDER = new LinkedHashSet<>();

    /* static initializer */
    static {
        for (State state : values()) {
            if (!state.equals(UNKNOWN)) {
                STATES_ABBR_IN_ORDER.add(state.getAbbreviation());
            }
            STATES_BY_ABBR.put(state.getAbbreviation(), state);
        }

        ACTIVE_STATES.add(ANDHRA_PRADESH);
        ACTIVE_STATES.add(DELHI);
        ACTIVE_STATES.add(GUJARAT);
        ACTIVE_STATES.add(HARYANA);
        ACTIVE_STATES.add(JHARKHAND);
        ACTIVE_STATES.add(KARNATAKA);
        ACTIVE_STATES.add(MAHARASHTRA);
        ACTIVE_STATES.add(MADHYA_PRADESH);
        ACTIVE_STATES.add(ODISHA);
        ACTIVE_STATES.add(RAJASTHAN);
        ACTIVE_STATES.add(TAMIL_NADU);
        ACTIVE_STATES.add(TELANGANA);
        ACTIVE_STATES.add(UTTAR_PRADESH);
        ACTIVE_STATES.add(UTTARAKHAND);
        ACTIVE_STATES.add(WEST_BENGAL);
        ACTIVE_STATES.add(ALL);
    }

    State(String displayName, String abbreviation, String invoiceAbbreviation, Zone zone, Region region, int stateCode) {
        this.displayName = displayName;
        this.abbreviation = abbreviation;
        this.invoiceAbbreviation = invoiceAbbreviation;
        this.zone = zone;
        this.region = region;
        this.stateCode = stateCode;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getInvoiceAbbreviation() {
        return invoiceAbbreviation;
    }

    public Zone getZone() {
        return zone;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public static State valueOfAbbreviation(final String abbr) {
        final State state = STATES_BY_ABBR.get(abbr);
        if (state != null) {
            return state;
        } else {
            return UNKNOWN;
        }
    }

    public static State valueOfName(final String name) {
        if (StringUtils.isBlank(name)) {
            return UNKNOWN;
        }
        final String enumName = name.replaceAll(" ", "_").replaceAll("&", "and").toUpperCase();
        try {
            return valueOf(enumName);
        } catch (final IllegalArgumentException e) {
            System.out.println("Cannot find state : " + e.getMessage());
            return State.UNKNOWN;
        }
    }

    public static State valueByNameOrAbbreviation(final String stateString) {
        State stateByName = State.valueOfName(stateString);
        if (!UNKNOWN.equals(stateByName)) {
            return stateByName;
        }
        return valueOfAbbreviation(stateString);
    }

    public static String[] getStateAbbrNamesInOrder() {
        return STATES_ABBR_IN_ORDER.toArray(new String[STATES_ABBR_IN_ORDER.size()]);
    }

    public static List<State> getActiveStates() {
        return ACTIVE_STATES;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public String getDisplayName() {
        return displayName;
    }

    public static boolean isValidState(String stateString) {
        State state = State.valueByNameOrAbbreviation(stateString);
        return isValidState(state);
    }

    public static boolean isValidState(State state) {
        return !State.UNKNOWN.equals(state) && !State.ALL.equals(state);
    }

    public static Comparator<State> comparatorByName = new Comparator<State>() {
        @Override public int compare(State o1, State o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }
    };
}
