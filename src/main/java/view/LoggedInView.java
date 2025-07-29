package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import entity.Task;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.TaskClickListener;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import data_access.LocationService;
import data_access.WeatherApiService;

public class LoggedInView extends JPanel implements PropertyChangeListener {

    private static final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final JLabel usernameLabel;
    private LogoutController logoutController;
    private final JPanel centerPanel;
    private final CalendarData calendarData;
    private final Map<LocalDate, Map<String, Object>> weatherMap;

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        LogoutController logoutController,
                        ViewManagerModel viewManagerModel) throws IOException {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.logoutController = logoutController;

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
        JLabel monthYearLabel = new JLabel(today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + today.getYear());
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
        this.weatherMap = getWeatherMapForCalendarData(calendarData);

        LocalDate startDate = this.calendarData.getWeekDates().getFirst();
        LocalDate endDate = startDate.plusDays(7);
        loggedInViewModel.loadTasksForWeek(startDate,endDate);

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
        logoutButton.addActionListener(e -> {
            if (e.getSource().equals(logoutButton)) {
                final LoggedInState currentState = loggedInViewModel.getState();

                logoutController.execute(
                        currentState.getUsername());
            }
        });

        addTaskButton.addActionListener(e -> {
            viewManagerModel.setState(AddTaskView.getViewName());
            viewManagerModel.firePropertyChanged();
        });

    }

    private Map<LocalDate, Map<String, Object>> getWeatherMapForCalendarData(CalendarData calendarData) throws IOException {
        Map<LocalDate, Map<String, Object>> map = new HashMap<>();

        String city = LocationService.getUserCity(); // Fetching the user's current location (city e.g., Toronto)
        WeatherApiService weatherService = new WeatherApiService();

        // Fetch weather for the week once and store in a map
        for (LocalDate date : calendarData.getWeekDates()) {
            map.put(date, weatherService.getDailyWeather(city, date));
        }
         return map;
    }

    private void rebuildCalendarWithTasks(List<Task> tasks) {
        centerPanel.removeAll();

        System.out.println("RECEIVED TASKS:");
        for (Task task : tasks) {
            System.out.println(task.getTaskInfo().getTaskName());
        }

        TaskClickListener clicker = task -> {
//            TaskViewModel taskViewModel = new TaskViewModel(task);
//            TaskController taskController = new TaskController(TaskInteractor);
//            TaskBox taskBox = new TaskBox(taskViewModel, taskController);
//            JOptionPane.showMessageDialog(this, box, "Task Details",
//                    JOptionPane.PLAIN_MESSAGE);
        };

        CalendarGrid grid = new CalendarGrid(calendarData, weatherMap, tasks, clicker);
        ScrollableCalendar scrollableCalendar = new ScrollableCalendar(grid);

        centerPanel.add(scrollableCalendar, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            LoggedInState state = (LoggedInState) evt.getNewValue();
            usernameLabel.setText("Signed in as: " + state.getUsername());
        }
        else if ("weekTasks".equals(evt.getPropertyName())) {
            List<Task> tasks = loggedInViewModel.getState().getWeekTasks();
            rebuildCalendarWithTasks(tasks);
        }
    }

    public static String getViewName() {
        return viewName;
    }
}
