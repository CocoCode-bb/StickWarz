package com.stickwarz.stickwarz;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Level {
    private final long TURNLENGTHSECS = 30;




    private Image levelImage;
    private ImageView levelNode;

    private Image backgroundImage;
    private ImageView backgroundNode;

    private StackPane levelPane;
    private PixelReader pixelReader;

    private Label scoreLabel;
    private  Label timerLabel;
    private long startTimeMillis;


    private boolean aimUp, aimDown, goRight, goLeft, goUp;
    private Player player1 = new Player("player1.png");
    private Player player2 = new Player("player2.png");
    private Player currentPlayer;
    private AnimationTimer gameLoopTimer;


    public Level(String imageFile, String backgroundFile) {
        //loading the images from the resource folder in my project
        currentPlayer = player1;
        currentPlayer.setCurrentPlayer(true);




         levelImage = new Image(imageFile);
         levelNode = new ImageView(levelImage);

        backgroundImage = new Image(backgroundFile);
        backgroundNode = new ImageView(backgroundImage);

        scoreLabel = new Label();
        scoreLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        scoreLabel.setFont(new Font(26));
        updateScoreLabel();

        timerLabel = new Label();
        timerLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        timerLabel.setFont(new Font(26));




        levelPane = new StackPane(backgroundNode, levelNode, scoreLabel, timerLabel);
        levelPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        levelPane.setAlignment(scoreLabel, Pos.TOP_LEFT);
        pixelReader = levelImage.getPixelReader();

        startTimeMillis = System.currentTimeMillis();
        handleTurnTimer();








    }


    public void addToScene(Group parent, StickWarzApp app) {
        Scene scene = parent.getScene();
        levelNode.fitWidthProperty().bind(scene.widthProperty());
        levelNode.fitHeightProperty().bind(scene.heightProperty());

        backgroundNode.fitWidthProperty().bind(scene.widthProperty());
        backgroundNode.fitHeightProperty().bind(scene.heightProperty());

        parent.getChildren().add(levelPane);

        player1.setLevel(this);
        player2.setLevel(this);


        //adding both players to scene
        player1.addToScene(parent);
        player2.addToScene(parent);

        //setting where both players will start
        player1.moveTo(70  , 130 );
        player2.moveTo((int) (parent.getScene().getWidth()- 70), 130);
        //player2.moveTo((int) (parent.getScene().getWidth()- 70), 130);
        //player2.moveTo((int) (levelImage.getWidth() - 70), 130);



        //calling the game loop
        startGameLoop(scene);
        setUpHandlers(scene, app);


    }
    public void removeFromScene(){
        gameLoopTimer.stop();
        player1.removeFromScene();
        player2.removeFromScene();
        levelPane.getScene().setOnKeyPressed(null);
        levelPane.getScene().setOnKeyReleased(null);
        Group parent = (Group) levelPane.getParent();
        parent.getChildren().remove(levelPane);

    }
    //
    public boolean isTerrain(int x, int y, int width, int height){
        int imageX = (x * (int)levelImage.getWidth()) / width;
        int imageY = (y * (int)levelImage.getHeight()) / height;
        Color color = pixelReader.getColor(imageX, imageY);
        double opacity = color.getOpacity();

        return opacity > 0.5;
    }
    public Player isOpponent(int x, int y){
        Player opponent;
        if (currentPlayer == player1){
            opponent = player2;
        }else{
            opponent = player1;
        }
        if (opponent.isHit(x, y)){
            return  opponent;
        }

        return  null;
    }
    // animation timer to render the game using fps for smoother animations and more accurate physics
    private void startGameLoop(Scene scene) {
         gameLoopTimer = new AnimationTimer() {
            //using animation timer to create simple game loop
            @Override
            public void handle(long now) {
                handleTurnTimer();


                if (goLeft) {
                    currentPlayer.goLeft();


                } else if (goRight) {
                    currentPlayer.goRight();

                }else if (aimUp){
                    currentPlayer.aimUp();
                }else if (aimDown){
                    currentPlayer.aimDown();
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

                    case ESCAPE:
                        app.endLevel();
                        break;
                    case ENTER:
                        currentPlayer.fireWeapon();
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
    private void handleTurnTimer(){
        long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
        long remainingSeconds = TURNLENGTHSECS - (elapsedMillis/1000);
        if (remainingSeconds <= 0){
            switchCurrentPlayer();
            remainingSeconds = TURNLENGTHSECS;
        }
        timerLabel.setText("Remaining turn time: "+remainingSeconds + " secs");

    }
    public void switchCurrentPlayer(){

            currentPlayer.setCurrentPlayer(false);
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        updateScoreLabel();
        startTimeMillis = System.currentTimeMillis();


        currentPlayer.setCurrentPlayer(true);

    }

    private void updateScoreLabel(){
        String text = "";
        if (currentPlayer == player1){
            text = "--> ";
        }

        text += "Player 1 lives: "+ player1.getLives() + "   ";
        if (currentPlayer == player2){
            text += "--> ";
        }
        text += "Player 2 lives: "+player2.getLives();
        scoreLabel.setText(text);


    }
    public void playerDied (Player deadPlayer) {
        //call back from one player to the other
        gameLoopTimer.stop();
        String winnerPlayer;
        if (deadPlayer == player1) {
            winnerPlayer = "Player 2";
        }else {
            winnerPlayer = "Player 1";
        }

        Label winnerLabel  = new Label();
        winnerLabel.setBackground(new Background(new BackgroundFill(Color.HONEYDEW, CornerRadii.EMPTY, Insets.EMPTY)));
        winnerLabel.setFont(new Font(50));
        winnerLabel.setText("Winner is: " +  winnerPlayer);
        levelPane.getChildren().add(winnerLabel);
    }




}


