package interface_adapter.addTask;

import entity.TaskID;

import java.util.UUID;

public class UuidGenerator implements TaskIDGenerator {

    /**
     * Generates a new unique TaskID using a random UUID.
     *
     * @return a new TaskID instance
     */
    @Override
    public TaskID generateTaskID() {
        return TaskID.from(UUID.randomUUID());
    }
}
