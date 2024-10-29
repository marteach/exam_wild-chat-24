module guichat {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires socket.io.client;
    requires engine.io.client;

    opens guichat;
    opens controllers;
    opens communication;
}