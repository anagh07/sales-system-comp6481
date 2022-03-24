public class DuplicateRecordException extends Exception {
    public DuplicateRecordException(String message) {
        super(message);
    }

    public DuplicateRecordException() {
        this("Error: duplicate record!");
    }
}
