package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.DBOperations;
import src.GameInfo;

import java.sql.*;
import java.util.*;


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
        enterDBInfo();
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

        TextField dbURLField = new TextField("jdbc:mysql://localhost/game_db?autoReconnect=true&useSSL= false");
        TextField uNameField = new TextField("root");
        //change to password field later
        TextField pwdField = new TextField("Cardinals2112");
        TextField driverURLField = new TextField("com.mysql.cj.jdbc.Driver");;

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

        addGamesButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addGame());
        });

        showPublisherButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showPublishers());
        });

        addPublisherButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addPublisher());
        });

        showReviewersButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showReviewers());
        });

        addReviewersButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addReviewer());
        });

        addReviewButton.setOnAction(e -> {
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addReview());
        });

        Stage stage = new Stage();
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.setMinWidth(850);
        stage.show();
    }

    public TableView showGames()
    {
        TableView table = new TableView();

        enterGameTableData(table);

        table.setMinHeight(525);
        return table;
    }

    public TableView showPublishers()
    {
        TableView table = new TableView();

        enterPublisherTableData(table);

        table.setMinHeight(525);
        return table;
    }

    public TableView showReviewers()
    {
        TableView table = new TableView();

        enterReviewerTableData(table);

        table.setMinHeight(525);
        table.setMinWidth(350);
        return table;
    }

    public void enterGameTableData(TableView table)
    {
        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameInfo, String> gameNameCol = new TableColumn<>("Game Name");
        gameNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameInfo, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<GameInfo, Integer> relYearCol = new TableColumn<>("Release Year");
        relYearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        TableColumn<GameInfo, String> pubNameCol = new TableColumn<>("Publisher Name");
        pubNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameInfo, Double> ratingCol = new TableColumn<>("Average Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("AvgReview"));

        table.getColumns().addAll(gameNameCol,genreCol,relYearCol,pubNameCol,ratingCol);
        table.getItems().addAll(dbOps.getGames());

    }

    public void enterPublisherTableData(TableView table)
    {
        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameInfo, String> pubNameCol = new TableColumn<>("Publisher Name");
        pubNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameInfo, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("City"));

        TableColumn<GameInfo, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));

        TableColumn<GameInfo, String> stateCol = new TableColumn<>("State");
        stateCol.setCellValueFactory(new PropertyValueFactory<>("State"));

        TableColumn<GameInfo, Double> avgRatingCol = new TableColumn<>("Average Game Rating");
        avgRatingCol.setCellValueFactory(new PropertyValueFactory<>("AvgRating"));

        table.getColumns().addAll(pubNameCol,cityCol,countryCol,stateCol,avgRatingCol);
        table.getItems().addAll(dbOps.getPublishers());

    }

    public void enterReviewerTableData(TableView table)
    {
        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameInfo, String> reviewerNameCol = new TableColumn<>("Reviewer Name");
        reviewerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameInfo, Double> avgReviewCol = new TableColumn<>("Average Review");
        avgReviewCol.setCellValueFactory(new PropertyValueFactory<>("avgRating"));

        TableColumn<GameInfo, Integer> numReviewsCol = new TableColumn<>("Number of Reviews");
        numReviewsCol.setCellValueFactory(new PropertyValueFactory<>("numReviews"));

        table.getColumns().addAll(reviewerNameCol,avgReviewCol,numReviewsCol);
        table.getItems().addAll(dbOps.getReviewers());
    }

    public VBox addGame()
    {
        VBox vbox = new VBox();
        Label gameName = new Label("Game Name");
        Label gameGenre = new Label("Game Genre");
        Label releaseYear = new Label("Release Year");
        Label pubName = new Label("Publisher");

        DBOperations dbOps = new DBOperations(conn);
        List<String> names = dbOps.getPublisherNames();

        TextField gameNameInput = new TextField();
        gameNameInput.setMinWidth(200);

        TextField gameGenreInput = new TextField();
        gameGenreInput.setMinWidth(200);

        TextField releaseYearInput = new TextField();
        releaseYearInput.setMinWidth(200);

        ComboBox<String> comboBox = new ComboBox<String>(FXCollections.observableArrayList(names));

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(gameName,gameNameInput);

        VBox genreBox = new VBox();
        genreBox.getChildren().addAll(gameGenre,gameGenreInput);

        VBox yearBox = new VBox();
        yearBox.getChildren().addAll(releaseYear,releaseYearInput);

        VBox pubBox = new VBox();
        pubBox.getChildren().addAll(pubName,comboBox);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.addGame(comboBox.getValue(),gameGenreInput.getText(),gameNameInput.getText(),releaseYearInput.getText());
        });

        vbox.getChildren().addAll(nameBox,genreBox,yearBox,pubBox,enterButton);
        vbox.setSpacing(40);
        vbox.setMargin(nameBox,new Insets(30, 10, 10, 40));
        vbox.setMargin(genreBox,new Insets(10, 10, 10, 40));
        vbox.setMargin(yearBox,new Insets(10, 10, 10, 40));
        vbox.setMargin(pubBox,new Insets(10, 10, 10, 40));
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }



    public VBox addPublisher()
    {
        VBox vbox = new VBox();
        Label pubName = new Label("Publisher Name");
        Label pubCity = new Label("Publisher City");
        Label pubState = new Label("Publisher State");
        Label pubCountry = new Label("Publisher Country");

        DBOperations dbOps = new DBOperations(conn);

        TextField pubNameInput = new TextField();
        pubNameInput.setMinWidth(200);

        TextField pubCityInput = new TextField();
        pubCityInput.setMinWidth(200);

        TextField pubStateInput = new TextField();
        pubStateInput.setMinWidth(200);

        TextField pubCountryInput = new TextField();
        pubCountryInput.setMinWidth(200);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(pubName,pubNameInput);

        VBox cityBox = new VBox();
        cityBox.getChildren().addAll(pubCity,pubCityInput);

        VBox stateBox = new VBox();
        stateBox.getChildren().addAll(pubState,pubStateInput);

        VBox countryBox = new VBox();
        countryBox.getChildren().addAll(pubCountry,pubCountryInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);
        enterButton.setOnAction(e -> {
            dbOps.addPublisher(pubNameInput.getText(),pubCityInput.getText(),pubStateInput.getText(),pubCountryInput.getText());
        });

        vbox.getChildren().addAll(nameBox,cityBox,stateBox,countryBox,enterButton);

        vbox.setSpacing(40);
        vbox.setMargin(nameBox,new Insets(30, 10, 10, 40));
        vbox.setMargin(cityBox,new Insets(10, 10, 10, 40));
        vbox.setMargin(stateBox,new Insets(10, 10, 10, 40));
        vbox.setMargin(countryBox,new Insets(10, 10, 10, 40));
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public VBox addReviewer()
    {
        VBox vbox = new VBox();

        DBOperations dbOps = new DBOperations(conn);

        Label reviewerName = new Label("Reviewer Name");

        TextField reviewerNameInput = new TextField();
        reviewerNameInput.setMinWidth(200);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(reviewerName,reviewerNameInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.addReviewer(reviewerNameInput.getText());
        });

        vbox.getChildren().addAll(nameBox,enterButton);
        vbox.setSpacing(30);
        vbox.setMargin(nameBox,new Insets(30,10,10,40));
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    public VBox addReview()
    {
        VBox vBox = new VBox();

        DBOperations dbOps = new DBOperations(conn);
        List<String> reviewerNames = dbOps.getReviewerNames();
        List<String> gameNames = dbOps.getGameNames();

        Label reviewerName = new Label("Reviewer Name");
        Label gameName = new Label("Game Name");
        Label rating = new Label("Score(out of 100)");
        Label comment = new Label("Comment");

        ComboBox<String> reviewerComboBox = new ComboBox<String>(FXCollections.observableArrayList(reviewerNames));
        ComboBox<String> gameComboBox = new ComboBox<String>(FXCollections.observableArrayList(gameNames));

        TextField ratingInput = new TextField();
        ratingInput.setMinWidth(200);

        TextField commentInput = new TextField();
        commentInput.setMinWidth(200);

        VBox reviewerNameBox = new VBox();
        reviewerNameBox.getChildren().addAll(reviewerName,reviewerComboBox);

        VBox gameNameBox = new VBox();
        gameNameBox.getChildren().addAll(gameName,gameComboBox);

        VBox ratingBox = new VBox();
        ratingBox.getChildren().addAll(rating,ratingInput);

        VBox commentBox = new VBox();
        commentBox.getChildren().addAll(comment,commentInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);
        enterButton.setOnAction(e -> {
            dbOps.addReview(reviewerComboBox.getValue(),gameComboBox.getValue(),Double.parseDouble(ratingInput.getText()),commentInput.getText());
        });

        vBox.getChildren().addAll(reviewerNameBox,gameNameBox,ratingBox,commentBox,enterButton);

        vBox.setSpacing(40);
        vBox.setMargin(reviewerNameBox,new Insets(30, 10, 10, 40));
        vBox.setMargin(gameNameBox,new Insets(10, 10, 10, 40));
        vBox.setMargin(ratingBox,new Insets(10, 10, 10, 40));
        vBox.setMargin(commentBox,new Insets(10, 10, 10, 40));
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

}
