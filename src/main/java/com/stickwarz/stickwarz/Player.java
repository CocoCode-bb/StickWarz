package com.stickwarz.stickwarz;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class Player {

    private final int MOVEMENT = 5;
    private final int AIMLINEMULTIPLIER = 3;

    //Variables needed for gravity to be calculated
    private final double GRAVITY = 0.2;
    private final int JUMPVELOCITY = -9;
    private final int TERMINALVELOCITY = 10;

    private double speedVertical = 0;
    private PlayerState currentState = PlayerState.PASSIVE;
    private Level level;



    private int lives = 5;


    private Image spriteImage;
    private ImageView spriteNode;
    private Group spriteGroup;

    private Line aimLine;
    private Group aimGroup;
    private int aimAngle;

    public Player(String imageFile) {
        //loading the images from the resource folder in my project
        spriteImage = new Image(imageFile);
        spriteNode = new ImageView(spriteImage);
        spriteGroup = new Group(spriteNode);

        aimLine = new Line();
        aimLine.setStrokeWidth(5);
        aimLine.setStroke(Color.BLUE);
        aimLine.getStrokeDashArray().addAll(10d, 10d);

        aimAngle = 0;

        aimGroup = new Group(aimLine);
        aimGroup.setVisible(false);
    }

    public void addToScene(Group parent) {
        parent.getChildren().add(spriteGroup);
        parent.getChildren().add(aimGroup);




    }
    public void removeFromScene(){

        Group parent = (Group) spriteGroup.getParent();
        parent.getChildren().remove(spriteGroup);
        parent.getChildren().remove(aimGroup);
    }

    public void setLevel(Level newLevel) {
        level = newLevel;
    }

    public void moveTo(int x, int y) {
        int cx = (int) spriteGroup.getBoundsInLocal().getWidth() / 2;
        int cy = (int) spriteGroup.getBoundsInLocal().getHeight() / 2;

        spriteGroup.relocate(x - cx, y - cy);
        updateAimLine();


    }

    public void moveBy(int moveX, int moveY) {
        //relocates the sprite from its current position based on how much it has moved by
        spriteGroup.relocate(spriteGroup.getLayoutX() + moveX, spriteGroup.getLayoutY() + moveY);
        updateAimLine();

    }
    // Go right and go left are defined by the final variable MOVEMENT, moveY is zero as the player won't move up and down using arrow keys

    public void goRight() {

        int dx = MOVEMENT;
        int x = (int) (spriteGroup.getLayoutX() + spriteGroup.getBoundsInLocal().getWidth());
        int sceneWidth = (int) spriteGroup.getScene().getWidth();
        //make sure that player can't go beyond the boundary of the scene
        if (x < sceneWidth) {
            if (sceneWidth - x < dx) {
                dx = sceneWidth - x;

            }
            while (dx > 0) {
                if (isTouchingTerrainRight()) {
                    return;
                }

                moveBy(1, 0);
                dx--;
            }
        }
    }


    public void goUp() {
        if (isTouchingTerrainBelow()) {

            speedVertical = JUMPVELOCITY;
            currentState = PlayerState.MOVING;
        }
    }

    public void goLeft() {

        int dx = MOVEMENT;
        int x = (int) spriteGroup.getLayoutX();
        //makes sure that the player can't move beyond the boundary of the scene
        if (x > 0) {
            if (x < dx) {
                dx = x;
            }
            while (dx > 0) {
                if (isTouchingTerrainLeft()) {
                    return;
                }

                moveBy(-1, 0);
                dx--;
            }
        }
    }

    public  void aimUp(){

        aimAngle -= 5;
        updateAimLine();
    }
    public  void aimDown(){
        aimAngle += 5;
        updateAimLine();
    }


    public boolean isTouchingTerrainBelow() {

        int x = (int) (spriteGroup.getLayoutX());
        int rightX = x + (int) (spriteGroup.getBoundsInLocal().getWidth() - 1);
        int y = (int) (spriteGroup.getLayoutY() + spriteGroup.getBoundsInLocal().getHeight());


        int sceneHeight = (int) spriteGroup.getScene().getHeight();
        int sceneWidth = (int) spriteGroup.getScene().getWidth();
        while (x <= rightX) {
            if (level.isTerrain(x, y, sceneWidth, sceneHeight)) {
                return true;
            }
            x++;
        }
        return false;


    }

    public boolean isTouchingTerrainAbove() {

        int x = (int) (spriteGroup.getLayoutX());
        int rightX = x + (int) (spriteGroup.getBoundsInLocal().getWidth() - 1);
        int y = (int) (spriteGroup.getLayoutY() - 1);


        int sceneHeight = (int) spriteGroup.getScene().getHeight();
        int sceneWidth = (int) spriteGroup.getScene().getWidth();
        while (x <= rightX) {
            if (level.isTerrain(x, y, sceneWidth, sceneHeight)) {
                return true;
            }
            x++;
        }
        return false;


    }

    public boolean isTouchingTerrainLeft() {

        int y = (int) (spriteGroup.getLayoutY());
        int bottomY = y + (int) (spriteGroup.getBoundsInLocal().getHeight() - 1);
        int x = (int) (spriteGroup.getLayoutX() - 1);


        int sceneHeight = (int) spriteGroup.getScene().getHeight();
        int sceneWidth = (int) spriteGroup.getScene().getWidth();
        while (y <= bottomY) {
            if (level.isTerrain(x, y, sceneWidth, sceneHeight)) {
                return true;
            }
            y++;
        }
        return false;


    }

    public boolean isTouchingTerrainRight() {

        int y = (int) (spriteGroup.getLayoutY());
        int bottomY = y + (int) (spriteGroup.getBoundsInLocal().getHeight() - 1);
        int x = (int) (spriteGroup.getLayoutX() + spriteGroup.getBoundsInLocal().getWidth());


        int sceneHeight = (int) spriteGroup.getScene().getHeight();
        int sceneWidth = (int) spriteGroup.getScene().getWidth();
        while (y <= bottomY) {
            if (level.isTerrain(x, y, sceneWidth, sceneHeight)) {
                return true;
            }
            y++;
        }
        return false;


    }

    public void gameLoop() {

        int sceneHeight = (int) spriteGroup.getScene().getHeight();
        if (speedVertical > 0) {
            int movedY = 0;
            while (movedY < speedVertical) {
                int y = (int) (spriteGroup.getLayoutY() + spriteGroup.getBoundsInLocal().getHeight());

                if (y >= (sceneHeight - 1)) {
                    // TODO: Player dies handling
                } else if (isTouchingTerrainBelow()) {
                    speedVertical = 0;
                    currentState = PlayerState.PASSIVE;
                    return;
                }
                moveBy(0, 1);
                movedY++;
            }
        } else {
            int movedY = 0;
            while (movedY > speedVertical) {


                if (isTouchingTerrainAbove()) {
                    speedVertical = 0;

                    break;
                }
                moveBy(0, -1);
                movedY--;
            }
        }


        currentState = PlayerState.MOVING;

        speedVertical = speedVertical + GRAVITY;
        if (speedVertical > TERMINALVELOCITY) {
            speedVertical = TERMINALVELOCITY;
        }
    }
    public void setAimLineVisible(boolean visible){

        aimGroup.setVisible(visible);
    }



    private void updateAimLine(){
        aimLine.getTransforms().clear();
        aimLine.setStartX(spriteGroup.getLayoutX()+(spriteGroup.getBoundsInLocal().getWidth()/2));
        aimLine.setStartY(spriteGroup.getLayoutY()+(spriteGroup.getBoundsInLocal().getHeight()/2));
        aimLine.setEndX(aimLine.getStartX());
        aimLine.setEndY(aimLine.getStartY()-(spriteGroup.getBoundsInLocal().getHeight()*3));

        aimLine.getTransforms().add(new Rotate( aimAngle, aimLine.getStartX(), aimLine.getStartY()));
    }


    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
        if (lives == 0){
            currentState = PlayerState.DEAD;
            level.playerDied(this);
        }
    }

}


