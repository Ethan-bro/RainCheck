package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import entity.Task;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.calendar.TaskClickListener;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTPresenter;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.deleteTask.DeleteTaskPresenter;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import data_access.LocationService;
import data_access.WeatherApiService;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.task.TaskViewModel;
import use_case.DeleteTask.DeleteTaskInteractor;
import use_case.createCustomTag.CCTInteractor;
import use_case.MarkTaskComplete.MarkTaskCompleteInteractor;
import interface_adapter.markTaskComplete.MarkTaskCompletePresenter;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;


public class LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final AddTaskViewModel addTaskViewModel;
    private final MarkTaskCompleteViewModel markTaskCompleteViewModel;
    private DeleteTaskViewModel deleteTaskViewModel;
    private final ViewManagerModel viewManagerModel;
    private String loggedInUsername;
    private final JLabel usernameLabel;
    private LogoutController logoutController;
    private final JPanel centerPanel;
    private final CalendarData calendarData;
    private Map<LocalDate, Map<String, Object>> weatherMap = null;
    private Map<LocalDate, List<Map<String, String>>> hourlyWeatherMap = null;
    private final SupabaseTagDataAccessObject tagDao;
    private final SupabaseTaskDataAccessObject taskDao;

    private ActionListener logoutAL;
    private ActionListener addTaskAL;
    private ActionListener manageTagsAL;

    private EditTaskController editTaskController;

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        MarkTaskCompleteViewModel markTaskCompleteViewModel,
                        LogoutController logoutController,
                        ViewManagerModel viewManagerModel,
                        SupabaseTagDataAccessObject tagDao,
                        SupabaseTaskDataAccessObject taskDao,
                        AddTaskViewModel addTaskViewModel,
                        DeleteTaskViewModel deleteTaskViewModel,
                        EditTaskController editTaskController) throws IOException {
        this.loggedInViewModel = loggedInViewModel;
        this.markTaskCompleteViewModel = markTaskCompleteViewModel;
        this.deleteTaskViewModel = deleteTaskViewModel;
        this.deleteTaskViewModel.addPropertyChangeListener(this);
        this.markTaskCompleteViewModel.addPropertyChangeListener(this);
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.logoutController = logoutController;
        this.tagDao = tagDao;
        this.taskDao = taskDao;
        this.addTaskViewModel = addTaskViewModel;
        this.editTaskController = editTaskController;

        setupActionListeners();

        setLayout(new BorderLayout());

        // --- Top Panel: Title + Username + Month-Year (no weather row here) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("RainCheck Calendar");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        usernameLabel = new JLabel();
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.PLAIN, 14f));

        LocalDate today = LocalDate.now();
        JLabel monthYearLabel = new JLabel(today.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + today.getYear());
        monthYearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthYearLabel.setFont(monthYearLabel.getFont().deriveFont(Font.ITALIC, 15f));

        topPanel.add(title);
        topPanel.add(usernameLabel);
        topPanel.add(monthYearLabel);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: Scrollable calendar with weather integrated in header ---
        this.centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        this.calendarData = new CalendarData();
        if (this.weatherMap == null) {
            this.weatherMap = getDailyWeatherMapForCalendarData(calendarData);
        }
        if (hourlyWeatherMap == null) {
            this.hourlyWeatherMap = getHourlyWeatherMapForCalendarData(calendarData);
        }

        // --- Bottom: Logout + Add Task Buttons ---
        JButton logoutButton = new JButton("Log Out");
        JButton addTaskButton = new JButton("+ Add Task");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Left side: Log Out button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(logoutButton);

        // Right side: + Add Task button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(addTaskButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button ActionListeners:
        logoutButton.addActionListener(logoutAL);
        addTaskButton.addActionListener(addTaskAL);

    }

    private void setupActionListeners() {

        logoutAL = e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            logoutController.execute(currentState.getUsername());
        };

        addTaskAL = e -> {
            addTaskViewModel.setUsername(this.loggedInUsername);
            viewManagerModel.setState(AddTaskView.getViewName());
            viewManagerModel.firePropertyChanged();
        };

        // TODO: Sean - wire to CCTUseCaseFactory.java
        manageTagsAL = e -> {
            CCTViewModel viewModel = new CCTViewModel();
            CCTPresenter presenter = new CCTPresenter(viewManagerModel, viewModel);
            CCTInteractor interactor = new CCTInteractor(tagDao, presenter);
            CCTController controller = new CCTController(interactor);

            new CCTView(viewModel, controller, loggedInViewModel);
        };

    }

    private Map<LocalDate, Map<String, Object>> getDailyWeatherMapForCalendarData(
            CalendarData calendarData) throws IOException {
        Map<LocalDate, Map<String, Object>> map = new HashMap<>();

        String city = LocationService.getUserCity(); // Fetching the user's current location (city e.g., Toronto)
        WeatherApiService weatherService = new WeatherApiService();

        // Fetch weather for the week once and store in a map
        for (LocalDate date : calendarData.getWeekDates()) {
            map.put(date, weatherService.getDailyWeather(city, date));
        }
         return map;
    }

    private Map<LocalDate, List<Map<String, String>>> getHourlyWeatherMapForCalendarData(
            CalendarData calendarData) throws IOException {
        Map<LocalDate, List<Map<String, String>>> map = new HashMap<>();

        String city = LocationService.getUserCity(); // Reuse location
        WeatherApiService weatherService = new WeatherApiService();

        for (LocalDate date : calendarData.getWeekDates()) {
            List<Map<String, String>> hourlyData = weatherService.getHourlyWeather(city, date, 0, 23);
            map.put(date, hourlyData);
        }

        return map;
    }

    private void rebuildCalendarWithTasks(List<Task> tasks) {
        centerPanel.removeAll();

        TaskClickListener taskClickListener = task -> {
            TaskViewModel taskViewModel = new TaskViewModel(task);

            MarkTaskCompletePresenter markTaskCompletePresenter = new MarkTaskCompletePresenter(
                    markTaskCompleteViewModel, taskViewModel);

            MarkTaskCompleteController markTaskCompleteController = new MarkTaskCompleteController(
                    new MarkTaskCompleteInteractor(taskDao, markTaskCompletePresenter));

            markTaskCompleteController.setUsername(loggedInUsername);

            DeleteTaskPresenter deleteTaskPresenter = new DeleteTaskPresenter(deleteTaskViewModel, taskViewModel);
            DeleteTaskController deleteTaskController = new DeleteTaskController(
                    new DeleteTaskInteractor(taskDao, deleteTaskPresenter));

            deleteTaskController.setUsername(loggedInUsername);

            TaskBox taskBox = new TaskBox(
                    taskViewModel,
                    markTaskCompleteController,
                    deleteTaskController,
                    editTaskController,
                    viewManagerModel
            );
            JOptionPane.showMessageDialog(this, taskBox, "Task Details",
                    JOptionPane.PLAIN_MESSAGE);
        };

        CalendarGrid grid = new CalendarGrid(
                calendarData,
                weatherMap,
                tasks,
                taskClickListener,
                addTaskAL,
                manageTagsAL,
                logoutAL
                );

        ScrollableCalendar scrollableCalendar = new ScrollableCalendar(grid);

        centerPanel.add(scrollableCalendar, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName()) && evt.getSource() == loggedInViewModel) {
            LoggedInState state = (LoggedInState) evt.getNewValue();
            this.loggedInUsername = state.getUsername();
            usernameLabel.setText("Signed in as: " + this.loggedInUsername);
            // TODO: Brad, this is how you get the email: 'state.getEmail()'
            reloadTasksForCurrentWeek();
        }
        else if ("weekTasks".equals(evt.getPropertyName())) {
            List<Task> tasks = loggedInViewModel.getState().getWeekTasks();
            rebuildCalendarWithTasks(tasks);
        } else if ("taskAdded".equals(evt.getPropertyName())) {
            reloadTasksForCurrentWeek();
        } else if (evt.getSource() == markTaskCompleteViewModel) {
            reloadTasksForCurrentWeek();
        } else if ("task completed".equals(evt.getPropertyName())) {
            reloadTasksForCurrentWeek();
        }
        else if ("task deleted".equals(evt.getPropertyName())) {
            reloadTasksForCurrentWeek();
        }
    }

    private void reloadTasksForCurrentWeek() {
        LocalDate startOfWeek = calendarData.getWeekDates().getFirst();
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        loggedInViewModel.loadTasksForWeek(startOfWeek, endOfWeek);
    }

    public static String getViewName() {
        return viewName;
    }
}
