package entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for a task.
 */
public final class TaskID {
    private final UUID value;

    /**
     * Constructs a TaskID with the given UUID value.
     *
     * @param value the UUID value, must not be null
     */
    public TaskID(UUID value) {
        this.value = Objects.requireNonNull(value, "TaskID must not be null");
    }

    /**
     * Creates a TaskID from an existing UUID.
     *
     * @param existing the existing UUID
     * @return a TaskID instance wrapping the given UUID
     */
    public static TaskID from(UUID existing) {
        return new TaskID(existing);
    }

    /**
     * Gets the UUID value wrapped by this TaskID.
     * @return the UUID value
     */
    public UUID getIDValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        }
        else if (!(o instanceof TaskID that)) {
            result = false;
        }
        else {
            result = value.equals(that.value);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
