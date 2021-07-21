package parsers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class DateUtils {


    public static final long SECOND = 1 * 1000;

    public static final long MINUTE = 60 * SECOND;

    public static final long HOUR = 60 * MINUTE;

    public static final long DAY = 24 * HOUR;

    @Nonnull
    public static String format(@Nonnull DateFormat formatter, @Nonnull Date date) {
        return formatter.format(date);
    }

    @Nonnull
    public static String format(@Nonnull DateFormat formatter, @Nonnull Long date) {
        return formatter.format(date);
    }

    @Nonnull
    public static String format(DateFormat formatter, Long date, String defaultValue) {
        try {
            return formatter.format(date);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    @Nonnull
    public static String format(DateFormat formatter, Date date, String defaultValue) {
        try {
            return formatter.format(date);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static Long parseToLong(@Nonnull String format, @Nonnull String date) {
        Date parsedDate = parse(format,date);
        return Optional.ofNullable(parsedDate).map(Date::getTime).orElse(null);
    }

    @Nullable
    public static Date parse(@Nonnull String format, @Nonnull String date) {
        return parse(format,date,null);
    }

    @Nullable
    public static Date parse(@Nonnull String format, @Nonnull String date, Date defaultValue) {
        if (StringUtils.isEmpty(date)) {
            return defaultValue;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        return parse(formatter, date, defaultValue);
    }

    @Nullable
    public static Date parse(@Nonnull DateFormat format, @Nonnull String date, Date defaultValue) {
        if (StringUtils.isEmpty(date)) {
            return defaultValue;
        }
        try {
            return format.parse(date);
        } catch (Exception e) {
            return defaultValue;
        }
    }


}
