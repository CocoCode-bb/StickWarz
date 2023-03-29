package com.stickwarz.stickwarz;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class Weapon {


    private Line laserLine;
    private Group laserGroup;
    private Point2D startPoint;
    private int laserAngle;
    private int laserLength = 0;
    private final int LASERSPEED = 20;
    private Level level;


    public Weapon(Level lvl, Line aimLine, int aimAngle) {
        laserAngle = aimAngle;
        level = lvl;

        laserLine = new Line();
        laserLine.setStrokeWidth(5);
        laserLine.setStroke(Color.RED);


        laserGroup = new Group(laserLine);
        startPoint = new Point2D(aimLine.getStartX(), aimLine.getStartY());
        Point2D start = aimLine.localToParent(aimLine.getStartX(), aimLine.getStartY());
        laserLine.setStartX(start.getX());
        laserLine.setStartY(start.getY());
        Point2D end = aimLine.localToParent(aimLine.getEndX(), aimLine.getEndY());
        laserLine.setEndX(end.getX());
        laserLine.setEndY(end.getY());

        updateLaserLine();

    }

    public void addToScene(Group parent) {
        parent.getChildren().add(laserGroup);

    }

    public void removeFromScene() {

        Group parent = (Group) laserGroup.getParent();
        parent.getChildren().remove(laserGroup);

    }

    private void updateLaserLine() {
        laserLine.getTransforms().clear();
        laserLine.setStartX(startPoint.getX());
        laserLine.setStartY(startPoint.getY());
        laserLine.setEndX(laserLine.getStartX());
        laserLine.setEndY(laserLine.getStartY() - laserLength);
        laserLine.getTransforms().add(new Rotate(laserAngle, laserLine.getStartX(), laserLine.getStartY()));
    }

    public void gameLoop() {
        int sceneHeight = (int) laserGroup.getScene().getHeight();
        int sceneWidth = (int) laserGroup.getScene().getWidth();
        int counter = 0;
        while (counter < LASERSPEED) {
            laserLength++;
            updateLaserLine();
            Point2D endPoint = laserLine.localToParent(laserLine.getEndX(), laserLine.getEndY());
            if (endPoint.getX() < 0 || endPoint.getY() < 0 || endPoint.getX() >= sceneWidth || endPoint.getY() >= sceneHeight ||
                    level.isTerrain((int) endPoint.getX(), (int) endPoint.getY(), sceneWidth, sceneHeight)) {
                level.switchCurrentPlayer();
                return;
            }
            Player opponent = level.isOpponent((int) endPoint.getX(), (int) endPoint.getY());
            if(opponent != null){
               if (opponent.loseLife()){
                   level.switchCurrentPlayer();
               }
               return;
            }
            counter++;
        }


    }


}
