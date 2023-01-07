module com.stickwarz.stickwarz {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stickwarz.stickwarz to javafx.fxml;
    exports com.stickwarz.stickwarz;
}