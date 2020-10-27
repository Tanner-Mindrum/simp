package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.LabeledSkinBase;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

            if (clickedButtonDay.equals("23")) {
                dayLabel.setText("FRIDAY");
            }
            else if (clickedButtonDay.equals("24")) {
                dayLabel.setText("SATURDAY");
            }
        }
        catch (NumberFormatException ignored) {}
    }

    public void addEvent(ActionEvent event) {
        addTaskButtonClickCount++;

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
}
