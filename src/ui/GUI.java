package ui;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import src.DBObject;
import src.DBOperations;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


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
            showGames();
            showMainMenu();

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

    public void showMainMenu()
    {
        HBox hbox = new HBox();
        VBox vBox = new VBox();
        Pane wrapperPane = new Pane();

        Button showGamesButton = new Button("List Games");
        showGamesButton.setMinSize(400,75);

        Button addGamesButton = new Button("Add Game");
        addGamesButton.setMinSize(400,75);

        Button showPublisherButton = new Button("List Publishers");
        showPublisherButton.setMinSize(400,75);

        Button addPublisherButton = new Button("add Publisher");
        addPublisherButton.setMinSize(400,75);

        Button showReviewersButton = new Button("List Reviewers");
        showReviewersButton.setMinSize(400,75);

        Button addReviewersButton = new Button("Add Reviewer");
        addReviewersButton.setMinSize(400,75);

        Button addReviewButton = new Button("Add Review");
        addReviewButton.setMinSize(400,75);

        vBox.getChildren().addAll(showGamesButton,addGamesButton,showPublisherButton,
                addPublisherButton,showReviewersButton,addReviewersButton,addReviewButton);

        hbox.getChildren().addAll(vBox,wrapperPane);

        showGamesButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showGames());
        });

        showPublisherButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showPublishers());
        });

        showReviewersButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showReviewers());
        });

        Stage stage = new Stage();
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.show();
    }


    public TableView showGames()
    {
        DBOperations games = new DBOperations(conn);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        DBObject res = games.getGames();

        enterTableData(res,data,table);

        table.setMinHeight(525);
        return table;
    }

    public TableView showPublishers()
    {
        DBOperations publishers = new DBOperations(conn);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        DBObject res = publishers.getPublishers();

        enterTableData(res,data,table);

        table.setMinHeight(525);
        return table;
    }

    public TableView showReviewers()
    {
        DBOperations reviewers = new DBOperations(conn);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView table = new TableView();
        DBObject res = reviewers.getReviewers();

        enterTableData(res,data,table);

        table.setMinHeight(525);
        return table;
    }

    public void enterTableData(DBObject res, ObservableList<ObservableList> data, TableView table)
    {
        try
        {
            int cols = res.getRsmd().getColumnCount();
            for (int i = 0; i < cols; i++)
            {
                final int j = i;
                TableColumn col = new TableColumn(res.getRsmd().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                    {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                table.getColumns().addAll(col);
            }

            while (res.getRs().next())
            {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= res.getRsmd().getColumnCount(); i++)
                {
                    row.add(res.getRs().getString(i));
                }
                data.add(row);

            }
            table.setItems(data);
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

}
