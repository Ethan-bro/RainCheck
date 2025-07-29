package interface_adapter.addTask;

import entity.TaskID;

import java.util.UUID;

public class UUIDGenerator implements TaskIDGenerator{

    @Override
    public TaskID generateTaskID() {
        return TaskID.from(UUID.randomUUID());
    }
}
