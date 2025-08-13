package use_case.listTasks;

import entity.Task;
import entity.TaskID;
import entity.TaskInfo;
import entity.Priority;
import entity.CustomTag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Unit tests for ListTasksInteractor.
 * - uses an in-test in-memory DAO implementation that's defensive for null usernames
 * - verifies normal behavior, invalid inputs, and the exception case
 */
class ListTasksInteractorTest {

    private static final String USERNAME = "alice";

    // ---------- Helper in-test DAO used by many tests ----------
    static class InMemoryTaskDaoForTest implements TaskDataAccessInterface {
        // map username -> list of tasks
        private final Map<String, List<Task>> store = new ConcurrentHashMap<>();

        @Override
        public List<Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate) {
            // Defensive: return empty list for null/blank username or invalid dates
            if (username == null || username.isBlank() || startDate == null || endDate == null || endDate.isBefore(startDate)) {
                return Collections.emptyList();
            }

            List<Task> all = store.getOrDefault(username, Collections.emptyList());
            List<Task> out = new ArrayList<>();
            for (Task t : all) {
                LocalDate d = t.getTaskInfo().getStartDateTime().toLocalDate();
                if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                    out.add(t);
                }
            }
            return out;
        }

        @Override
        public void addTask(String username, Task task) {
            if (username == null) return;
            store.computeIfAbsent(username, _ -> new ArrayList<>()).add(task);
        }
    }

    // Simple presenter stub to capture presented tasks
    static class CapturingPresenter implements ListTasksOutputBoundary {
        List<Task> presented = null;
        @Override
        public void presentTasks(List<Task> tasks) {
            this.presented = tasks;
        }
    }

    // ---------- Helper to create simple Task objects ----------
    private Task makeTask(String id_str, String name, LocalDateTime start) {
        TaskInfo info = new TaskInfo();
        info.setCoreDetails(TaskID.from(UUID.fromString(id_str)), name, start, start.plusHours(1));
        info.setAdditionalDetails(Priority.LOW, new CustomTag("tag", "🏷"), null, "No");
        return new Task(info);
    }

    @Test
    void successfulListReturnsTasksInRange() {
        InMemoryTaskDaoForTest dao = new InMemoryTaskDaoForTest();
        CapturingPresenter presenter = new CapturingPresenter();
        ListTasksInteractor interactor = new ListTasksInteractor(dao, presenter);

        LocalDateTime now = LocalDateTime.of(2025, 8, 4, 10, 0);
        Task t1 = makeTask(UUID.randomUUID().toString(), "A", now);
        Task t2 = makeTask(UUID.randomUUID().toString(), "B", now.plusDays(1));

        dao.addTask(USERNAME, t1);
        dao.addTask(USERNAME, t2);

        LocalDate start = now.toLocalDate();
        LocalDate end = start.plusDays(1);

        interactor.listTasks(USERNAME, start, end);

        Assertions.assertNotNull(presenter.presented, "presented should not be null");
        Assertions.assertEquals(2, presenter.presented.size(), "should present both tasks");
        List<String> names = presenter.presented.stream().map(x -> x.getTaskInfo().getTaskName()).toList();
        Assertions.assertTrue(names.contains("A") && names.contains("B"));
    }

    @Test
    void invalidInputsProduceEmptyList() {
        InMemoryTaskDaoForTest dao = new InMemoryTaskDaoForTest();
        CapturingPresenter presenter = new CapturingPresenter();
        ListTasksInteractor interactor = new ListTasksInteractor(dao, presenter);

        LocalDate now = LocalDate.of(2025, 8, 4);

        interactor.listTasks("testUser", now, now);
        Assertions.assertNotNull(presenter.presented);
        Assertions.assertTrue(presenter.presented.isEmpty());

        // blank username -> empty
        interactor.listTasks("   ", now, now);
        Assertions.assertNotNull(presenter.presented);
        Assertions.assertTrue(presenter.presented.isEmpty());

        // invalid date range -> empty
        interactor.listTasks(USERNAME, now.plusDays(1), now);
        Assertions.assertNotNull(presenter.presented);
        Assertions.assertTrue(presenter.presented.isEmpty());
    }

    @Test
    void daoExceptionPropagates() {
        // Create a DAO that throws from getTasksByDateRange
        TaskDataAccessInterface throwingDao = new TaskDataAccessInterface() {
            @Override
            public List<Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate) {
                throw new RuntimeException("boom");
            }
            @Override
            public void addTask(String username, Task task) { /* unused */ }
        };

        CapturingPresenter presenter = new CapturingPresenter();
        ListTasksInteractor interactor = new ListTasksInteractor(throwingDao, presenter);

        LocalDate start = LocalDate.of(2025, 8, 4);
        LocalDate end = start.plusDays(1);

        // Expect the exception to propagate (test changed to reflect interactor behavior)
        Assertions.assertThrows(RuntimeException.class, () -> interactor.listTasks(USERNAME, start, end));
    }
}
