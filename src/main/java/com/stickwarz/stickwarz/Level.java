package com.stickwarz.stickwarz;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Level {




    private Image levelImage;
    private ImageView levelNode;
    private Group levelGroup;
    private PixelReader pixelReader;

    public Level(String imageFile) {
        //loading the images from the resource folder in my project

         levelImage = new Image(imageFile);
         levelNode = new ImageView(levelImage);


         levelGroup = new Group(levelNode);
        pixelReader = levelImage.getPixelReader();



    }


    public void addToScene(Group parent) {
        Scene scene = parent.getScene();
        levelNode.fitWidthProperty().bind(scene.widthProperty());
        levelNode.fitHeightProperty().bind(scene.heightProperty());
        parent.getChildren().add(levelGroup);


    }
    //
    public boolean isTerrain(int x, int y, int width, int height){

        int imageX = (x * (int)levelImage.getWidth()) / width;
        int imageY = (y * (int)levelImage.getHeight()) / height;
        Color color = pixelReader.getColor(imageX, imageY);
        double opacity = color.getOpacity();

        return opacity > 0.5;
    }




}


