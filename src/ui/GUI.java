package ui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class GUI extends Application
{
    Map<String, String> dbInput = new HashMap<>();
    String url = "DbUrl";
    String uName = "username";
    String pwd = "password";
    String driverUrl = "driverUrl";


    ResultSet rs = null;
    Statement stmt = null;
    ResultSetMetaData rsmd = null;
    Connection conn = null;

    @Override
    public void start(Stage primaryStage)
    {
//        primaryStage.setTitle("ui.Game Database");
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
        String _url = dbInput.get("DbUrl");
        String userName = dbInput.get("username");
        String pwd = dbInput.get("password");


        //Execute relevant method based off argument entered in command line
        try {
            Class.forName(dbInput.get("driverUrl"));
            conn = DriverManager.getConnection(_url, userName, pwd);
//            showGames();
//            showPublishers();
                showReviewers();

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


    }

    public void showGames()
    {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT game.name, genre, release_year, publisher.name, AVG(rating)\n" +
                    "FROM game\n" +
                    "JOIN publisher\n" +
                    "\tON game.pub_id = publisher.pub_id\n" +
                    "JOIN review\n" +
                    "\tON game.game_id = review.game_id\n" +
                    "GROUP BY game.name");
            rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            for (int i = 0; i < cols; i++)
            {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        Scene scene = new Scene(table);
        stage.setScene(scene);
        stage.show();
    }

    public void showPublishers()
    {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT publisher.name, city, state, county, AVG(rating)\n" +
                    "FROM game\n" +
                    "JOIN publisher\n" +
                    "\tON game.pub_id = publisher.pub_id\n" +
                    "JOIN review\n" +
                    "\tON game.game_id = review.game_id\n" +
                    "GROUP BY publisher.name");
            rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            for (int i = 0; i < cols; i++)
            {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        Scene scene = new Scene(table);
        stage.setScene(scene);
        stage.show();
    }

    public void showReviewers()
    {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT reviewer.name, AVG(rating), COUNT(rating)\n" +
                    "FROM reviewer\n" +
                    "JOIN review\n" +
                    "\tON review.reviewer_id = reviewer.reviewer_id\n" +
                    "GROUP BY reviewer.reviewer_id");
            rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();

            for (int i = 0; i < cols; i++)
            {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            table.setItems(data);
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        Scene scene = new Scene(table);
        stage.setScene(scene);
        stage.show();
    }

}
