package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Calendar;

public class Controller {

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

    private String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private HashMap<String, Integer> monthsOfYear = new HashMap<String, Integer>();

    private int addTaskButtonClickCount;

    @FXML
    private Button monthButton;
    @FXML
    private Button OctButton;
    private Button currentMonth;

    private int selectedYear;
    private int selectedMonth;


    @FXML
    private void initialize() {
        // Gets the Calendar
        Calendar calendar = Calendar.getInstance();
        // Gets and displays the current date
        String todayNumber = Integer.toString(calendar.get(Calendar.DATE));
        String todayFullName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayNumberLabelId.setText(todayNumber);
        dayLabel.setText(todayFullName.toUpperCase());

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

        SUNLabel.setStyle("-fx-font-weight: bold;");
        MONLabel.setStyle("-fx-font-weight: bold;");
        TUELabel.setStyle("-fx-font-weight: bold;");
        WEDLabel.setStyle("-fx-font-weight: bold;");
        THULabel.setStyle("-fx-font-weight: bold;");
        FRILabel.setStyle("-fx-font-weight: bold;");
        SATLabel.setStyle("-fx-font-weight: bold;");

        addTaskButtonClickCount = 0;
    }

    public void updateCalendarMonth(int selectedMonth){
        YearMonth selectedYearMonth = YearMonth.of(selectedYear, selectedMonth);
        int daysInMonth = selectedYearMonth.lengthOfMonth();
        System.out.println(daysInMonth);
        System.out.println("First day of the month is: " + selectedYearMonth.atDay(1).getDayOfWeek());
        // Change button labels to update calendar
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
        try {
            Button clickedButton = (Button) event.getSource();
            String clickedButtonDay = (clickedButton.getText());
            Integer.parseInt(clickedButtonDay);
            dayNumberLabelId.setText(clickedButtonDay);
            String todayFullName = daysOfWeek[GridPane.getColumnIndex(clickedButton)];
            dayLabel.setText(todayFullName.toUpperCase());
            updateTaskList(clickedButtonDay);
        }
        catch (NumberFormatException ignored) {}
    }

    public void addEvent(ActionEvent event) {
        addTaskButtonClickCount++;

        System.out.println(dayNumberLabelId.getText());
        String enteredTask = taskField.getText();
        if (enteredTask.length() > 0) {
            if (addTaskButtonClickCount == 1) {
                taskTextArea.setText("Current Tasks:\n-    " + enteredTask);
            }
            else {
                taskTextArea.setText(taskTextArea.getText() + "\n-    " + enteredTask);
            }
        }
    }

    /**
     * Searches through JSON database and checks if an object exists with the current day selected.
     * Updates the text area with tasks if object exists, otherwise sets text to default
     * "no tasks exist yet" message
     * @param currentDay
     */
    public void updateTaskList(String currentDay) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("simp\\src\\sample\\db.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            boolean dayClickedFound = false;
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
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