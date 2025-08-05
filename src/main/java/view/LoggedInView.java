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
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.calendar.TaskClickListener;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.deleteTask.DeleteTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import data_access.LocationService;
import data_access.WeatherApiService;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.task.TaskBoxDependencies;
import interface_adapter.task.TaskViewModel;
import org.jetbrains.annotations.NotNull;
import use_case.deleteTask.DeleteTaskInteractor;
import use_case.MarkTaskComplete.MarkTaskCompleteInteractor;
import interface_adapter.markTaskComplete.MarkTaskCompletePresenter;


public class LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final AddTaskViewModel addTaskViewModel;
    private final ManageTagsViewModel manageTagsViewModel;
    private final EditTaskViewModel editTaskViewModel;
    private final ViewManagerModel viewManagerModel;
    private String username;
    private String email;
    private final JLabel usernameLabel;
    private final LogoutController logoutController;
    private final JPanel centerPanel;
    private final CalendarData calendarData;
    private Map<LocalDate, Map<String, Object>> weatherMap = null;
    private Map<LocalDate, List<Map<String, String>>> hourlyWeatherMap = null;
    private final SupabaseTagDataAccessObject tagDao;
    private final SupabaseTaskDataAccessObject taskDao;

    private ActionListener logoutAL;
    private ActionListener addTaskAL;
    private ActionListener manageTagsAL;

    private final TaskBoxDependencies taskBoxDependencies;

    public LoggedInView(LoggedInDependencies loggedInDependencies,
                        SupabaseTagDataAccessObject tagDao,
                        SupabaseTaskDataAccessObject taskDao,
                        AddTaskViewModel addTaskViewModel,
                        TaskBoxDependencies taskBoxDependencies,
                        ManageTagsViewModel manageTagsViewModel) throws IOException {
        this.taskBoxDependencies = taskBoxDependencies;
        this.viewManagerModel = taskBoxDependencies.viewManagerModel();
        this.editTaskViewModel = taskBoxDependencies.editTaskViewModel();
        taskBoxDependencies.markTaskCompleteViewModel().addPropertyChangeListener(this);
        taskBoxDependencies.deleteTaskViewModel().addPropertyChangeListener(this);
        taskBoxDependencies.editTaskViewModel().addPropertyChangeListener(this);

        this.loggedInViewModel = loggedInDependencies.loggedInViewModel();
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.logoutController = loggedInDependencies.logoutController();
        this.tagDao = tagDao;
        this.taskDao = taskDao;
        this.addTaskViewModel = addTaskViewModel;
        this.manageTagsViewModel = manageTagsViewModel;

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
            addTaskViewModel.setUsername(this.username);
            viewManagerModel.setState(AddTaskView.getViewName());
            viewManagerModel.firePropertyChanged();
        };

        manageTagsAL = e -> {
            manageTagsViewModel.setUsername(this.username);
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();
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
        // clear out the old view
        centerPanel.removeAll();

        // Task‐click handler: pops up TaskBox
        TaskClickListener taskClickListener = task -> {
            TaskViewModel vm  = new TaskViewModel(task);
            TaskBox     box   = getTaskBox(vm);
            JOptionPane.showMessageDialog(
                    this,
                    box,
                    "Task Details",
                    JOptionPane.PLAIN_MESSAGE
            );
        };

        // Build the header row with a menu button in the corner
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Corner menu button
        JButton menuBtn = new JButton("☰");
        menuBtn.setMargin(new Insets(0, 0, 0, 0));
        menuBtn.setPreferredSize(new Dimension(60, 0));  // matches hour‐column width
        menuBtn.addActionListener(e -> showSideMenu(menuBtn));
        headerRow.add(menuBtn, BorderLayout.WEST);

        // Day headers
        JPanel daysPanel = new JPanel(new GridLayout(1, 7));
        for (LocalDate date : calendarData.getWeekDates()) {
            daysPanel.add(HeaderCellFactory.makeHeader(date, weatherMap.get(date)));
        }
        headerRow.add(daysPanel, BorderLayout.CENTER);

        // Hour labels on the left (aligns with the grid’s hours)
        HourLabelPane hourLabelPane = new HourLabelPane(60);

        // The task grid pane in the center
        TaskGridPane bodyPane = new TaskGridPane(
                calendarData,    // so it knows the dates & grid geometry
                tasks,
                taskClickListener
        );

        // Assemble into one scrollable container
        JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.add(headerRow,      BorderLayout.NORTH);
        calendarContainer.add(hourLabelPane,  BorderLayout.WEST);
        calendarContainer.add(bodyPane,       BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(calendarContainer);
        scroll.setBorder(null);
        centerPanel.add(scroll, BorderLayout.CENTER);

        // Refresh
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void showSideMenu(Component invoker) {
        JPopupMenu sideMenu = new JPopupMenu();
        JMenuItem addTaskItem    = new JMenuItem("Add Task");
        JMenuItem manageTagsItem = new JMenuItem("Manage Tags");
        JMenuItem logoutItem     = new JMenuItem("Log Out");

        addTaskItem.addActionListener(addTaskAL);
        manageTagsItem.addActionListener(manageTagsAL);
        logoutItem.addActionListener(logoutAL);

        sideMenu.add(addTaskItem);
        sideMenu.add(manageTagsItem);
        sideMenu.add(logoutItem);

        sideMenu.show(invoker, 0, invoker.getHeight());
    }

    @NotNull
    private TaskBox getTaskBox(TaskViewModel taskViewModel) {
        MarkTaskCompletePresenter presenter = new MarkTaskCompletePresenter(
                taskBoxDependencies.markTaskCompleteViewModel(),
                taskViewModel
        );
        MarkTaskCompleteController markTaskCompleteController =
                new MarkTaskCompleteController(new MarkTaskCompleteInteractor(taskDao, presenter));
        markTaskCompleteController.setUsername(username);

        DeleteTaskPresenter deleteTaskPresenter = new DeleteTaskPresenter(
                taskBoxDependencies.deleteTaskViewModel(), taskViewModel
        );
        DeleteTaskController deleteTaskController = new DeleteTaskController(
                new DeleteTaskInteractor(taskDao, deleteTaskPresenter)
        );
        deleteTaskController.setUsername(username);

        return new TaskBox(
                taskViewModel,
                markTaskCompleteController,
                deleteTaskController,
                taskBoxDependencies.editTaskController(),
                taskBoxDependencies.viewManagerModel()
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        if ("state".equals(prop) && evt.getSource() == loggedInViewModel) {
            LoggedInState state = (LoggedInState) evt.getNewValue();
            this.username = state.getUsername();
            usernameLabel.setText("Signed in as: " + this.username);
            this.email = state.getEmail();
            // TODO: do smth with email (Kian)
            taskBoxDependencies.editTaskController().setUsername(this.username);
            editTaskViewModel.setUsername(this.username);
            reloadTasksForCurrentWeek();
            return;
        }

        if ("weekTasks".equals(prop)) {
            List<Task> tasks = loggedInViewModel.getState().getWeekTasks();
            rebuildCalendarWithTasks(tasks);
            return;
        }

        if ("taskAdded".equals(prop) ||
                "task completed".equals(prop) ||
                "task deleted".equals(prop) ||
                "task updated".equals(prop) ||
                evt.getSource() == this.taskBoxDependencies.markTaskCompleteViewModel()) {

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
