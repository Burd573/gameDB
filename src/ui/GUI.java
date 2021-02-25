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
import src.GamePlatforms;
import src.GameReviews;

import java.sql.*;
import java.util.*;


public class GUI extends Application
{
    Map<String, String> dbInput = new HashMap<>();
    String url = "DbUrl";
    String uName = "username";
    String pwd = "password";
    String driverUrl = "driverUrl";

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

        try {
            Class.forName(dbInput.get("driverUrl"));
            conn = DriverManager.getConnection(_url, userName, pwd);
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
        hbox.setPrefWidth(600);
        VBox col1Buttons = new VBox();
        VBox col2Buttons = new VBox();
        HBox buttons = new HBox();
        Pane wrapperPane = new Pane();
        wrapperPane.prefWidthProperty().bind(hbox.widthProperty());

        Stage stage = new Stage();
        stage.setMinWidth(600);
        stage.setMinHeight(390);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);

        Button showGamesButton = new Button("List Games");
        showGamesButton.setMinSize(100,50);

        Button addGamesButton = new Button("Add Game");
        addGamesButton.setMinSize(100,50);

        Button showPublisherButton = new Button("List Publishers");
        showPublisherButton.setMinSize(100,50);

        Button addPublisherButton = new Button("add Publisher");
        addPublisherButton.setMinSize(100,50);

        Button showReviewersButton = new Button("List Reviewers");
        showReviewersButton.setMinSize(100,50);

        Button addReviewersButton = new Button("Add Reviewer");
        addReviewersButton.setMinSize(100,50);

        Button addReviewButton = new Button("Add Review");
        addReviewButton.setMinSize(100,50);

        Button gameInfoButton = new Button("Game Info");
        gameInfoButton.setMinSize(100,50);

        Button platformInfoButton = new Button("Platform Info");
        platformInfoButton.setMinSize(100,50);

        Button publisherInfoButton = new Button("Publisher Info");
        publisherInfoButton.setMinSize(100,50);

        Button reviewerInfoButton = new Button("Reviewer Info");
        reviewerInfoButton.setMinSize(100,50);

        Button genreInfoButton = new Button("Genre Info");
        genreInfoButton.setMinSize(100,50);

        Button editDataButton = new Button("Edit Data");
        editDataButton.setMinSize(100,50);

        Button deleteDataButton = new Button("Remove Data");
        deleteDataButton.setMinSize(100,50);

        col1Buttons.getChildren().addAll(showGamesButton, gameInfoButton, addGamesButton,showPublisherButton, publisherInfoButton,
                addPublisherButton,deleteDataButton);

        col2Buttons.getChildren().addAll(showReviewersButton,reviewerInfoButton,addReviewersButton,addReviewButton,platformInfoButton,
                genreInfoButton,editDataButton);

        buttons.getChildren().addAll(col1Buttons,col2Buttons);

        hbox.getChildren().addAll(buttons,wrapperPane);

        showGamesButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showGames(wrapperPane));
        });

        addGamesButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addGame(wrapperPane));
        });

        showPublisherButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showPublishers(wrapperPane));
        });

        addPublisherButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addPublisher(wrapperPane));
        });

        showReviewersButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(showReviewers(wrapperPane));
        });

        addReviewersButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addReviewer(wrapperPane));
        });

        addReviewButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(addReview(wrapperPane));
        });

        gameInfoButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(selectGame(wrapperPane));
        });
        stage.show();
    }

    public TableView showGames(Pane wrapper)
    {
        TableView table = new TableView();

        enterGameTableData(table);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    public TableView showPublishers(Pane wrapper)
    {
        TableView table = new TableView();

        enterPublisherTableData(table);
        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());

        return table;
    }

    public TableView showReviewers(Pane wrapper)
    {
        TableView table = new TableView();

        enterReviewerTableData(table);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());

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

    public VBox addGame(Pane wrapper)
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

        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(names));

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
        vbox.setSpacing(20);
        vbox.setMargin(nameBox,new Insets(15, 40, 0, 40));
        vbox.setMargin(genreBox,new Insets(10, 40, 0, 40));
        vbox.setMargin(yearBox,new Insets(10, 40, 0, 40));
        vbox.setMargin(pubBox,new Insets(10, 40, 15, 40));
        vbox.setAlignment(Pos.CENTER);

        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }



    public VBox addPublisher(Pane wrapper)
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

        vbox.setSpacing(20);
        vbox.setMargin(nameBox,new Insets(15, 40, 0, 40));
        vbox.setMargin(cityBox,new Insets(10, 40, 0, 40));
        vbox.setMargin(stateBox,new Insets(10, 40, 0, 40));
        vbox.setMargin(countryBox,new Insets(10, 40, 15, 40));
        vbox.setAlignment(Pos.CENTER);

        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }

    public VBox addReviewer(Pane wrapper)
    {
        VBox vbox = new VBox();
        Pane wrapperPane = new Pane();

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
        vbox.setMargin(nameBox,new Insets(30,40,10,40));
        vbox.setAlignment(Pos.CENTER);

        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }

    public VBox addReview(Pane wrapper)
    {
        VBox vBox = new VBox();

        DBOperations dbOps = new DBOperations(conn);
        List<String> reviewerNames = dbOps.getReviewerNames();
        List<String> gameNames = dbOps.getGameNames();

        Label reviewerName = new Label("Reviewer Name");
        Label gameName = new Label("Game Name");
        Label rating = new Label("Score(out of 100)");
        Label comment = new Label("Comment");

        ComboBox<String> reviewerComboBox = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));
        ComboBox<String> gameComboBox = new ComboBox<>(FXCollections.observableArrayList(gameNames));

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

        vBox.setSpacing(20);
        vBox.setMargin(reviewerNameBox,new Insets(15, 40, 0, 40));
        vBox.setMargin(gameNameBox,new Insets(10, 40, 0, 40));
        vBox.setMargin(ratingBox,new Insets(10, 40, 0, 40));
        vBox.setMargin(commentBox,new Insets(10, 40, 15, 40));
        vBox.setAlignment(Pos.CENTER);

        vBox.prefHeightProperty().bind(wrapper.heightProperty());
        vBox.prefWidthProperty().bind(wrapper.widthProperty());

        return vBox;
    }

    public VBox selectGame(Pane wrapper)
    {
        VBox getSelection = new VBox();

        DBOperations dbOps = new DBOperations(conn);
        List<String> gameNames = dbOps.getGameNames();
        Label gameName = new Label("Select Game You Wish to View");
        ComboBox<String> gameComboBox = new ComboBox<>(FXCollections.observableArrayList(gameNames));

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            getSelection.getChildren().clear();
            getSelection.getChildren().add(gameInfo(wrapper,gameComboBox.getValue()));
        });

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(gameName,gameComboBox);

        getSelection.getChildren().addAll(nameBox,enterButton);
        getSelection.setSpacing(30);
        getSelection.setMargin(nameBox,new Insets(30,40,10,40));
        getSelection.setAlignment(Pos.CENTER);

        getSelection.prefHeightProperty().bind(wrapper.heightProperty());
        getSelection.prefWidthProperty().bind(wrapper.widthProperty());

        return getSelection;
    }

    public VBox gameInfo(Pane wrapper,String name)
    {
        VBox ret = new VBox();
        HBox select = new HBox();
        String reviews = "Reviews";
        String platforms = "Platforms";

        Label choiceSelection = new Label("Select Desired Information");
        ComboBox<String> choice = new ComboBox<>();
        choice.getItems().addAll(reviews,platforms);

        Button okButton = new Button("OK");
        select.getChildren().add(choiceSelection);
        select.getChildren().add(choice);
        select.getChildren().add(okButton);

        okButton.setOnAction(e -> {
            if(choice.getValue().equals(reviews))
            {
                ret.getChildren().clear();
                ret.getChildren().addAll(showGameReviews(wrapper,name));
            }

            if(choice.getValue().equals(platforms))
            {
                ret.getChildren().clear();
                ret.getChildren().addAll(showGamePlatforms(wrapper,name));
            }
        });
        ret.getChildren().add(select);
        return ret;
    }

    public TableView showGameReviews(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterGameReviewTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    public void enterGameReviewTableData(TableView table,String name)
    {
        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameReviews, String> reviewerNameCol = new TableColumn<>("Reviewer Name");
        reviewerNameCol.setCellValueFactory(new PropertyValueFactory<>("reviewerName"));

        TableColumn<GameReviews, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("Score"));

        TableColumn<GameReviews, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("Comment"));

        table.getColumns().addAll(reviewerNameCol,ratingCol,commentCol);
        table.getItems().addAll(dbOps.viewGameReviews(name));
    }

    public TableView showGamePlatforms(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterGamePlatformTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    public void enterGamePlatformTableData(TableView table,String name)
    {
        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GamePlatforms, String> platformNameCol = new TableColumn<>("Platform Name");
        platformNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(platformNameCol);
        table.getItems().addAll(dbOps.getGamePlatform(name));
    }
}
