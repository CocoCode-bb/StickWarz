package com.stickwarz.stickwarz;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;


public class Menu {




    private Image menuImage;
    private ImageView menuNode;
    private Group menuGroup;


    public Menu() {




         menuImage = new Image("menu.png");
         menuNode = new ImageView(menuImage);





        menuGroup = new Group( menuNode);




    }


    public void addToScene(Group parent, StickWarzApp app) {
        Scene scene = parent.getScene();
        menuNode.fitWidthProperty().bind(scene.widthProperty());
        menuNode.fitHeightProperty().bind(scene.heightProperty());



        parent.getChildren().add(menuGroup);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DIGIT1  :
                        app.startLevel(1);
                        break;
                    case DIGIT2  :
                        app.startLevel(2);
                        break;
                    case DIGIT3  :
                        app.startLevel(3);
                        break;
                    case DIGIT4  :
                        app.startLevel(4);
                        break;
                    case DIGIT5  :
                         app.startLevel(5);
                        break;




                }

            }
        });


    }
    public void removeFromScene(){
        menuGroup.getScene().setOnKeyPressed(null);
       Group parent = (Group) menuGroup.getParent();
       parent.getChildren().remove(menuGroup);

    }


;


}


