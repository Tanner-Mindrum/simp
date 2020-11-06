package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
public class Controller {

    @FXML
    private GridPane gridPane;
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
    private Button monthButton;
    @FXML
    private Button OctButton;
    private Button currentMonth;
    private int selectedYear;
    // Button to change years up and down
    @FXML
    private Button yearButton;
    @FXML
    private Label yearLabel;

    public int currentYear;


    private String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private int[] daysInMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private HashMap<String, Integer> monthsOfYear = new HashMap<String, Integer>();

    private int currentDayOfWeek;
    private int thisMonth;


    private int addTaskButtonClickCount;


    private int selectedMonth;

    public Controller() {
    }


    @FXML
    private void initialize() {
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

        monthsOfYear.put("Jan", 1); monthsOfYear.put("Feb", 2); monthsOfYear.put("Mar", 3);
        monthsOfYear.put("Apr", 4); monthsOfYear.put("May", 5); monthsOfYear.put("Jun", 6);
        monthsOfYear.put("Jul", 7); monthsOfYear.put("Aug", 8); monthsOfYear.put("Sep", 9);
        monthsOfYear.put("Oct", 10); monthsOfYear.put("Nov", 11); monthsOfYear.put("Dec", 12);


        // TODO: Change which month it currently is, replace which days are in there
        currentMonth = OctButton;


        // Gets current month, january starts at 0
//        int currentMonth = calendar.get(Calendar.MONTH);
//        selectedMonth = currentMonth;
//        System.out.println("Current Month: " + currentMonth);
//        int currentYear = calendar.get(Calendar.YEAR);
//        selectedYear = currentYear;
//        System.out.println("Current Year: " + currentYear);
//        // Getting first of the month
//        YearMonth yearMonthObject = YearMonth.of(currentYear, currentMonth + 1);
//        int daysInMonth = yearMonthObject.lengthOfMonth();
//        System.out.println(daysInMonth);
//        System.out.println("First day of the month is: " + yearMonthObject.atDay(1).getDayOfWeek());

        updateTaskList(todayNumber);
        this.currentMonth = OctButton;
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

        monthsOfYear.put("Jan", 1); monthsOfYear.put("Feb", 2); monthsOfYear.put("Mar", 3);
        monthsOfYear.put("Apr", 4); monthsOfYear.put("May", 5); monthsOfYear.put("Jun", 6);
        monthsOfYear.put("Jul", 7); monthsOfYear.put("Aug", 8); monthsOfYear.put("Sep", 9);
        monthsOfYear.put("Oct", 10); monthsOfYear.put("Nov", 11); monthsOfYear.put("Dec", 12);
        addTaskButtonClickCount = 0;
    }

//    public void updateCalendarMonth(int selectedMonth){
//        YearMonth selectedYearMonth = YearMonth.of(selectedYear, selectedMonth);
//        int daysInMonth = selectedYearMonth.lengthOfMonth();
//        System.out.println(daysInMonth);
//        System.out.println("First day of the month is: " + selectedYearMonth.atDay(1).getDayOfWeek());
//        // Change button labels to update calendar
//    }

    // Updates year when
    public void updateYear(ActionEvent event){
        yearButton = (Button) event.getSource();
        String yearDirection = yearButton.getText();
        if (yearDirection.equals("yearUp")){
            // Increment year up one
            currentYear = Integer.parseInt(yearLabel.getText());
            currentYear += 1;
            yearLabel.setText(String.valueOf(currentYear));
        } else {
            // Increment year down one
            currentYear = Integer.parseInt(yearLabel.getText());
            currentYear -= 1;
            yearLabel.setText(String.valueOf(currentYear));
        }
    }


    public void clickedMonths(ActionEvent event) {
        monthButton = (Button) event.getSource();
        String clickedButtonMonth = monthButton.getText();
        String monthButtonText = monthButton.getText();
        if (monthButton != currentMonth) {
            currentMonth.setTextFill(Paint.valueOf("#868686"));
            currentMonth.setStyle("-fx-font-weight: Normal; -fx-background-color: transparent");
            currentMonth = monthButton;
            currentMonth.setTextFill(Paint.valueOf(("#171717")));
            currentMonth.setStyle("-fx-background-color: transparent; -fx-font-weight: Bold");
        }
    }


    public void pressButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        System.out.println(clickedButton.getTextFill());
        if(clickedButton.getTextFill().equals(Paint.valueOf("#959595"))) {
            System.out.println("Not moving month");
            updateCurrentDay(clickedButton);
        }
        else {
            if(GridPane.getRowIndex(clickedButton) == 0) {
                System.out.println("Moving month back");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, thisMonth - 1);
                updateCurrentDay(clickedButton);
                System.out.println(calendar.getTime());
                updateCalendar(calendar);
            }
            else {
                System.out.println("Moving month forward");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, thisMonth + 1);
                updateCurrentDay(clickedButton);
                System.out.println(calendar.getTime());
                updateCalendar(calendar);
            }
        }
    }

    public void updateCurrentDay(Button clickedButton) {
        try {
            String clickedButtonDay = (clickedButton.getText());
            dayNumberLabelId.setText(clickedButtonDay);

            currentDayOfWeek = GridPane.getColumnIndex(clickedButton);
            String todayFullName = daysOfWeek[currentDayOfWeek];
            currentDayOfWeek++;

            dayLabel.setText(todayFullName.toUpperCase());
            updateTaskList(clickedButtonDay);
        } catch (NumberFormatException ignored) {
        }
    }


    public void updateCalendar(Calendar calendar) {
        thisMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int previousMonth;

        int startOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int lastMonthDays;
        System.out.println("Start of month:" + startOfMonth);
        if(startOfMonth == 0) startOfMonth += 7;
        if(thisMonth == 0) {
            previousMonth = 11;
        }
        else {
            previousMonth = thisMonth - 1;
        }
        lastMonthDays = daysInMonth[previousMonth] - (startOfMonth - 1);

        System.out.println("Days to add before: " + lastMonthDays);

        int thisMonthDays = 1;
        int nextMonthDays = 1;
        for (Node node : gridPane.getChildren()) {
            AnchorPane a = (AnchorPane) node;
            for (Node innerNode : a.getChildren()) {
                Button current = (Button) innerNode;
                if(lastMonthDays <= daysInMonth[previousMonth]) {
                    //System.out.println("Add last month: " + lastMonthDays);
                    current.setText(String.valueOf(lastMonthDays));
                    current.setTextFill(Paint.valueOf("#ccc"));
                    lastMonthDays++;
                }
                else if(thisMonthDays <= daysInMonth[thisMonth]) {
                    //System.out.println("Add this month: " + thisMonthDays);
                    current.setText(String.valueOf(thisMonthDays));
                    current.setTextFill(Paint.valueOf("#959595"));
                    thisMonthDays++;
                }
                else {
                    //System.out.println("Add next month: " + nextMonthDays);
                    current.setText(String.valueOf(nextMonthDays));
                    current.setTextFill(Paint.valueOf("#ccc"));
                    nextMonthDays++;
                }
            }
        }
    }

    public void addEvent(ActionEvent event) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            // Get JSONArray of days with tasks
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            boolean dayFound = false;
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                //
                if (jsonObj.get("day").equals(dayNumberLabelId)) {
                    dayFound = true;
                    JSONArray taskArray = (JSONArray) jsonObj.get("tasks");
                    taskArray.add(taskField.getText());
                }
            }

            if (!dayFound) {

            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
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
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
//        try (Reader reader = new FileReader("src\\sample\\db.json")) {
            // Get JSONArray of days with tasks
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            boolean dayClickedFound = false;
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                //
                if (jsonObj.get("day").equals(currentDay)) {
                    dayClickedFound = true;
                    JSONArray taskArray = (JSONArray) jsonObj.get("tasks");
                    StringBuilder listOfTasks = new StringBuilder();
                    for (Object task : taskArray) {
                        listOfTasks.append("\n-    ").append((String) task);
                    }
                    taskTextArea.setText("Current Tasks:" + listOfTasks);
                    break;
                }
            }
            if (!dayClickedFound) {
                taskTextArea.setText("Current Tasks:" + "\n-    There are no tasks");
            }
        }
        catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}