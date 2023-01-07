package com.stickwarz.stickwarz;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class StickWarzApp extends Application {
    ;

    private boolean aimUp, aimDown, goRight, goLeft, goUp;
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Player currentPlayer = player1;
    private Level currentLevel;


    @Override
    public void start(Stage stage) throws IOException {


        Group rootGroup = new Group();
        Scene scene = new Scene(rootGroup);
        setUpHandlers(scene);

        setUpLevels(rootGroup, scene);
        player1.setLevel(currentLevel);
        player2.setLevel(currentLevel);

        //adding both players to scene
        player1.addToScene(rootGroup);
        player2.addToScene(rootGroup);

        //setting where both players will start
        player1.moveTo(100, 100);
        player2.moveTo(200, 200);


        displayScene(stage, scene);
        //calling the game loop
        runGameLoop(scene);
    }

    // animation timer to render the game using fps for smoother animations and more accurate physics
    private void runGameLoop(Scene scene) {
        AnimationTimer timer = new AnimationTimer() {
            //using animation timer to create simple game loop
            @Override
            public void handle(long now) {

                if (goLeft) {
                    currentPlayer.goLeft();


                } else if (goRight) {
                    currentPlayer.goRight();

                }

                if (goUp) {
                    currentPlayer.goUp();
                }
                player1.gameLoop();
                player2.gameLoop();


            }


        };

        timer.start();

    }

    //setting up an image as a level, imageView node used to adjust the scene accurately to any screen size and aspect ratio without distortion
    private void setUpLevels(Group parent, Scene scene) {
        Level level1 = new Level("level1.png", "background1.jpg");
        level1.addToScene(parent);
        currentLevel = level1;


    }


    private void displayScene(Stage stage, Scene scene) {

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();


        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());


        stage.setScene(scene);
        stage.show();
    }

    //handlers for detection keys being pressed and not pressed
    private void setUpHandlers(Scene scene) {


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        aimUp = true;
                        break;
                    case DOWN:
                        aimDown = true;
                        break;
                    case LEFT:
                        goLeft = true;
                        break;
                    case RIGHT:

                        goRight = true;
                        break;
                    case SPACE:

                        goUp = true;
                        break;
                    //allows me to swap between players manually
                    case A:
                        if (currentPlayer == player1) {
                            currentPlayer = player2;
                        } else {
                            currentPlayer = player1;
                        }
                        break;

                }

            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        aimUp = false;
                        break;
                    case DOWN:
                        aimDown = false;
                        break;
                    case LEFT:
                        goLeft = false;
                        break;
                    case RIGHT:
                        goRight = false;
                        break;
                    case SPACE:
                        goUp = false;
                        break;
                }
            }
        });


    }

    public static void main(String[] args) {
        launch();
    }
}
