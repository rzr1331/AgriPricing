package parsers;

public class ParserUtils {


    public static Double parseRawStringToDouble(String rawValue) {

        if (rawValue == null || rawValue.length() == 0) {
            return null;
        }
        String value = rawValue.toLowerCase().replaceAll("\\s+|/-|-|,|rs.|rs|inr|:", "").trim();

        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            System.out.println("unblabe to parse {}" + value);
        }
        return null;
    }


}
