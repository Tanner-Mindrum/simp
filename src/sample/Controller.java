package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    @FXML
    private Button monthButton;
    @FXML
    private Button OctButton;
    private Button currentMonth;
    private int selectedYear;

    private String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private HashMap<String, Integer> monthsOfYear = new HashMap<String, Integer>();
    private int addTaskButtonClickCount;

    @FXML
    private void initialize() {
        Calendar calendar = Calendar.getInstance();
        String todayNumber = Integer.toString(calendar.get(Calendar.DATE));
        String todayFullName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayNumberLabelId.setText(todayNumber);
        dayLabel.setText(todayFullName.toUpperCase());
        updateTaskList(todayNumber);
        this.currentMonth = OctButton;
        SUNLabel.setStyle("-fx-font-weight: bold;");
        MONLabel.setStyle("-fx-font-weight: bold;");
        TUELabel.setStyle("-fx-font-weight: bold;");
        WEDLabel.setStyle("-fx-font-weight: bold;");
        THULabel.setStyle("-fx-font-weight: bold;");
        FRILabel.setStyle("-fx-font-weight: bold;");
        SATLabel.setStyle("-fx-font-weight: bold;");

        // Gets current month, january starts at 0
        int currentMonth = calendar.get(Calendar.MONTH);
        System.out.println("month: " + currentMonth);
        int currentYear = calendar.get(Calendar.YEAR);
        System.out.println("year: " + currentYear);
        // Getting first of the month
        YearMonth yearMonthObject = YearMonth.of(currentYear, currentMonth + 1);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        System.out.println(daysInMonth);
        System.out.println("First day of the month is:" + yearMonthObject.atDay(1).getDayOfWeek());

        monthsOfYear.put("Jan", 1); monthsOfYear.put("Feb", 2); monthsOfYear.put("Mar", 3);
        monthsOfYear.put("Apr", 4); monthsOfYear.put("May", 5); monthsOfYear.put("Jun", 6);
        monthsOfYear.put("Jul", 7); monthsOfYear.put("Aug", 8); monthsOfYear.put("Sep", 9);
        monthsOfYear.put("Oct", 10); monthsOfYear.put("Nov", 11); monthsOfYear.put("Dec", 12);
        addTaskButtonClickCount = 0;
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
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}