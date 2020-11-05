package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

    private int addTaskButtonClickCount;


    @FXML
    private void initialize() {
        Calendar calendar = Calendar.getInstance();
        String todayNumber = Integer.toString(calendar.get(Calendar.DATE));
        String todayFullName = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayNumberLabelId.setText(todayNumber);
        dayLabel.setText(todayFullName.toUpperCase());

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
        try (Reader reader = new FileReader("src\\sample\\db.json")) {
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
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}