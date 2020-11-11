package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.HashMap;

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

    // Button to change years up and down
    @FXML
    private Button yearButton;
    @FXML
    private Label yearLabel;

    public int currentYear, thisMonth;

    private String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private HashMap<String, Integer> monthsOfYear = new HashMap<String, Integer>();

    private final int[] daysInMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private Button[] monthButtons = new Button[12];

    private int addTaskButtonClickCount;

    //private String colorMonthDays, colorNotDays, colorWeekDays, color
    public Controller() {

    }

    @FXML
    private void initialize() {
        //Had to be instantiated in here or else it was null
        monthButtons = new Button[] {janButton, febButton, marButton, aprButton, mayButton, junButton, julButton, augButton, sepButton, octButton, novButton, decButton};
        // Gets the Calendar
        Calendar calendar = Calendar.getInstance();

        thisMonth = calendar.get(Calendar.MONTH);
        // Gets and displays the current date
        String todayNumber = Integer.toString(calendar.get(Calendar.DATE));
        String todayFullName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        currentYear = calendar.get(Calendar.YEAR);
        System.out.println("CURRENT YEAR: " + currentYear);
        dayNumberLabelId.setText(todayNumber);
        dayLabel.setText(todayFullName.toUpperCase());

        updateCalendar(calendar);

//        System.out.println(thisMonth);
        currentMonth = monthButtons[thisMonth];

//        System.out.println(currentMonth.getText());

        currentMonth.setTextFill(Paint.valueOf(("#171717")));
        currentMonth.setStyle("-fx-background-color: transparent; -fx-font-weight: Bold");

        monthsOfYear.put("Jan", 0); monthsOfYear.put("Feb", 1); monthsOfYear.put("Mar", 2);
        monthsOfYear.put("Apr", 3); monthsOfYear.put("May", 4); monthsOfYear.put("Jun", 5);
        monthsOfYear.put("Jul", 6); monthsOfYear.put("Aug", 7); monthsOfYear.put("Sep", 8);
        monthsOfYear.put("Oct", 9); monthsOfYear.put("Nov", 10); monthsOfYear.put("Dec", 11);

        updateTaskList(todayNumber);
        SUNLabel.setStyle("-fx-font-weight: bold;");
        MONLabel.setStyle("-fx-font-weight: bold;");
        TUELabel.setStyle("-fx-font-weight: bold;");
        WEDLabel.setStyle("-fx-font-weight: bold;");
        THULabel.setStyle("-fx-font-weight: bold;");
        FRILabel.setStyle("-fx-font-weight: bold;");
        SATLabel.setStyle("-fx-font-weight: bold;");

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
    }

    // Updates year when
    public void updateYear(ActionEvent event){
        yearButton = (Button) event.getSource();
        String yearDirection = yearButton.getText();
        if (yearDirection.equals("yearUp")){
            // Increment year up one
            updateYearDisplay(1);
        } else {
            // Increment year down one
            updateYearDisplay(-1);
        }
        Calendar cal = Calendar.getInstance();                      //Get calendar instance
        cal.set(Calendar.MONTH, thisMonth);                         //Set calendar to current month
        cal.set(Calendar.YEAR, currentYear);                        //Set calendar to current year
        updateCalendar(cal);                                        //Update viewable calendar
    }

    public void clickedMonths(ActionEvent event) {
        Button monthButton = (Button) event.getSource();
        if (monthButton != currentMonth) {
            updateMonth(monthButton, currentMonth);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthsOfYear.get(currentMonth.getText()));
            cal.set(Calendar.YEAR, currentYear);
            updateCalendar(cal);
        }
    }

    public void pressButton(ActionEvent event) {
        //Get button
        Button clickedButton = (Button) event.getSource();
        //If button is in this month
        if(clickedButton.getTextFill().equals(Paint.valueOf("#959595"))) {
            //Set new date and weekday
            updateCurrentDay(clickedButton);
        }
        else {
            //If day clicked is in previous month
            if(GridPane.getRowIndex(clickedButton) == 0) {
                updateCurrentDay(clickedButton);                                   //Update current day display
                shiftCalendar(-1);                                      //shift and update calendar
            }
            //If day clicked is in next month
            else {
                updateCurrentDay(clickedButton);                                   //Update current day display
                    shiftCalendar(1);                                   //shift calendar and calendar
            }
        }
    }

    public void updateCurrentDay(Button clickedButton) {
        try {
            String clickedButtonDay = (clickedButton.getText());                        //Get current button number as string
            dayNumberLabelId.setText(clickedButtonDay);                                 //Set current day label with number

            String todayFullName = daysOfWeek[GridPane.getColumnIndex(clickedButton)];  //Get weekday name
            dayLabel.setText(todayFullName.toUpperCase());                              //Set it to full uppercase
            updateTaskList(clickedButtonDay);                                           //Update current day task list
        } catch (NumberFormatException ignored) {
        }
    }

    public void shiftCalendar(int shiftDirection) {
        Button newMonth;
        Calendar calendar = Calendar.getInstance();                                                 //Get a calendar instance
        calendar.set(Calendar.MONTH, thisMonth + shiftDirection);                                   //Set month to previous month of current month
        if(monthsOfYear.get(currentMonth.getText()) == 0 && shiftDirection == -1) {                 //If month is january and shifting to previous month
            newMonth = monthButtons[11];                                                            //December
            updateYearDisplay(shiftDirection);                                                      //Update Year
        }
        else if(monthsOfYear.get(currentMonth.getText()) == 11 && shiftDirection == 1) {            //If month is december and shifting to next month
            newMonth = monthButtons[0];                                                             //January
            updateYearDisplay(shiftDirection);                                                      //Update Year
        }
        else newMonth = monthButtons[(monthsOfYear.get(currentMonth.getText()) + shiftDirection)];  //Shift calendar forward or backward
        updateMonth(newMonth, currentMonth);
        updateCalendar(calendar);                                                                   //Update calendar
    }

    public void updateMonth(Button clicked, Button old) {
        old.setTextFill(Paint.valueOf("#868686"));                                      //Set old to grey
        old.setStyle("-fx-font-weight: Normal; -fx-background-color: transparent");     //Make non-bold
        clicked.setTextFill(Paint.valueOf(("#171717")));                                //Set new to dark grey
        clicked.setStyle("-fx-background-color: transparent; -fx-font-weight: Bold");   //Make bold
        currentMonth = clicked;                                                         //Point to new month
    }

    public void updateYearDisplay(int shiftDirection) {
        currentYear = Integer.parseInt(yearLabel.getText());
        currentYear += shiftDirection;
        yearLabel.setText(String.valueOf(currentYear));
    }

    public void updateCalendar(Calendar calendar) {
        System.out.println(calendar.getTime());
        thisMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        //System.out.println("Month: "+thisMonth+" Year: "+ currentYear);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //Stores value of previous month
        int previousMonth;


        int lastMonthCount;
        int thisMonthCount = 1;
        int nextMonthCount = 1;

        int startOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if(startOfMonth == 0) startOfMonth += 7;
        if(thisMonth == 0) {
            previousMonth = 11;
        }
        else {
            previousMonth = thisMonth - 1;
        }

        //Stores values of number of days in month
        //needed for leap years
        int previousMonthDays = checkIfLeapYear(previousMonth);
        int thisMonthDays = checkIfLeapYear(thisMonth);

        lastMonthCount = previousMonthDays - (startOfMonth - 1);

        for (Node node : gridPane.getChildren()) {
            AnchorPane a = (AnchorPane) node;
            for (Node innerNode : a.getChildren()) {
                Button current = (Button) innerNode;
                if(lastMonthCount <= previousMonthDays) {
                    current.setText(String.valueOf(lastMonthCount));
                    current.setTextFill(Paint.valueOf("#ccc"));
                    lastMonthCount++;
                }
                else if(thisMonthCount <= thisMonthDays) {
                    current.setText(String.valueOf(thisMonthCount));
                    current.setTextFill(Paint.valueOf("#959595"));
                    thisMonthCount++;
                }
                else {
                    current.setText(String.valueOf(nextMonthCount));
                    current.setTextFill(Paint.valueOf("#ccc"));
                    nextMonthCount++;
                }
            }
        }
    }

    public int checkIfLeapYear(int month) {
        if(month == 1) {
            if(((currentYear % 4 == 0) && (currentYear % 100!= 0)) || (currentYear%400 == 0)) {
                return daysInMonth[month] + 1;
            }
        }
        return daysInMonth[month];
    }

    /**
     * Adds a task to the current month/day selected by user. Updates a pre-existing object with a task if one
     * exists already, otherwise creates a new entry in the database with the user's inputted task
     * @param event - the button clicked
     */
    public void addEvent(ActionEvent event) {
        if (taskField.getText().length() > 0) {
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader("src\\sample\\db.json")) {
                // Get JSONArray of days with tasks
                JSONArray jsonArray = (JSONArray) parser.parse(reader);

            /* Search JSON database. If a day with tasks already exists, add the users inputted task to it.
             If it does not exist, create a new JSON object and add it to the database */
                boolean objFound = false;
                for (Object obj : jsonArray) {
                    JSONObject jsonObj = (JSONObject) obj;
                    // Exists in database if its month/day match the user's selected month/day
                    if (jsonObj.get("year").equals(currentYear) &&
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
                    // Create month/day attributes
                    JSONObject newTaskObj = new JSONObject();
                    newTaskObj.put("year", currentYear);
                    newTaskObj.put("month", currentMonth.getText());
                    newTaskObj.put("day", dayNumberLabelId.getText());
                    // Initialize an array of tasks with the user's task input
                    JSONArray tasks = new JSONArray();
                    tasks.add(taskField.getText());
                    newTaskObj.put("tasks", tasks);
                    // Add this newly created JSON object to database and write to file
                    jsonArray.add(newTaskObj);
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
        }
    }

    /**
     * Searches through JSON database and checks if an object exists with the current day selected.
     * Updates the text area with tasks if object exists, otherwise sets text to default
     * no tasks exist yet message
     * @param currentDay - the day number
     */
    public void updateTaskList(String currentDay) {
        JSONParser parser = new JSONParser();
//        try (Reader reader = new FileReader("simp\\src\\sample\\db.json")) {
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            // Get JSONArray of days with tasks
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Iterate through JSON objects (existing days with tasks) in database
            boolean dayClickedFound = false;
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                // Exists in database if its month/day match the user's selected month/day
                if (jsonObj.get("month").equals(currentMonth.getText()) && ((long) jsonObj.get("year") == currentYear)
                        && jsonObj.get("day").equals(currentDay)) {
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
            //System.out.println("Error");
        }
    }

    public void changeToLight() {
        taskDisplayArea.setStyle("-fx-background-color: rgba(53,89,119,0.8)");
        System.out.println("Change to Light");

        dayNumberLabelId.setTextFill(Paint.valueOf("white"));
        dayLabel.setTextFill(Paint.valueOf("white"));
        taskTextArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-prompt-text-fill: #FFF; -fx-text-inner-color: #FFF;");
        taskField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #FFF transparent; -fx-text-inner-color: #FFF; -fx-prompt-text-fill: #C0C0C0;");

        calendarPane.setStyle("-fx-background-color: #FFF;");

        for(Button month: monthButtons) {
            month.setTextFill(Paint.valueOf("#868686"));
        }

    }

    public void changeToDark() {
        taskDisplayArea.setStyle("-fx-background-color: rgba(202,166,136,0.8)");
        System.out.println("Change to Dark");

        dayNumberLabelId.setTextFill(Paint.valueOf("#373737"));
        dayLabel.setTextFill(Paint.valueOf("#373737"));
        taskTextArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-prompt-text-fill: #373737; -fx-text-inner-color: #373737;");
        taskField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #373737 transparent; -fx-text-inner-color: #373737; -fx-prompt-text-fill: #505050;");

        calendarPane.setStyle("-fx-background-color: #373737;");

        for(Button month: monthButtons) {
            month.setTextFill(Paint.valueOf("797979"));
        }

//        for (Node node : gridPane.getChildren()) {
//            AnchorPane a = (AnchorPane) node;
//            for (Node innerNode : a.getChildren()) {
//                Button current = (Button) innerNode;
//
//            }
//        }
    }

    @FXML
    public void changeMode(ActionEvent actionEvent) {
        Button modeButton = (Button) actionEvent.getSource();
        if(modeButton.getText().compareTo("Dark Mode") == 0) {
            modeButton.setStyle("-fx-background-color: #f8f8f8");
            modeButton.setTextFill(Paint.valueOf("#1d1d1d"));
            modeButton.setText("Light Mode");
            changeToDark();
        }
        else {
            modeButton.setStyle("-fx-background-color: #1d1d1d");
            modeButton.setTextFill(Paint.valueOf("#f8f8f8"));
            modeButton.setText("Dark Mode");
            changeToLight();
        }
    }
}