package parsers;

public interface CsvConverter<T> {
    String[] convert(T item);
}
