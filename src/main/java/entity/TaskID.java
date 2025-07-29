package entity;

import java.util.Objects;
import java.util.UUID;

public final class TaskID {
    private final UUID value;

    private TaskID(UUID value) {
        this.value = Objects.requireNonNull(value, "TaskID must not be null");
    }

    public static TaskID random() {
        return new TaskID(UUID.randomUUID());
    }

    public static TaskID from(UUID existing){
        return new TaskID(existing);
    }

    public UUID getIDValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskID that)) return false;
        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
