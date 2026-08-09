package use_case;

/**
 * Unchecked exception thrown by data access implementations when an operation fails,
 * wrapping SQLiteException for instance. Interactors catch this specific
 * type rather than the RuntimeException generic type.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
