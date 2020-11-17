package sample;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class Controller {

    @FXML
    private Pane taskDisplayArea;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane calendarPane;

    @FXML
    private Button createTaskButton;

    @FXML
    private Label dayNumberLabelId;
    @FXML
    private Label SUNLabel;
    @FXML
    private Label MONLabel;
    @FXML
    private Label TUELabel;
    @FXML
    private Label WEDLabel;
    @FXML
    private Label THULabel;
    @FXML
    private Label FRILabel;
    @FXML
    private Label SATLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private TextField taskField;
    @FXML
    private TextArea taskTextArea;

    @FXML
    private Button janButton;
    @FXML
    private Button febButton;
    @FXML
    private Button marButton;
    @FXML
    private Button aprButton;
    @FXML
    private Button mayButton;
    @FXML
    private Button junButton;
    @FXML
    private Button julButton;
    @FXML
    private Button augButton;
    @FXML
    private Button sepButton;
    @FXML
    private Button octButton;
    @FXML
    private Button novButton;
    @FXML
    private Button decButton;

    private Button currentMonth;

    private Button currentDay;

    // Button to change years up and down
    @FXML
    private Button yearButton;
    @FXML
    private Label yearLabel;

    private int yearSelection, monthSelection, daySelection;
    private int startingYear, startingMonth, startingDay;

    private final String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private final HashMap<String, Integer> monthsOfYear = new HashMap<String, Integer>();

    private final int[] daysInMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private Button[] monthButtons = new Button[12];
    private Label[] weekdayLabels = new Label[7];

    private int addTaskButtonClickCount;

    private String colorOfDays, colorOfNotDays, colorOfWeekdays, colorOfMonths, colorSelectedMonth, colorOfStartingDay;

    private String notSelectedStylesheet, selectedStylesheet, startingDayStylesheet;

    private PseudoClass taskDot = PseudoClass.getPseudoClass("task-dot");

    public Controller() {

    }

    @FXML
    private void initialize() {
        //Had to be instantiated in here or else it was null
        monthButtons = new Button[] {janButton, febButton, marButton, aprButton, mayButton, junButton, julButton,
                augButton, sepButton, octButton, novButton, decButton};
        //Did it here because monthButtons had to as well
        weekdayLabels = new Label[] {SUNLabel, MONLabel, TUELabel, WEDLabel, THULabel, FRILabel, SATLabel};
        //Universal colors for between dark mode and light mode
        colorSelectedMonth = "#171717";
        colorOfMonths = "#868686";

        colorOfDays = "#959595";
        colorOfNotDays = "#ccc";

        colorOfWeekdays = "#737373";

        colorOfStartingDay = "white";

        notSelectedStylesheet = "dayButton";
        selectedStylesheet = "dayButtonSelected";
        startingDayStylesheet = "startingDay";

        // Gets the Calendar
        Calendar calendar = Calendar.getInstance();

        startingYear = calendar.get(Calendar.YEAR);
        startingMonth = calendar.get(Calendar.MONTH);
        startingDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Gets and displays the current date
        String todayNumber = Integer.toString(calendar.get(Calendar.DATE));
        String todayFullName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayNumberLabelId.setText(todayNumber);
        dayLabel.setText(todayFullName.toUpperCase());

        displayNewMonth(calendar, false);
        updateCurrentDayDisplay(currentDay);

        currentMonth = monthButtons[monthSelection];

        currentMonth.setTextFill(Paint.valueOf(("#171717")));
        currentMonth.setStyle("-fx-background-color: transparent; -fx-font-weight: Bold; -fx-cursor: hand;");

        monthsOfYear.put("Jan", 0); monthsOfYear.put("Feb", 1); monthsOfYear.put("Mar", 2);
        monthsOfYear.put("Apr", 3); monthsOfYear.put("May", 4); monthsOfYear.put("Jun", 5);
        monthsOfYear.put("Jul", 6); monthsOfYear.put("Aug", 7); monthsOfYear.put("Sep", 8);
        monthsOfYear.put("Oct", 9); monthsOfYear.put("Nov", 10); monthsOfYear.put("Dec", 11);

        updateTaskList();

        for(Label weekday : weekdayLabels) {
            weekday.setStyle("-fx-font-weight: bold;");
        }

//        // Gets current month, january starts at 0
//        int currentMonth = calendar.get(Calendar.MONTH);
//        System.out.println("month: " + currentMonth);
//        int currentYear = calendar.get(Calendar.YEAR);
//        System.out.println("year: " + currentYear);
//        // Getting first of the month
//        YearMonth yearMonthObject = YearMonth.of(currentYear, currentMonth + 1);
//        int daysInMonth = yearMonthObject.lengthOfMonth();
//        System.out.println(daysInMonth);
//        System.out.println("First day of the month is:" + yearMonthObject.atDay(1).getDayOfWeek());

//        monthsOfYear.put("Jan", 1); monthsOfYear.put("Feb", 2); monthsOfYear.put("Mar", 3);
//        monthsOfYear.put("Apr", 4); monthsOfYear.put("May", 5); monthsOfYear.put("Jun", 6);
//        monthsOfYear.put("Jul", 7); monthsOfYear.put("Aug", 8); monthsOfYear.put("Sep", 9);
//        monthsOfYear.put("Oct", 10); monthsOfYear.put("Nov", 11); monthsOfYear.put("Dec", 12);
        addTaskButtonClickCount = 0;

        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONObject modeObj = (JSONObject) jsonArray.get(1);
            changeModeInitialize((String) modeObj.get("mode"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        updateTaskDots();
    }

    // Updates year when
    public void updateYear(ActionEvent event){
        yearButton = (Button) event.getSource();
        String yearDirection = yearButton.getText();
        if (yearDirection.equals("yearUp")){
            // Increment year up one
            updateYearAndDisplay(1);
        } else {
            // Increment year down one
            updateYearAndDisplay(-1);
        }
        Calendar cal = Calendar.getInstance();                      //Get calendar instance
        cal.set(Calendar.MONTH, monthSelection);                         //Set calendar to current month
        cal.set(Calendar.YEAR, yearSelection);                        //Set calendar to current year
        displayNewMonth(cal, false);               //Update viewable calendar
        updateCurrentDayDisplay(currentDay);
        updateTaskList();
        updateTaskDots();
    }

    public void updateYearAndDisplay(int shiftDirection) {
        yearSelection = Integer.parseInt(yearLabel.getText());
        yearSelection += shiftDirection;
        yearLabel.setText(String.valueOf(yearSelection));
    }

    public void clickedMonths(ActionEvent event) {
        Button monthButton = (Button) event.getSource();
        if (monthButton != currentMonth) {
            newSelectedMonth(monthButton, currentMonth);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthsOfYear.get(currentMonth.getText()));
            cal.set(Calendar.YEAR, yearSelection);
            displayNewMonth(cal, false);
            updateCurrentDayDisplay(currentDay);
            updateTaskList();
            updateTaskDots();
        }
    }

    public void newSelectedMonth(Button clicked, Button old) {
        old.setTextFill(Paint.valueOf(colorOfMonths));                                      //Set old to grey
        old.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");     //Make non-bold
        clicked.setTextFill(Paint.valueOf((colorSelectedMonth)));                                //Set new to dark grey
        clicked.setStyle("-fx-background-color: transparent; -fx-font-weight: Bold; -fx-cursor: hand;");   //Make bold
        currentMonth = clicked;                                                         //Point to new month
    }

    public void pressButton(ActionEvent event) {
        //Get button
        Button clickedButton = (Button) event.getSource();
        //If button is in this month
        if(clickedButton.getTextFill().equals(Paint.valueOf(colorOfDays))
                || clickedButton.getTextFill().equals(Paint.valueOf(colorOfStartingDay))) {
            //Set new date and weekday
            updateCurrentDayDisplay(clickedButton);
            changeCalendarDaySelection(clickedButton);
        }
        else {
            //If day clicked is in previous month
            if(GridPane.getRowIndex(clickedButton) == 0) {
                updateCurrentDayDisplay(clickedButton);                                   //Update current day display
                shiftCalendar(-1);                                      //shift and update calendar
            }
            //If day clicked is in next month
            else {
                updateCurrentDayDisplay(clickedButton);                                   //Update current day display
                shiftCalendar(1);                                   //shift calendar and calendar
            }
            updateTaskDots();
        }
        updateTaskList();
    }

    public void shiftCalendar(int shiftDirection) {
        boolean changedYearFromGrayButtons = false;
        Button newMonth;
        Calendar calendar = Calendar.getInstance();                                                 //Get a calendar instance
        calendar.set(Calendar.MONTH, monthSelection + shiftDirection);
        calendar.set(Calendar.YEAR, yearSelection);                                 //Set month to previous month of current month
        if(monthsOfYear.get(currentMonth.getText()) == 0 && shiftDirection == -1) {                 //If month is january and shifting to previous month
            newMonth = monthButtons[11];                                                            //December
            updateYearAndDisplay(shiftDirection);                                                      //Update Year
            changedYearFromGrayButtons = true;
        }
        else if(monthsOfYear.get(currentMonth.getText()) == 11 && shiftDirection == 1) {            //If month is december and shifting to next month
            newMonth = monthButtons[0];                                                             //January
            updateYearAndDisplay(shiftDirection);                                                      //Update Year
            changedYearFromGrayButtons = true;
        }
        else {
            newMonth = monthButtons[(monthsOfYear.get(currentMonth.getText()) + shiftDirection)];  //Shift calendar forward or backward
        }
        newSelectedMonth(newMonth, currentMonth);
        displayNewMonth(calendar, changedYearFromGrayButtons);                                                                   //Update calendar
    }

    public void displayNewMonth(Calendar calendar, Boolean changedYearFromGrayButtons) {
        monthSelection = calendar.get(Calendar.MONTH);
        if (!changedYearFromGrayButtons) {
            yearSelection = calendar.get(Calendar.YEAR);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //Stores value of previous month for indexing
        int previousMonth;

        int lastMonthCount;
        int thisMonthCount = 1;
        int nextMonthCount = 1;

        int startOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if(startOfMonth == 0) startOfMonth += 7;
        if(monthSelection == 0) {
            previousMonth = 11;
        }
        else {
            previousMonth = monthSelection - 1;
        }

        //Stores values of number of days in month
        //needed for leap years
        int previousMonthDays = checkIfLeapYear(previousMonth);
        int thisMonthDays = checkIfLeapYear(monthSelection);

        lastMonthCount = previousMonthDays - (startOfMonth - 1);

        for (Node node : gridPane.getChildren()) {
            AnchorPane a = (AnchorPane) node;
            for (Node innerNode : a.getChildren()) {
                if (innerNode instanceof Button) {
                    Button current = (Button) innerNode;
                    current.getStyleClass().removeAll("startingDay", "startingDayDark");
                    current.getStyleClass().add(notSelectedStylesheet);
                    if (lastMonthCount <= previousMonthDays) {
                        current.setText(String.valueOf(lastMonthCount));
                        current.setTextFill(Paint.valueOf(colorOfNotDays));
                        lastMonthCount++;
                    } else if (thisMonthCount <= thisMonthDays) {
                        current.setText(String.valueOf(thisMonthCount));
                        current.setTextFill(Paint.valueOf(colorOfDays));
                        if (thisMonthCount == startingDay && monthSelection == startingMonth
                                && yearSelection == startingYear) {
                            highlightStartingDay(current);
                        }
                        if (thisMonthCount == daySelection)
                            changeCalendarDaySelection(current);
                        thisMonthCount++;
                    } else {
                        current.setText(String.valueOf(nextMonthCount));
                        current.setTextFill(Paint.valueOf(colorOfNotDays));
                        nextMonthCount++;
                    }
                }
            }
        }
    }

    public void updateCurrentDayDisplay(Button clickedButton) {
        try {
            String clickedButtonDay = clickedButton.getText();                        //Get current button number as string
            dayNumberLabelId.setText(clickedButtonDay);                                 //Set current day label with number
            daySelection = Integer.parseInt(clickedButtonDay);                   //Save value to update new month selection with proper day

            String todayFullName = daysOfWeek[GridPane.getColumnIndex(clickedButton)];  //Get weekday name
            dayLabel.setText(todayFullName.toUpperCase());                              //Set it to full uppercase
        }
        catch (NumberFormatException ignored) {
        }
    }

    private void highlightStartingDay(Button current) {
        if(currentDay == null) currentDay = current;
        current.getStyleClass().removeAll(selectedStylesheet, notSelectedStylesheet,
                "startingDay", "startingDayDark");
        current.getStyleClass().add(startingDayStylesheet);
        current.setTextFill(Paint.valueOf(colorOfStartingDay));
    }

    public void changeCalendarDaySelection(Button clickedButton) {
        currentDay.getStyleClass().removeAll(selectedStylesheet);
        currentDay.getStyleClass().add(notSelectedStylesheet);
        currentDay = clickedButton;
        currentDay.getStyleClass().removeAll(notSelectedStylesheet);
        currentDay.getStyleClass().add(selectedStylesheet);
    }

    public int checkIfLeapYear(int month) {
        if(month == 1) {
            if(((yearSelection % 4 == 0) && (yearSelection % 100!= 0)) || (yearSelection %400 == 0)) {
                return daysInMonth[month] + 1;
            }
        }
        return daysInMonth[month];
    }

    /**
     * Adds a task to the current month/day selected by user. Updates a pre-existing object with a task if one
     * exists already, otherwise creates a new entry in the database with the user's inputted task
     */
    public void addEvent(ActionEvent event) {
        if (taskField.getText().length() > 0) {
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader("src\\sample\\db.json")) {
                // Get JSONArray of days with tasks
                JSONArray jsonArray = (JSONArray) parser.parse(reader);
                JSONArray taskJsonArray = (JSONArray) ((JSONObject) jsonArray.get(0)).get("allTasks");

                /* Search JSON database. If a day with tasks already exists, add the users inputted task to it.
                If it does not exist, create a new JSON object and add it to the database */
                boolean objFound = false;
                for (Object obj : taskJsonArray) {
                    JSONObject jsonObj = (JSONObject) obj;
                    // Exists in database if its year/month/day match the user's selected month/day
                    if ((long) jsonObj.get("year") == yearSelection &&
                            jsonObj.get("month").equals(currentMonth.getText()) &&
                            jsonObj.get("day").equals(dayNumberLabelId.getText())) {
                        objFound = true;
                        // Get that days pre-existing list of tasks and add user's inputted task
                        JSONArray taskArray = (JSONArray) jsonObj.get("tasks");
                        taskArray.add(taskField.getText());
                        // Write to JSON file to update database
                        try (FileWriter file = new FileWriter("src\\sample\\db.json")) {
                            file.write(jsonArray.toJSONString());
                            file.flush();
                        }
                        // Update app's left hand column with new task list that includes user's input
                        StringBuilder listOfTasks = new StringBuilder();
                        for (Object task : taskArray) {
                            listOfTasks.append("\n-    ").append((String) task);
                        }
                        taskTextArea.setText("Current Tasks:" + listOfTasks);
                    }
                }

                // If the user's selected month/day does not exist, create a new entry in database
                if (!objFound) {
                    // Create year/month/day attributes
                    JSONObject newTaskObj = new JSONObject();
                    newTaskObj.put("year", yearSelection);
                    newTaskObj.put("month", currentMonth.getText());
                    newTaskObj.put("day", dayNumberLabelId.getText());
                    // Initialize an array of tasks with the user's task input
                    JSONArray tasks = new JSONArray();
                    tasks.add(taskField.getText());
                    newTaskObj.put("tasks", tasks);
                    // Add this newly created JSON object to database and write to file
                    taskJsonArray.add(newTaskObj);
                    try (FileWriter file = new FileWriter("src\\sample\\db.json")) {
                        file.write(jsonArray.toJSONString());
                        file.flush();
                    }
                    // Update app's left hand column with new task list that includes user's input
                    StringBuilder listOfTasks = new StringBuilder();
                    for (Object task : tasks) {
                        listOfTasks.append("\n-    ").append((String) task);
                    }
                    taskTextArea.setText("Current Tasks:" + listOfTasks);
                }
            }
            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            taskField.clear();
            updateTaskDots();
        }
    }

    /**
     * Searches through JSON database and checks if an object exists with the current day selected.
     * Updates the text area with tasks if object exists, otherwise sets text to default
     * no tasks exist yet message
     */
    public void updateTaskList() {
        JSONParser parser = new JSONParser();
//        try (Reader reader = new FileReader("simp\\src\\sample\\db.json")) {
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            // Get JSONArray of days with tasks
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONArray taskJsonArray = (JSONArray) ((JSONObject) jsonArray.get(0)).get("allTasks");

            // Iterate through JSON objects (existing days with tasks) in database
            boolean dayClickedFound = false;
            for (Object obj : taskJsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                // Exists in database if its month/day match the user's selected month/day
                if (jsonObj.get("month").equals(currentMonth.getText()) && ((long) jsonObj.get("year") == yearSelection)
                        && jsonObj.get("day").equals(dayNumberLabelId.getText())) {
                    dayClickedFound = true;
                    // Get that object's list of tasks
                    JSONArray taskArray = (JSONArray) jsonObj.get("tasks");
                    // Update app's left hand column with that days current list of tasks
                    StringBuilder listOfTasks = new StringBuilder();
                    for (Object task : taskArray) {
                        listOfTasks.append("\n-    ").append((String) task);
                    }
                    taskTextArea.setText("Current Tasks:" + listOfTasks);
                    break;
                }
            }
            // If the user selects a day with no tasks, display default "no tasks" message
            if (!dayClickedFound) {
                taskTextArea.setText("Current Tasks:" + "\n-    There are no tasks");
            }
        }
        catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void changeToLight() {
        //*********** Set Universals ****************//
        colorSelectedMonth = "#171717";
        colorOfMonths = "#868686";

        colorOfDays = "#959595";
        colorOfNotDays = "#ccc";

        colorOfWeekdays = "#737373";

        colorOfStartingDay = "white";

        notSelectedStylesheet = "dayButton";
        selectedStylesheet = "dayButtonSelected";
        startingDayStylesheet = "startingDay";
        //************ End Universal Set **************//

        taskDisplayArea.setStyle("-fx-background-color: rgba(53,89,119,0.8)");

        dayNumberLabelId.setTextFill(Paint.valueOf("white"));
        dayLabel.setTextFill(Paint.valueOf("white"));
        taskTextArea.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: transparent; -fx-prompt-text-fill: #FFF; -fx-text-inner-color: #FFF;");
        taskField.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: transparent transparent #FFF transparent;" +
                "-fx-text-inner-color: #FFF; -fx-prompt-text-fill: #C0C0C0;");
        createTaskButton.setStyle("-fx-background-color: #FFF; -fx-cursor: hand");

        calendarPane.setStyle("-fx-background-color: #FFF;");

        for(Button month: monthButtons) {
            if(month.equals(currentMonth)) {
                month.setTextFill(Paint.valueOf(colorSelectedMonth));
            }
            else {
                month.setTextFill(Paint.valueOf(colorOfMonths));
            }
        }

        //change weekday colors
        for (Label weekday : weekdayLabels) {
            weekday.setTextFill(Paint.valueOf(colorOfWeekdays));
        }

        for (Node node : gridPane.getChildren()) {
            AnchorPane a = (AnchorPane) node;
            for (Node innerNode : a.getChildren()) {
                if (innerNode instanceof Button) {
                    Button current = (Button) innerNode;
                    if (current.getTextFill().equals(Paint.valueOf("#373737"))) {
                        highlightStartingDay(current);
                    } else {
                        if (current.getTextFill().equals(Paint.valueOf("#4c4c4c"))) {
                            current.setTextFill(Paint.valueOf(colorOfNotDays));
                        } else {
                            current.setTextFill(Paint.valueOf(colorOfDays));
                        }
                        current.getStyleClass().removeAll("dayButtonDark", "dayButtonDarkSelected");
                        if (current.equals(currentDay)) current.getStyleClass().add("dayButtonSelected");
                        current.getStyleClass().add("dayButton");
                    }
                }
            }
        }
    }

    public void changeToDark() {
        //*********** Set Universals ****************//
        colorSelectedMonth = "#e8e8e8";
        colorOfMonths = "#797979";

        colorOfDays = "#6a6a6a";
        colorOfNotDays = "#4c4c4c";

        colorOfWeekdays = "#8c8c8c";

        colorOfStartingDay = "#373737";

        notSelectedStylesheet = "dayButtonDark";
        selectedStylesheet = "dayButtonDarkSelected";
        startingDayStylesheet = "startingDayDark";
        //************ End Universal Set **************//

        //change left pane background
        taskDisplayArea.setStyle("-fx-background-color: rgba(202,166,136,0.8)");

        //change left pane colors
        dayNumberLabelId.setTextFill(Paint.valueOf("#373737"));
        dayLabel.setTextFill(Paint.valueOf("#373737"));
        taskTextArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;" +
                "-fx-prompt-text-fill: #373737; -fx-text-inner-color: #373737;");
        taskField.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: transparent transparent #373737 transparent;" +
                "-fx-text-inner-color: #373737; -fx-prompt-text-fill: #505050;");
        createTaskButton.setStyle("-fx-background-color: #373737; -fx-cursor: hand");

        //change right pane background
        calendarPane.setStyle("-fx-background-color: #373737;");

        //change month colors
        for(Button month: monthButtons) {
            if(month.equals(currentMonth)) {
                month.setTextFill(Paint.valueOf(colorSelectedMonth));
            }
            else {
                month.setTextFill(Paint.valueOf(colorOfMonths));
            }
        }

        //change weekday colors
        for (Label weekday : weekdayLabels) {
            weekday.setTextFill(Paint.valueOf(colorOfWeekdays));
        }

        //change day colors
        for (Node node : gridPane.getChildren()) {
            AnchorPane a = (AnchorPane) node;
            for (Node innerNode : a.getChildren()) {
                if (innerNode instanceof Button) {
                    Button current = (Button) innerNode;
                    if (current.getTextFill().equals(Paint.valueOf("#ffffff"))) {
                        highlightStartingDay(current);
                    } else {
                        if (current.getTextFill().equals(Paint.valueOf("#ccc"))) {
                            current.setTextFill(Paint.valueOf(colorOfNotDays));
                        } else {
                            current.setTextFill(Paint.valueOf(colorOfDays));
                        }
                        current.getStyleClass().removeAll("dayButton", "dayButtonSelected");
                        if (current.equals(currentDay)) current.getStyleClass().add("dayButtonDarkSelected");
                        current.getStyleClass().add("dayButtonDark");
                    }
                }
            }
        }
    }

    @FXML
    public void changeMode(ActionEvent actionEvent) {
        Button modeButton = (Button) actionEvent.getSource();
        if(modeButton.getText().compareTo("Dark Mode") == 0) {
            modeButton.setStyle("-fx-background-color: #f8f8f8; -fx-cursor: hand;");
            modeButton.setTextFill(Paint.valueOf("#1d1d1d"));
            modeButton.setText("Light Mode");
            changeToDark();
        }
        else {
            modeButton.setStyle("-fx-background-color: #1d1d1d; -fx-cursor: hand;");
            modeButton.setTextFill(Paint.valueOf("#f8f8f8"));
            modeButton.setText("Dark Mode");
            changeToLight();
        }
        // Update user's desired mode in JSON database
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONObject modeObj = (JSONObject) jsonArray.get(1);
            if (modeButton.getText().compareTo("Dark Mode") == 0) {
                modeObj.put("mode", "light");
            }
            else {
                modeObj.put("mode", "dark");
            }
            try (FileWriter file = new FileWriter("src\\sample\\db.json")) {
                file.write(jsonArray.toJSONString());
                file.flush();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    Button theModeButton;
    public void changeModeInitialize(String mode) {
        if(mode.equals("dark")) {
            theModeButton.setStyle("-fx-background-color: #f8f8f8; -fx-cursor: hand");
            theModeButton.setTextFill(Paint.valueOf("#1d1d1d"));
            theModeButton.setText("Light Mode");
            changeToDark();
        }
        else {
            createTaskButton.setStyle("-fx-background-color: #FFF; -fx-cursor: hand");
        }
    }

    public void updateTaskDots() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONArray allTasks = (JSONArray) ((JSONObject) jsonArray.get(0)).get("allTasks");
            ArrayList<JSONObject> taskObjects = new ArrayList<>();
            for (Object o : allTasks) {
                JSONObject jsonObject = (JSONObject) o;
                taskObjects.add(jsonObject);
            }

            ArrayList<DayButton> dayButtons = new ArrayList<>();
            for (Node node : gridPane.getChildren()) {
                AnchorPane a = (AnchorPane) node;
                DayButton dayButton = new DayButton();
                for (Node innerNode : a.getChildren()) {
                    if (innerNode instanceof Button) {
                        dayButton.setDayButton((Button) innerNode);
                        dayButton.setDayNumber(((Button) innerNode).getText());
                        dayButton.setDayPaintColor(((Button) innerNode).getTextFill());
                    }
                    else {
                        dayButton.setDayButtonTaskDot((Label) innerNode);
                        dayButton.getDayButtonTaskDot().pseudoClassStateChanged(taskDot, false);
                    }
                }
                dayButtons.add(dayButton);
            }

            for (DayButton db : dayButtons) {
                for (JSONObject j : taskObjects) {
                    if (j.get("day").equals(db.getDayNumber())) {
                        if ((db.getDayPaintColor().equals(Paint.valueOf(colorOfDays))
                                || db.getDayPaintColor().equals(Paint.valueOf(colorOfStartingDay)))
                                && j.get("month").equals(currentMonth.getText())
                                && (long) j.get("year") == yearSelection) {
                            db.getDayButtonTaskDot().pseudoClassStateChanged(taskDot, true);
                        }
                        else {
                            if (GridPane.getRowIndex(db.getDayButton().getParent()) != null) {
                                int row = GridPane.getRowIndex(db.getDayButton().getParent());
                                if ((row == 4 || row == 5)
                                        && db.getDayPaintColor().equals(Paint.valueOf(colorOfNotDays))) {
                                    String nextMonthName = "";
                                    for (int i = 0; i < monthButtons.length - 1; i++) {
                                        if (monthButtons[i] == currentMonth) {
                                            nextMonthName = monthButtons[i + 1].getText();
                                        }
                                    }
                                    for (JSONObject j2 : taskObjects) {
                                        if (j2.get("day").equals(db.getDayNumber())
                                                && j2.get("month").equals(nextMonthName)
                                                && (long) j2.get("year") == yearSelection) {
                                            db.getDayButtonTaskDot().pseudoClassStateChanged(taskDot, true);
                                        }
                                    }
                                } else if ((row == 0 || row == 1)
                                        && db.getDayPaintColor().equals(Paint.valueOf(colorOfNotDays))) {
                                    String previousMonthName = "";
                                    for (int i = 0; i < monthButtons.length; i++) {
                                        if (monthButtons[i] == currentMonth) {
                                            previousMonthName = monthButtons[i - 1].getText();
                                        }
                                    }
                                    for (JSONObject j2 : taskObjects) {
                                        if (j2.get("day").equals(db.getDayNumber())
                                                && j2.get("month").equals(previousMonthName)
                                                && (long) j2.get("year") == yearSelection) {
                                            db.getDayButtonTaskDot().pseudoClassStateChanged(taskDot, true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}