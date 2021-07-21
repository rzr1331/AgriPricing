package parsers;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by rohan on 23/09/16.
 */
public final class CsvUtils {

    private CsvUtils() {
        // No instance creation
    }

    public static <T> File build(String fileName, Collection<T> data, CsvConverter<T> converter)
        throws IOException {

        File file = new File(fileName);
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            file.createNewFile();
        }

        if (CollectionUtils.isEmpty(data)) {
            return file;
        }

        FileWriter fileWriter = new FileWriter(file);
        CSVWriter writer = new CSVWriter(fileWriter);

        for (T element : data) {
            try {
                String[] values = converter.convert(element);
                writer.writeNext(values);
            } catch (Exception e) {
                System.out.println("build : exception occurred while converting : {}" + element + e);
            }
        }

        writer.flush();
        writer.close();
        return file;
    }

    public static void addColumn(String[] row, int columnNo, Object content) {
        // because index in arrays start from zero
        if (row == null || columnNo >= row.length) {
            return;
        }

        if (content == null) {
            row[columnNo] = "";
        } else {
            row[columnNo] = (content.toString());
        }
    }
}
