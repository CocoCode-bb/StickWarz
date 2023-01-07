package com.stickwarz.stickwarz;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Level {




    private Image levelImage;
    private ImageView levelNode;

    private Image backgroundImage;
    private ImageView backgroundNode;

    private Group levelGroup;
    private PixelReader pixelReader;

    private boolean aimUp, aimDown, goRight, goLeft, goUp;
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Player currentPlayer = player1;
    private AnimationTimer gameLoopTimer;

    public Level(String imageFile, String backgroundFile) {
        //loading the images from the resource folder in my project



         levelImage = new Image(imageFile);
         levelNode = new ImageView(levelImage);

        backgroundImage = new Image(backgroundFile);
        backgroundNode = new ImageView(backgroundImage);



        levelGroup = new Group(backgroundNode, levelNode);
        pixelReader = levelImage.getPixelReader();



    }


    public void addToScene(Group parent, StickWarzApp app) {
        Scene scene = parent.getScene();
        levelNode.fitWidthProperty().bind(scene.widthProperty());
        levelNode.fitHeightProperty().bind(scene.heightProperty());

        backgroundNode.fitWidthProperty().bind(scene.widthProperty());
        backgroundNode.fitHeightProperty().bind(scene.heightProperty());

        parent.getChildren().add(levelGroup);

        player1.setLevel(this);
        player2.setLevel(this);


        //adding both players to scene
        player1.addToScene(parent);
        player2.addToScene(parent);

        //setting where both players will start
        player1.moveTo(100, 100);
        player2.moveTo(200, 200);



        //calling the game loop
        startGameLoop(scene);
        setUpHandlers(scene, app);


    }
    public void removeFromScene(){
        gameLoopTimer.stop();
        player1.removeFromScene();
        player2.removeFromScene();
        levelGroup.getScene().setOnKeyPressed(null);
        levelGroup.getScene().setOnKeyReleased(null);
        Group parent = (Group) levelGroup.getParent();
        parent.getChildren().remove(levelGroup);

    }
    //
    public boolean isTerrain(int x, int y, int width, int height){

        int imageX = (x * (int)levelImage.getWidth()) / width;
        int imageY = (y * (int)levelImage.getHeight()) / height;
        Color color = pixelReader.getColor(imageX, imageY);
        double opacity = color.getOpacity();

        return opacity > 0.5;
    }
    // animation timer to render the game using fps for smoother animations and more accurate physics
    private void startGameLoop(Scene scene) {
         gameLoopTimer = new AnimationTimer() {
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

        gameLoopTimer.start();

    }
    //handlers for detection keys being pressed and not pressed
    private void setUpHandlers(Scene scene, StickWarzApp app) {


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
                    case ESCAPE:
                        app.endLevel();
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




}


