package interface_adapter.addTask;

import entity.TaskID;

/**
 * Interface for generating unique TaskID instances.
 */
public interface TaskIDGenerator {

    /**
     * Generates a new unique TaskID.
     *
     * @return a newly generated TaskID
     */
    TaskID generateTaskID();
}
