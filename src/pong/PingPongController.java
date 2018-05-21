package pong;

import javafx.animation.KeyFrame;
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
import java.util.Random;

/*
 This version of the controller implements 6 steps of the game strategy.
 It also prints the score on the system's console.
 Your project assignment is to display the score right on the ping-pong table and
  implement the handler for the N-key to start a new game.

*/
public class PingPongController {

    final int PADDLE_MOVEMENT_INCREMENT = 7;
    final int BALL_MOVEMENT_INCREMENT = 3;

    double centerTableY;

    DoubleProperty currentKidPaddleY = new SimpleDoubleProperty();
    DoubleProperty currentComputerPaddleY = new SimpleDoubleProperty();
    double initialComputerPaddleY;

    DoubleProperty ballCenterX = new SimpleDoubleProperty();
    DoubleProperty ballCenterY = new SimpleDoubleProperty();

    double allowedPaddleTopY;
    double allowedPaddleBottomY;

    Timeline timeline;

    int computerScore;
    int kidScore;

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


        initialComputerPaddleY = compPaddle.getLayoutY();
        currentComputerPaddleY.set(initialComputerPaddleY);
        compPaddle.layoutYProperty().bind(currentComputerPaddleY);


        allowedPaddleTopY = PADDLE_MOVEMENT_INCREMENT;
        allowedPaddleBottomY = table.getHeight() - kidPaddle.getHeight() - PADDLE_MOVEMENT_INCREMENT;

        centerTableY = table.getHeight()/2;
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

    private void process_key_S() {

        ballCenterY.set(currentKidPaddleY.doubleValue() + kidPaddle.getHeight()/2);
        ballCenterX.set(kidPaddle.getLayoutX());

        moveTheBall();
    }

    private void moveTheBall(){

        Random randomYGenerator = new Random();
        double randomYincrement = randomYGenerator.nextInt(BALL_MOVEMENT_INCREMENT);

        final boolean isServingFromTop = (ballCenterY.get() <= centerTableY)?true:false;

        KeyFrame keyFrame = new KeyFrame(new Duration(10), event -> {

            if (ballCenterX.get() >= -20) {

                ballCenterX.set(ballCenterX.get() - BALL_MOVEMENT_INCREMENT);

                if (isServingFromTop) {
                    ballCenterY.set(ballCenterY.get() + randomYincrement);

                    currentComputerPaddleY.set( currentComputerPaddleY.get() + 1);

                } else {
                    ballCenterY.set(ballCenterY.get() - randomYincrement);

                    currentComputerPaddleY.set(currentComputerPaddleY.get() - 1);
                }

                if (checkForBallPaddleContact(compPaddle)){
                    timeline.stop();
                    currentComputerPaddleY.set(initialComputerPaddleY);
                    bounceTheBall();
                };

            } else {
                timeline.stop();

                updateScore();

                currentComputerPaddleY.set(initialComputerPaddleY);
            }
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }


    private boolean checkForBallPaddleContact(Rectangle paddle){

        if (ball.intersects(paddle.getBoundsInParent())){
            return true;
        } else {
             return false;
        }
    }

    private void bounceTheBall() {

        double theBallOffTheTableX = table.getWidth() + 20;

        KeyFrame keyFrame = new KeyFrame(new Duration(10), event -> {

            if (ballCenterX.get() < theBallOffTheTableX) {

                ballCenterX.set(ballCenterX.get() + BALL_MOVEMENT_INCREMENT);

                if (checkForBallPaddleContact(kidPaddle)){
                    timeline.stop();
                    moveTheBall();
                };

            } else {
                timeline.stop();
                updateScore();
            }

        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();

      }

    private void updateScore(){

        if (ballCenterX.get() > table.getWidth()){
            // Computer bounced the ball and the Kid didn't hit it back
            computerScore ++;
        } else if (ballCenterY.get() > 0 && ballCenterY.get() <= table.getHeight()){
            // The Kid served the ball and Computer didn't hit it back
            kidScore++;
        } else{
            // The Kid served the ball off the table
            computerScore++;
        }


        System.out.println("Computer: " + computerScore + ", Kid: " + kidScore);
    }
}