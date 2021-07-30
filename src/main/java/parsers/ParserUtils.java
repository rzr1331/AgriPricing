package parsers;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

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

    public static boolean equalsIgnoreCaseAndIngnoreSpace(@Nonnull String first,
        @Nonnull String second) {
        return first.replaceAll(" ", "").
            equalsIgnoreCase(second.replaceAll(" ", ""));
    }

    public static Optional<Integer> valueOfString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            return Optional.of(Integer.valueOf(str));
        } catch (Exception e) {
            //log.error("error while pricing [{}]", str);
        }
        return Optional.empty();
    }
}
