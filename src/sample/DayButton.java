package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class DayButton {

    private String dayNumber;
    private Button dayButton;
    private Paint dayPaintColor;
    private Label dayButtonTaskDot;

    public DayButton() {
        dayNumber = "";
        dayButton = null;
        dayPaintColor = null;
        dayButtonTaskDot = null;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public Button getDayButton() {
        return dayButton;
    }

    public Paint getDayPaintColor() {
        return dayPaintColor;
    }

    public Label getDayButtonTaskDot() {
        return dayButtonTaskDot;
    }

    public void setDayNumber(String dayNum) {
        dayNumber = dayNum;
    }

    public void setDayButton(Button b) {
        dayButton = b;
    }

    public void setDayPaintColor(Paint paintColor) {
        dayPaintColor = paintColor;
    }

    public void setDayButtonTaskDot(Label dayButtonTaskDot) {
        this.dayButtonTaskDot = dayButtonTaskDot;
    }
}
