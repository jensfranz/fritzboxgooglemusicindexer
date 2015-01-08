package jensfranz.de.fritzboxgooglemusicindexer.event;

public class LoggedInEvent {
    private final boolean success;
    private final String failureReason;

    public LoggedInEvent(boolean success) {
        this.success = success;
        this.failureReason = null;
    }

    public LoggedInEvent(final String failureReason) {
        this.success = false;
        this.failureReason = failureReason;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
