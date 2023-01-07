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

    private Scene scene;
    private Group rootGroup;
    private Level currentLevel;
    private Menu mainMenu;


    @Override
    public void start(Stage stage) throws IOException {


         rootGroup = new Group();
         scene = new Scene(rootGroup);

        mainMenu = new Menu();
        mainMenu.addToScene(rootGroup, this);

        displayScene(stage, scene);

    }

    //setting up an image as a level, imageView node used to adjust the scene accurately to any screen size and aspect ratio without distortion
    public void startLevel(int levelNum) {
        mainMenu.removeFromScene();
        Level level1 = new Level("level"+levelNum+".png", "background"+levelNum+".jpg");
        level1.addToScene(rootGroup,this);
        currentLevel = level1;


    }
    public void endLevel(){
        currentLevel.removeFromScene();
        mainMenu.addToScene(rootGroup, this);
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


    public static void main(String[] args) {
        launch();
    }
}
