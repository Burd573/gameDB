package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class GUI extends Application
{


    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Game Database");

        Label dbURL = new Label("Enter the url to the database: ");
        Label uName = new Label("Enter the username: ");
        Label pwd = new Label("Enter password: ");
        Label driverURL = new Label("Enter the url for the driver: ");

        TextField dbURLField = new TextField();
        TextField uNameField = new TextField();
        TextField pwdField = new PasswordField();
        TextField driverURLField = new TextField();

        Button okButton = new Button("OK");

        GridPane root = new GridPane();
        root.add(dbURL,0,0);
        root.add(uName,0,1);
        root.add(pwd,0,2);
        root.add(driverURL,0,3);

        root.add(dbURLField,1,0);
        root.add(uNameField,1,1);
        root.add(pwdField,1,2);
        root.add(driverURLField,1,3);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
