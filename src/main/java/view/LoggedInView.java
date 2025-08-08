package view;

import data_access.LocationService;
import data_access.SupabaseTaskDataAccessObject;
import data_access.WeatherApiService;

import entity.Task;

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
import interface_adapter.manageTags.ManageTagsViewModel;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.markTaskComplete.MarkTaskCompletePresenter;
import interface_adapter.task.TaskBoxDependencies;
import interface_adapter.task.TaskViewModel;

import tools.EnsureEmailConfigForUser;

import use_case.deleteTask.DeleteTaskInteractor;
import use_case.markTaskComplete.MarkTaskCompleteInteractor;
import use_case.notification.NotificationDataAccessInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.jetbrains.annotations.NotNull;

public class LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "logged in";

    private static final float TITLE_FONT_SIZE = 18f;
    private static final float USERNAME_FONT_SIZE = 14f;
    private static final float MONTH_YEAR_FONT_SIZE = 15f;
    private static final int MENU_BUTTON_WIDTH = 60;
    private static final int HOUR_LABEL_WIDTH = 60;

    private final LoggedInViewModel loggedInViewModel;
    private final AddTaskViewModel addTaskViewModel;
    private final ManageTagsViewModel manageTagsViewModel;
    private final EditTaskViewModel editTaskViewModel;
    private final ViewManagerModel viewManagerModel;
    private String username;
    private String email;
    private JLabel usernameLabel;
    private final LogoutController logoutController;
    private final JPanel centerPanel;
    private final CalendarData calendarData;
    private Map<LocalDate, Map<String, Object>> weatherMap;
    private Map<LocalDate, List<Map<String, String>>> hourlyWeatherMap;
    private final SupabaseTaskDataAccessObject taskDao;

    private ActionListener logoutListener;
    private ActionListener addTaskListener;
    private ActionListener manageTagsListener;

    private final TaskBoxDependencies taskBoxDependencies;
    private final NotificationDataAccessInterface notificationDataAccess;

    public LoggedInView(
            LoggedInDependencies loggedInDependencies,
            SupabaseTaskDataAccessObject taskDao,
            AddTaskViewModel addTaskViewModel,
            TaskBoxDependencies taskBoxDependencies,
            ManageTagsViewModel manageTagsViewModel,
            NotificationDataAccessInterface notificationDataAccess) throws IOException {

        this.taskBoxDependencies = taskBoxDependencies;
        this.viewManagerModel = taskBoxDependencies.viewManagerModel();
        this.editTaskViewModel = taskBoxDependencies.editTaskViewModel();
        taskBoxDependencies.markTaskCompleteViewModel().addPropertyChangeListener(this);
        taskBoxDependencies.deleteTaskViewModel().addPropertyChangeListener(this);
        taskBoxDependencies.editTaskViewModel().addPropertyChangeListener(this);

        this.loggedInViewModel = loggedInDependencies.loggedInViewModel();
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.logoutController = loggedInDependencies.logoutController();
        this.taskDao = taskDao;
        this.addTaskViewModel = addTaskViewModel;
        this.manageTagsViewModel = manageTagsViewModel;

        this.notificationDataAccess = notificationDataAccess;

        setupActionListeners();

        setLayout(new BorderLayout());

        initTopPanel();

        this.centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        this.calendarData = new CalendarData();

        if (this.weatherMap == null) {
            this.weatherMap = getDailyWeatherMap(calendarData);
        }
        if (this.hourlyWeatherMap == null) {
            this.hourlyWeatherMap = getHourlyWeatherMap(calendarData);
        }

        initBottomPanel();
    }

    private void initTopPanel() {
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("RainCheck Calendar");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));

        usernameLabel = new JLabel();
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.PLAIN, USERNAME_FONT_SIZE));

        final LocalDate today = LocalDate.now();
        final JLabel monthYearLabel = new JLabel(
                today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + today.getYear());
        monthYearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthYearLabel.setFont(monthYearLabel.getFont().deriveFont(Font.ITALIC, MONTH_YEAR_FONT_SIZE));

        topPanel.add(title);
        topPanel.add(usernameLabel);
        topPanel.add(monthYearLabel);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initBottomPanel() {
        final JButton logoutButton = new JButton("Log Out");
        final JButton addTaskButton = new JButton("+ Add Task");

        final JPanel bottomPanel = new JPanel(new BorderLayout());

        final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(logoutButton);

        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(addTaskButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(logoutListener);
        addTaskButton.addActionListener(addTaskListener);
    }

    private void setupActionListeners() {
        logoutListener = event -> {
            logoutController.execute(this.username);
        };

        addTaskListener = event -> {
            viewManagerModel.setState(AddTaskView.getViewName());
            viewManagerModel.firePropertyChanged();
        };

        manageTagsListener = event -> {
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();
        };
    }

    private Map<LocalDate, Map<String, Object>> getDailyWeatherMap(final CalendarData calendar) throws IOException {
        final Map<LocalDate, Map<String, Object>> map = new HashMap<>();

        final String city = LocationService.getUserCity();
        final WeatherApiService weatherService = new WeatherApiService();

        for (LocalDate date : calendar.getWeekDates()) {
            map.put(date, weatherService.getDailyWeather(city, date));
        }

        return map;
    }

    private Map<LocalDate, List<Map<String, String>>> getHourlyWeatherMap(CalendarData calendar) throws IOException {
        final Map<LocalDate, List<Map<String, String>>> map = new HashMap<>();

        final String city = LocationService.getUserCity();
        final WeatherApiService weatherService = new WeatherApiService();

        for (LocalDate date : calendar.getWeekDates()) {
            final List<Map<String, String>> hourlyData = weatherService.getHourlyWeather(
                    city,
                    date,
                    0,
                    23
            );
            map.put(date, hourlyData);
        }

        return map;
    }

    private void rebuildCalendarWithTasks(final List<Task> tasks) {
        centerPanel.removeAll();

        final TaskClickListener taskClickListener = task -> {
            final TaskViewModel vm = new TaskViewModel(task);
            final TaskBox box = getTaskBox(vm);
            JOptionPane.showMessageDialog(this, box, "Task Details", JOptionPane.PLAIN_MESSAGE);
        };

        final JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        final JButton menuBtn = new JButton("☰");
        menuBtn.setMargin(new Insets(0, 0, 0, 0));
        menuBtn.setPreferredSize(new Dimension(MENU_BUTTON_WIDTH, 0));
        menuBtn.addActionListener(event -> showSideMenu(menuBtn));
        headerRow.add(menuBtn, BorderLayout.WEST);

        final JPanel daysPanel = new JPanel(new java.awt.GridLayout(1, 7));
        for (LocalDate date : calendarData.getWeekDates()) {
            daysPanel.add(HeaderCellFactory.makeHeader(date, weatherMap.get(date)));
        }
        headerRow.add(daysPanel, BorderLayout.CENTER);

        final HourLabelPane hourLabelPane = new HourLabelPane(HOUR_LABEL_WIDTH);

        final TaskGridPane bodyPane = new TaskGridPane(calendarData, tasks, taskClickListener);

        final JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.add(headerRow, BorderLayout.NORTH);
        calendarContainer.add(hourLabelPane, BorderLayout.WEST);
        calendarContainer.add(bodyPane, BorderLayout.CENTER);

        final JScrollPane scroll = new JScrollPane(calendarContainer);
        scroll.setBorder(null);
        centerPanel.add(scroll, BorderLayout.CENTER);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void showSideMenu(final Component invoker) {
        final JPopupMenu sideMenu = new JPopupMenu();
        final JMenuItem addTaskItem = new JMenuItem("Add Task");
        final JMenuItem manageTagsItem = new JMenuItem("Manage Tags");
        final JMenuItem logoutItem = new JMenuItem("Log Out");

        addTaskItem.addActionListener(addTaskListener);
        manageTagsItem.addActionListener(manageTagsListener);
        logoutItem.addActionListener(logoutListener);

        sideMenu.add(addTaskItem);
        sideMenu.add(manageTagsItem);
        sideMenu.add(logoutItem);

        sideMenu.show(invoker, 0, invoker.getHeight());
    }

    @NotNull
    private TaskBox getTaskBox(final TaskViewModel taskViewModel) {
        final MarkTaskCompletePresenter presenter = new MarkTaskCompletePresenter(
                taskBoxDependencies.markTaskCompleteViewModel(),
                taskViewModel
        );
        final MarkTaskCompleteController markTaskCompleteController = new MarkTaskCompleteController(
                new MarkTaskCompleteInteractor(taskDao, presenter)
        );
        markTaskCompleteController.setUsername(username);

        final DeleteTaskPresenter deleteTaskPresenter = new DeleteTaskPresenter(
                taskBoxDependencies.deleteTaskViewModel(), taskViewModel
        );
        final DeleteTaskController deleteTaskController = new DeleteTaskController(
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
    public void propertyChange(final PropertyChangeEvent evt) {
        final String prop = evt.getPropertyName();

        if ("state".equals(prop) && evt.getSource() == loggedInViewModel) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();

            this.username = state.getUsername();
            usernameLabel.setText("Signed in as: " + this.username);

            this.email = state.getEmail();
            EnsureEmailConfigForUser.ensureEmailConfigForUser(notificationDataAccess, this.username);

            setViewModelsUsername(this.username);
            reloadTasksForCurrentWeek();
        }
        else if ("weekTasks".equals(prop)) {
            final List<Task> tasks = loggedInViewModel.getState().getWeekTasks();
            rebuildCalendarWithTasks(tasks);
        }
        else if ("taskAdded".equals(prop)
                || "task completed".equals(prop)
                || "task deleted".equals(prop)
                || "task updated".equals(prop)
                || evt.getSource() == this.taskBoxDependencies.markTaskCompleteViewModel()) {

            reloadTasksForCurrentWeek();
        }
    }

    private void setViewModelsUsername(final String name) {
        taskBoxDependencies.editTaskController().setUsername(name);
        addTaskViewModel.setUsername(name);
        editTaskViewModel.setUsername(name);
        manageTagsViewModel.setUsername(name);
    }

    private void reloadTasksForCurrentWeek() {
        final LocalDate startOfWeek = calendarData.getWeekDates().getFirst();
        final LocalDate endOfWeek = startOfWeek.plusDays(6);
        loggedInViewModel.loadTasksForWeek(startOfWeek, endOfWeek);
    }

    public static String getViewName() {
        return VIEW_NAME;
    }
}
