package pong;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// This version of the controller adds the method moveTheBall: the Timeline with horizontal movement to  PingPongController_v3

public class PingPongController_v4 {

    final int PADDLE_MOVEMENT_INCREMENT = 6;

    DoubleProperty currentKidPaddleY = new SimpleDoubleProperty();


    DoubleProperty ballCenterX = new SimpleDoubleProperty();
    DoubleProperty ballCenterY = new SimpleDoubleProperty();

    double allowedPaddleTopY;
    double allowedPaddleBottomY;

    Timeline timeline;

    @FXML
    Rectangle table;
    @FXML  Rectangle compPaddle;
    @FXML  Rectangle kidPaddle;
    @FXML  Circle ball;

    public void initialize()
    {

        currentKidPaddleY.set(kidPaddle.getLayoutY());
        kidPaddle.layoutYProperty().bind(currentKidPaddleY);

        ballCenterX.set(ball.getCenterX());
        ballCenterY.set(ball.getCenterY());

        ball.centerXProperty().bind(ballCenterX);
        ball.centerYProperty().bind(ballCenterY);


        allowedPaddleTopY = PADDLE_MOVEMENT_INCREMENT;
        allowedPaddleBottomY = table.getHeight() - kidPaddle.getHeight() - PADDLE_MOVEMENT_INCREMENT;


    }
    public void keyReleasedHandler(KeyEvent event){

        KeyCode keyCode = event.getCode();

        switch (keyCode){
            case UP:
                process_key_Up();
                break;
            case DOWN:
                process_key_Down();
                break;
            case N:
                process_key_N();
                break;
            case Q:
                Platform.exit(); // Terminate the application
                break;
            case S:
                process_key_S();
                break;
        }
    }


    private void process_key_Up() {

        if (currentKidPaddleY.get() > allowedPaddleTopY) {
            currentKidPaddleY.set(currentKidPaddleY.get() - PADDLE_MOVEMENT_INCREMENT);
        }
    }

    private void process_key_Down() {

        if (currentKidPaddleY.get()< allowedPaddleBottomY) {
            currentKidPaddleY.set(currentKidPaddleY.get() + PADDLE_MOVEMENT_INCREMENT);
        }
    }

    private void process_key_N() {
        System.out.println("Processing the N key");
    }

    // This method serves the ball
    private void process_key_S() {

        ballCenterY.set(currentKidPaddleY.doubleValue() + kidPaddle.getHeight()/2);
        ballCenterX.set(kidPaddle.getLayoutX());

        moveTheBall();

    }

    private void moveTheBall(){

        timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue keyValue = new KeyValue(ballCenterX, 0);
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();

    }
}