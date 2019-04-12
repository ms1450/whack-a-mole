package common;

public class WAMException extends Exception {
    /**
     * Convenience constructor to create a new {@link WAMException}
     * with an error message.
     *
     * @param message The error message associated with the exception.
     */
    public WAMException(String message) {
        super(message);
    }

    /**
     * Convenience constructor to create a new {@link WAMException}
     * as a result of some other exception.
     *
     * @param cause The root cause of the exception.
     */
    public WAMException(Throwable cause) {
        super(cause);
    }

    /**
     * * Convenience constructor to create a new {@link WAMException}
     * as a result of some other exception.
     *
     * @param message The message associated with the exception.
     * @param cause   The root cause of the exception.
     */
    public WAMException(String message, Throwable cause) {
        super(message, cause);
    }
}
