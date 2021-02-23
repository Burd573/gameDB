package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class GUI extends Application
{
    Map<String, String> dbInput = new HashMap<>();

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Game Database");
        enterDBInfo();
//        for(Map.Entry<String,String> map: dbInput.entrySet())
//        {
//            System.out.println(map.getKey() + " -> " + map.getValue());
//        }
    }

    public void enterDBInfo()
    {
        Stage stage = new Stage();
        VBox vbox = new VBox();
        GridPane gridpane = new GridPane();

        Label dbUrlLabel = new Label("Enter the url to the database: ");
        Label uNameLabel = new Label("Enter the username: ");
        Label pwdLabel = new Label("Enter password: ");
        Label driverUrlLabel = new Label("Enter the url for the driver: ");

        TextField dbURLField = new TextField();
        TextField uNameField = new TextField();
        TextField pwdField = new PasswordField();
        TextField driverURLField = new TextField();;

        gridpane.add(dbUrlLabel,0,0);
        gridpane.add(uNameLabel,0,1);
        gridpane.add(pwdLabel,0,2);
        gridpane.add(driverUrlLabel,0,3);

        gridpane.add(dbURLField,1,0);
        gridpane.add(uNameField,1,1);
        gridpane.add(pwdField,1,2);
        gridpane.add(driverURLField,1,3);

        Button okButton = new Button("OK");
        okButton.setPrefWidth(50);

        vbox.getChildren().addAll(gridpane,okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(250,150);
        vbox.setMargin(gridpane,new Insets(10,10,10,10));

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();

        okButton.setOnAction(e -> {
            String url = "DbUrl";
            String uName = "username";
            String pwd = "password";
            String driverUrl = "driverUrl";

            dbInput.put(url,dbURLField.getText());
            dbInput.put(uName,uNameField.getText());
            dbInput.put(pwd,pwdField.getText());
            dbInput.put(driverUrl,driverURLField.getText());
            connectToDB();

            stage.hide();
        });
    }

    public void connectToDB()
    {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;

        String _url = dbInput.get("DbUrl");
        String userName = dbInput.get("username");
        String pwd = dbInput.get("password");

        //Execute relevant method based off argument entered in command line
        try {
            Class.forName(dbInput.get("driverUrl"));
            conn = DriverManager.getConnection(_url, userName, pwd);

            stmt = conn.createStatement();

            //SQL query
            rs = stmt.executeQuery("SELECT name FROM game");
            while(rs.next())
            {
                System.out.println(rs.getString(1));
            }

        }  catch(ClassNotFoundException e)
        {
            System.out.println("Incorrect Driver URL");
        } catch(SQLNonTransientConnectionException e)
        {
            System.out.println("Incorrect Login Info");
        }catch (SQLException e)
        {
            System.out.println("Incorrect Database URL");;
        }
            finally
        {
            //close the connection
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

}
