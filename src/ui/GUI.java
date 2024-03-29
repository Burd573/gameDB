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
import src.*;

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

    DBOperations dbOps;


    @Override
    public void start(Stage primaryStage)
    {
        enterDBInfo();
    }

    /**
     * Get the database and driver information from the user. Store it in a hashmap
     * so that we can easily access/change information if needed
     */
    public void enterDBInfo()
    {
        Stage stage = new Stage();
        VBox vbox = new VBox();
        GridPane gridpane = new GridPane();

        //Create labels to prompt user what they need to enter
        Label dbUrlLabel = new Label("Enter the url to the database: ");
        Label uNameLabel = new Label("Enter the username: ");
        Label pwdLabel = new Label("Enter password: ");
        Label driverUrlLabel = new Label("Enter the url for the driver: ");

        //Create textfield for user to enter information
        TextField dbURLField = new TextField();
        TextField uNameField = new TextField();
        //change to password field later
        PasswordField pwdField = new PasswordField();
        TextField driverURLField = new TextField();

        //Add the labels and textfields to the gridpane at their appropriate spots
        gridpane.add(dbUrlLabel,0,0);
        gridpane.add(uNameLabel,0,1);
        gridpane.add(pwdLabel,0,2);
        gridpane.add(driverUrlLabel,0,3);

        gridpane.add(dbURLField,1,0);
        gridpane.add(uNameField,1,1);
        gridpane.add(pwdField,1,2);
        gridpane.add(driverURLField,1,3);

        //Create button for user to press after entering their information
        Button okButton = new Button("OK");
        okButton.setPrefWidth(50);

        //add the button and gridpane to the vbox and adjust where they are
        // located in the vbox
        vbox.getChildren().addAll(gridpane,okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(250,150);
        VBox.setMargin(gridpane,new Insets(10,10,10,10));

        //add the vbox to scene which gets added to the stage.
        Scene scene = new Scene(vbox);
        stage.setScene(scene);

        //show the stage
        stage.show();

        //When the user enters their information, store their inputs
        //in the hashmap
        okButton.setOnAction(e -> {
            dbInput.put(url,dbURLField.getText());
            dbInput.put(uName,uNameField.getText());
            dbInput.put(pwd,pwdField.getText());
            dbInput.put(driverUrl,driverURLField.getText());
            connectToDB();

            //after the button is pressed, hide the stage
            stage.hide();
        });
    }

    /**
     * Use the user input to connect to the database. Retrieve user input from hashmap
     * and use that information to establish connection with the database
     */
    public void connectToDB()
    {
        //Get info from hashmap
        String _url = dbInput.get("DbUrl");
        String userName = dbInput.get("username");
        String pwd = dbInput.get("password");

        try {
            //Establish connection to database
            Class.forName(dbInput.get("driverUrl"));
            conn = DriverManager.getConnection(_url, userName, pwd);
            dbOps = new DBOperations(conn);
            showMainMenu();
        //Print error messages showing what went wrong. Should probably be GUI pop-ups
        }  catch(ClassNotFoundException e)
        {
            System.out.println("Incorrect Driver URL");
        } catch(SQLNonTransientConnectionException e)
        {
            System.out.println("Incorrect Login Info");
        }catch (SQLException e)
        {
            System.out.println("Incorrect Database URL");
        }
    }

    /**
     * Show the main menu after establishing connection to database. Add menu buttons
     * to the left and a section to the right to view query results as tables
     */
    public void showMainMenu()
    {
        //Hbox will store everything in the menu
        HBox hbox = new HBox();
        hbox.setPrefWidth(600);
        //Menu will have two columns of buttons stored in an hbox
        VBox col1Buttons = new VBox();
        VBox col2Buttons = new VBox();
        HBox buttons = new HBox();
        //wrapper pane will be what the tables are displayed in
        //used wrapper pane for easy clearing of old table
        //and showing new tables. Bound to the HBox so it changes
        //size with the stage
        Pane wrapperPane = new Pane();
        wrapperPane.prefWidthProperty().bind(hbox.widthProperty());

        //Set the main hbox in the scene and the scene in the stage
        Stage stage = new Stage();
        stage.setMinWidth(600);
        stage.setMinHeight(390);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);

        //Make a bunch of menu buttons.
        //List X lists general info about all X's
        //X Info allows user to get info about a specific X
        //Add X adds X to database
        //Edit Data allows user to edit specific items in DB
        //Delete data allows user to delete specific data from DB
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

        //Add Buttons to their columns.
        col1Buttons.getChildren().addAll(gameInfoButton,publisherInfoButton, reviewerInfoButton, platformInfoButton, genreInfoButton, deleteDataButton, editDataButton);
        col2Buttons.getChildren().addAll(showGamesButton,showPublisherButton,showReviewersButton,addGamesButton,addPublisherButton,addReviewersButton,addReviewButton);
        //Add Columns of buttons to their hbox
        buttons.getChildren().addAll(col1Buttons,col2Buttons);

        //Add the hbox of buttons to the main hbox
        hbox.getChildren().addAll(buttons,wrapperPane);

        //Every time a button is presses, the current table is cleared away
        //and the relevant table is shown
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
            wrapperPane.getChildren().add(selectGameAdd(wrapperPane));
        });

        platformInfoButton.setOnAction( e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(platformInfo(wrapperPane));
        });

        publisherInfoButton.setOnAction( e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(publisherInfo(wrapperPane));
        });

        genreInfoButton.setOnAction( e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(genreInfo(wrapperPane));
        });

        reviewerInfoButton.setOnAction( e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(reviewerInfo(wrapperPane));
        });

        editDataButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(editData(wrapperPane));
        });

        deleteDataButton.setOnAction(e -> {
            stage.sizeToScene();
            wrapperPane.getChildren().clear();
            wrapperPane.getChildren().add(deleteData(wrapperPane));
        });

        stage.setOnCloseRequest(e -> {
            dbOps.close();
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }catch(SQLException ex)
            {
                ex.printStackTrace();
            }
        });

        //show the stage containing all elements of main menu
        stage.show();
    }

    /**
     * Shows the table containing general info about all of the games in the DB
     *
     * @param wrapper wrapper class that the table is placed into.
     * @return relevant table
     */
    public TableView showGames(Pane wrapper)
    {
        TableView table = new TableView();

        enterGameTableData(table);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Shows the table containing general info about all of the publishers in the DB
     *
     * @param wrapper wrapper class that the table is placed into.
     * @return relevant table
     */
    public TableView showPublishers(Pane wrapper)
    {
        TableView table = new TableView();

        enterPublisherTableData(table);
        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());

        return table;
    }

    /**
     * Shows the table containing general info about all of the reviewers in the DB
     *
     * @param wrapper wrapper class that the table is placed into.
     * @return relevant table
     */
    public TableView showReviewers(Pane wrapper)
    {
        TableView table = new TableView();

        enterReviewerTableData(table);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());

        return table;
    }

    /**
     * Inserts general data about all games in the DB. Places all of the data
     * pulled from query into the table. Execute query through
     * dbOps, create columns in table, and then place data into rows. Placing data
     * into rows uses Getter method from relevant object so the call needs to be
     * specific to the object we are getting data from (i.e. GameInfo)
     *
     * @param table table we are placing data into
     */
    public void enterGameTableData(TableView table)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameList, String> gameNameCol = new TableColumn<>("Game Name");
        gameNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameList, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<GameList, Integer> relYearCol = new TableColumn<>("Release Year");
        //this uses the getter method getReleaseYear() from GameInfo Object so it must
        //be syntax-specific. Cannot use release_year or releaseyear because it looks
        //specifically for getReleaseYear() from gameInfo Object
        relYearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        TableColumn<GameList, String> pubNameCol = new TableColumn<>("Publisher Name");
        pubNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameList, Double> ratingCol = new TableColumn<>("Average Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("AvgReview"));

        table.getColumns().addAll(gameNameCol,genreCol,relYearCol,pubNameCol,ratingCol);
        table.getItems().addAll(dbOps.getGames());

    }

    /**
     * Inserts general data about all publishers in the DB. Places all of the data
     * pulled from query into the table. Execute query through
     * dbOps, create columns in table, and then place data into rows. Placing data
     * into rows uses Getter method from relevant object so the call needs to be
     * specific to the object we are getting data from (i.e. PublisherInfo)
     *
     * @param table table we are placing data into
     */
    public void enterPublisherTableData(TableView table)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameList, String> pubNameCol = new TableColumn<>("Publisher Name");
        pubNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameList, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("City"));

        TableColumn<GameList, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));

        TableColumn<GameList, String> stateCol = new TableColumn<>("State");
        //this uses the getter method getState from PublisherInfo Object so it must
        //be syntax-specific. Cannot use pub_state or state because it looks
        //specifically for getState from PublisherInfo Object
        stateCol.setCellValueFactory(new PropertyValueFactory<>("State"));

        TableColumn<GameList, Double> avgRatingCol = new TableColumn<>("Average Game Rating");
        avgRatingCol.setCellValueFactory(new PropertyValueFactory<>("AvgRating"));

        table.getColumns().addAll(pubNameCol,cityCol,countryCol,stateCol,avgRatingCol);
        table.getItems().addAll(dbOps.getPublishers());

    }

    /**
     * Inserts general data about all reviewers in the DB. Places all of the data
     * pulled from query into the table. Execute query through
     * dbOps, create columns in table, and then place data into rows. Placing data
     * into rows uses Getter method from relevant object so the call needs to be
     * specific to the object we are getting data from (i.e. ReviewerInfo)
     *
     * @param table table we are placing data into
     */
    public void enterReviewerTableData(TableView table)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameList, String> reviewerNameCol = new TableColumn<>("Reviewer Name");
        reviewerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<GameList, Double> avgReviewCol = new TableColumn<>("Average Review");
        //this uses the getter method getAvgRating from ReviewerInfo Object so it must
        //be syntax-specific. Cannot use avg_rating or avgrating becuase it looks
        //speficially for getAvgRating() from ReviewerInfo Object
        avgReviewCol.setCellValueFactory(new PropertyValueFactory<>("avgRating"));

        TableColumn<GameList, Integer> numReviewsCol = new TableColumn<>("Number of Reviews");
        numReviewsCol.setCellValueFactory(new PropertyValueFactory<>("numReviews"));

        table.getColumns().addAll(reviewerNameCol,avgReviewCol,numReviewsCol);
        table.getItems().addAll(dbOps.getReviewers());
    }

    /**
     * Menu to add a game to the DB. User selects from a list of publishers
     * currently in the DB and enters new game info including the name, genre,
     * and release year of the game.
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing menu
     */
    public VBox addGame(Pane wrapper)
    {
        VBox vbox = new VBox();
        //Labels instructing the user which data to enter
        Label gameName = new Label("Game Name");
        Label gameGenre = new Label("Game Genre");
        Label releaseYear = new Label("Release Year");
        Label pubName = new Label("Publisher");

        //Get a list of the publishers currently in the DB
//        DBOperations dbOps = new DBOperations(conn);
        List<String> names = dbOps.getPublisherNames();

        //Textfields for users to enter data
        TextField gameNameInput = new TextField();
        gameNameInput.setMinWidth(200);

        TextField gameGenreInput = new TextField();
        gameGenreInput.setMinWidth(200);

        TextField releaseYearInput = new TextField();
        releaseYearInput.setMinWidth(200);

        //The user must select from a publisher already stored in the DB
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(names));

        //Add labels, textfields and buttons to the vbox
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

        //When the button is clicked, all new game info is inserted into the DB
        enterButton.setOnAction(e -> {
            dbOps.addGame(comboBox.getValue(),gameGenreInput.getText(),gameNameInput.getText(),releaseYearInput.getText());
            gameGenreInput.clear();
            gameNameInput.clear();
            releaseYearInput.clear();
        });

        //Adjust spacing/margins of items
        vbox.getChildren().addAll(nameBox,genreBox,yearBox,pubBox,enterButton);
        vbox.setSpacing(20);
        VBox.setMargin(nameBox,new Insets(15, 40, 0, 40));
        VBox.setMargin(genreBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(yearBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(pubBox,new Insets(10, 40, 15, 40));
        vbox.setAlignment(Pos.CENTER);

        //bind vbox to parent element
        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }


    /**
     * Menu to add a publisher to the DB.
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing menu
     */
    public VBox addPublisher(Pane wrapper)
    {
        VBox vbox = new VBox();
//        DBOperations dbOps = new DBOperations(conn);

        //Labels instructing the user which data to enter
        Label pubName = new Label("Publisher Name");
        Label pubCity = new Label("Publisher City");
        Label pubState = new Label("Publisher State");
        Label pubCountry = new Label("Publisher Country");

        //Textfields for users to enter data
        TextField pubNameInput = new TextField();
        pubNameInput.setMinWidth(200);

        TextField pubCityInput = new TextField();
        pubCityInput.setMinWidth(200);

        TextField pubStateInput = new TextField();
        pubStateInput.setMinWidth(200);

        TextField pubCountryInput = new TextField();
        pubCountryInput.setMinWidth(200);

        //Add labels, textfields and buttons to the vbox
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
        //When the button is clicked, all new publisher info is inserted into the DB
        enterButton.setOnAction(e -> {
            dbOps.addPublisher(pubNameInput.getText(),pubCityInput.getText(),pubStateInput.getText(),pubCountryInput.getText());
            pubNameInput.clear();
            pubCityInput.clear();
            pubStateInput.clear();
            pubCountryInput.clear();
        });

        vbox.getChildren().addAll(nameBox,cityBox,stateBox,countryBox,enterButton);

        vbox.setSpacing(20);
        VBox.setMargin(nameBox,new Insets(15, 40, 0, 40));
        VBox.setMargin(cityBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(stateBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(countryBox,new Insets(10, 40, 15, 40));
        vbox.setAlignment(Pos.CENTER);

        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }

    /**
     * Menu to add a Reviewer to the DB.
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing menu
     */
    public VBox addReviewer(Pane wrapper)
    {
        VBox vbox = new VBox();

//        DBOperations dbOps = new DBOperations(conn);

        Label reviewerName = new Label("Reviewer Name");

        TextField reviewerNameInput = new TextField();
        reviewerNameInput.setMinWidth(200);

        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(reviewerName,reviewerNameInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.addReviewer(reviewerNameInput.getText());
            reviewerNameInput.clear();
        });

        vbox.getChildren().addAll(nameBox,enterButton);
        vbox.setSpacing(30);
        VBox.setMargin(nameBox,new Insets(30,40,10,40));
        vbox.setAlignment(Pos.CENTER);

        vbox.prefHeightProperty().bind(wrapper.heightProperty());
        vbox.prefWidthProperty().bind(wrapper.widthProperty());

        return vbox;
    }

    /**
     * Menu to add a review to the DB. The reviewer and game must be in the DB
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing menu
     */
    public VBox addReview(Pane wrapper)
    {
        VBox vBox = new VBox();

//        DBOperations dbOps = new DBOperations(conn);
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
            ratingInput.clear();
            commentInput.clear();
        });

        vBox.getChildren().addAll(reviewerNameBox,gameNameBox,ratingBox,commentBox,enterButton);

        vBox.setSpacing(20);
        VBox.setMargin(reviewerNameBox,new Insets(15, 40, 0, 40));
        VBox.setMargin(gameNameBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(ratingBox,new Insets(10, 40, 0, 40));
        VBox.setMargin(commentBox,new Insets(10, 40, 15, 40));
        vBox.setAlignment(Pos.CENTER);

        vBox.prefHeightProperty().bind(wrapper.heightProperty());
        vBox.prefWidthProperty().bind(wrapper.widthProperty());

        return vBox;
    }

    /**
     * Select a specific game in the DB to view information about
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing menu
     */
    public VBox selectGameAdd(Pane wrapper)
    {
        VBox getSelection = new VBox();

//        DBOperations dbOps = new DBOperations(conn);
        List<String> gameNames = dbOps.getGameNames();
        Label gameName = new Label("Select Game");
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
        VBox.setMargin(nameBox,new Insets(30,40,10,40));
        getSelection.setAlignment(Pos.CENTER);

        getSelection.prefHeightProperty().bind(wrapper.heightProperty());
        getSelection.prefWidthProperty().bind(wrapper.widthProperty());

        return getSelection;
    }

    /**
     * Select the information that you wish to view about a selected game
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name game that the information is related to
     * @return vbox containing the menu
     */
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

    /**
     * Show the reviews for the selected game. Includes reviewer, score, and comment
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name game that the information is related to
     * @return vbox containing the menu
     */
    public TableView showGameReviews(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterGameReviewTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Enter the review data for specific game into table
     *
     * @param table table data is inserted into
     * @param name of the game reviewed
     */
    public void enterGameReviewTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GameReviewsInfo, String> reviewerNameCol = new TableColumn<>("Reviewer Name");
        reviewerNameCol.setCellValueFactory(new PropertyValueFactory<>("reviewerName"));

        TableColumn<GameReviewsInfo, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("Score"));

        TableColumn<GameReviewsInfo, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("Comment"));

        table.getColumns().addAll(reviewerNameCol,ratingCol,commentCol);
        table.getItems().addAll(dbOps.viewGameReviews(name));
    }

    /**
     * Show the platforms that a specific game is available on
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name game that the information is related to
     * @return vbox containing the menu
     */
    public TableView showGamePlatforms(Pane wrapper,String name)
    {
        var table = new TableView();

        enterGamePlatformTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Enter the platform data for specific game into table
     *
     * @param table table data is inserted into
     * @param name of the game reviewed
     */
    public void enterGamePlatformTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<PlatformGamesInfo, String> platformNameCol = new TableColumn<>("Platform Name");
        platformNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(platformNameCol);
        table.getItems().addAll(dbOps.getGamePlatforms(name));
    }

    /**
     * Menu to select a platform. We can view all games available on the
     * selected platform
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing the menu
     */
    public VBox platformInfo(Pane wrapper)
    {
        VBox ret = new VBox();
        HBox select = new HBox();

//        DBOperations dbOps = new DBOperations(conn);
        List<String> platformNames = dbOps.getPlatformNames();

        Label choiceSelection = new Label("Select Platform");
        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(platformNames));

        Button okButton = new Button("OK");

        select.getChildren().addAll(choiceSelection,choice,okButton);

        okButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(showPlatformGames(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select);
        return ret;
    }

    /**
     * Show the games that a specific platform has available
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name game that the information is related to
     * @return vbox containing the menu
     */
    public TableView showPlatformGames(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterPlatformGamesTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Enter the game data for specific platform into table
     *
     * @param table table data is inserted into
     * @param name of the game reviewed
     */
    public void enterPlatformGamesTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<PlatformGamesInfo, String> gameNameCol = new TableColumn<>("Game Name");
        gameNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(gameNameCol);
        table.getItems().addAll(dbOps.getPlatformGames(name));
    }

    /**
     * Menu to select a reviewer. We can view all reviews made by that reviewer
     * selected reviewer name
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return vbox containing the menu
     */
    public VBox reviewerInfo(Pane wrapper)
    {
        VBox ret = new VBox();
        HBox select = new HBox();

//        DBOperations dbOps = new DBOperations(conn);
        List<String> reviewerNames = dbOps.getReviewerNames();

        Label choiceSelection = new Label("Select Reviewer");
        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));

        Button okButton = new Button("OK");

        select.getChildren().addAll(choiceSelection,choice,okButton);

        okButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(showReviewerReviews(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select);
        return ret;
    }

    /**
     * Show all reviews that made by a specific reviewer
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name game that the information is related to
     * @return vbox containing the menu
     */
    public TableView showReviewerReviews(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterReviewerReviewsTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Enter the reviews data for specific reviewer into table
     *
     * @param table table data is inserted into
     * @param name of the reviewer
     */
    public void enterReviewerReviewsTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<ReviewerInfo, String> reviewerNameCol = new TableColumn<>("Game Name");
        reviewerNameCol.setCellValueFactory(new PropertyValueFactory<>("GameName"));

        TableColumn<ReviewerInfo, String> reviewCol = new TableColumn<>("Review");
        reviewCol.setCellValueFactory(new PropertyValueFactory<>("review"));

        TableColumn<ReviewerInfo, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.getColumns().addAll(reviewerNameCol,reviewCol,ratingCol);
        table.getItems().addAll(dbOps.getReviewerInfo(name));
    }

    /**
     * Menu for selecting a specific publisher to view
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return menu for selecting publisher
     */
    public VBox publisherInfo(Pane wrapper)
    {
        VBox ret = new VBox();
        HBox select = new HBox();

//        DBOperations dbOps = new DBOperations(conn);
        List<String> pubNames = dbOps.getPublisherNames();

        Label choiceSelection = new Label("Select Publisher");
        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(pubNames));

        Button okButton = new Button("OK");

        select.getChildren().addAll(choiceSelection,choice,okButton);

        okButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(showPublisherGames(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select);
        return ret;
    }

    /**
     * Create table showing all of the games from a specific publisher
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name of publisher
     * @return table with all games from a specific publisher
     */
    public TableView showPublisherGames(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterPublisherGamesTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Insert values into publihser games table
     *
     * @param table to insert values into
     * @param name of publisher
     */
    public void enterPublisherGamesTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<PublisherInfo, String> gameNameCol = new TableColumn<>("Game Name");
        gameNameCol.setCellValueFactory(new PropertyValueFactory<>("GameName"));

        TableColumn<PublisherInfo, String> ratingCol = new TableColumn<>("Average Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("AvgReview"));

        table.getColumns().addAll(gameNameCol,ratingCol);
        table.getItems().addAll(dbOps.getPublisherGames(name));
    }

    /**
     * Menu to select genre to view
     *
     * @param wrapper wrapper class the vbox is placed into
     * @return menu
     */
    public VBox genreInfo(Pane wrapper)
    {
        VBox ret = new VBox();
        HBox select = new HBox();

//        DBOperations dbOps = new DBOperations(conn);
        List<String> genres = dbOps.getGenreNames();

        Label choiceSelection = new Label("Select Genre");
        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(genres));

        Button okButton = new Button("OK");

        select.getChildren().addAll(choiceSelection,choice,okButton);

        okButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(showGenreGames(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select);
        return ret;
    }

    /**
     * Show the table of all games of a specified genre
     *
     * @param wrapper wrapper class the vbox is placed into
     * @param name name of genre
     * @return table showing all games of specified genre
     */
    public TableView showGenreGames(Pane wrapper,String name)
    {
        TableView table = new TableView();

        enterGenreGamesTableData(table,name);

        table.prefHeightProperty().bind(wrapper.heightProperty());
        table.prefWidthProperty().bind(wrapper.widthProperty());
        return table;
    }

    /**
     * Enter games for a specified genre into table
     *
     * @param table to enter games into
     * @param name of genre
     */
    public void enterGenreGamesTableData(TableView table,String name)
    {
//        DBOperations dbOps = new DBOperations(conn);
        TableColumn<GenreInfo, String> genreNameCol = new TableColumn<>("Game Name");
        genreNameCol.setCellValueFactory(new PropertyValueFactory<>("GameName"));

        TableColumn<PublisherInfo, String> ratingCol = new TableColumn<>("Average Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("AvgReview"));

        table.getColumns().addAll(genreNameCol,ratingCol);
        table.getItems().addAll(dbOps.getGenreInfo(name));
    }

    public VBox editData(Pane wrapper)
    {
        VBox ret = new VBox();
        //Labels instructing the user which data to enter
        Label selectionLabel = new Label("Select what you would like to edit");
        String game = "Game";
        String publisher = "Publisher";
        String reviewer = "Reviewer";
        String review = "Review";
//        String platform = "Platform";

        List<String> options = new ArrayList<>();
        options.add(game);
        options.add(publisher);
        options.add(reviewer);
        options.add(review);
//        options.add(platform);

        //The user must select from a publisher already stored in the DB
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(options));

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        //Add labels, textfields and buttons to the vbox
        VBox selectionBox = new VBox();
        selectionBox.getChildren().addAll(selectionLabel,comboBox);

        //When the button is clicked, all new game info is inserted into the DB
        enterButton.setOnAction(e -> {
            switch (comboBox.getValue())
            {
                case "Game":
                    ret.getChildren().clear();
                    ret.getChildren().add(editGameData(wrapper));
                    break;
                case "Publisher":
                    ret.getChildren().clear();
                    ret.getChildren().add(editPublisherData(wrapper));
                    break;
                case "Reviewer":
                    ret.getChildren().clear();
                    ret.getChildren().add(editReviewerData(wrapper));
                    break;
                case "Review":
                    ret.getChildren().clear();
                    ret.getChildren().add(selectReviewer(wrapper));
                    break;
            }

        });

        //Adjust spacing/margins of items
        ret.getChildren().addAll(selectionBox,enterButton);
        ret.setSpacing(20);
        ret.setMargin(selectionBox,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        //bind vbox to parent element
        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }

    public VBox editGameData(Pane wrapper)
    {
        VBox ret = new VBox();
        List<String> gameNames = dbOps.getGameNames();

        Label selectionLabel = new Label("Select game you would like to edit");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(gameNames));
        Button editButton = new Button("Edit");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(enterNewGameData(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox enterNewGameData(Pane wrapper, String gameName)
    {
        VBox ret = new VBox();
        Label nameLabel = new Label("Game Name");
        Label genreLabel = new Label("Game Genre");

        TextField gameNameInput = new TextField();
        gameNameInput.setMinWidth(200);

        TextField gameGenreInput = new TextField();
        gameGenreInput.setMinWidth(200);

        //Add labels, textfields and buttons to the vbox
        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,gameNameInput);

        VBox genreBox = new VBox();
        genreBox.getChildren().addAll(genreLabel,gameGenreInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.updateGame(gameName,gameNameInput.getText(),gameGenreInput.getText());
            gameNameInput.clear();
            gameGenreInput.clear();
        });

        ret.getChildren().addAll(nameBox,genreBox,enterButton);

        ret.setSpacing(20);
        ret.setMargin(nameBox,new Insets(15, 40, 0, 40));
        ret.setMargin(genreBox,new Insets(10, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }

    public VBox editPublisherData(Pane wrapper)
    {
        List<String> pubNames = dbOps.getPublisherNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select publisher you would like to edit");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(pubNames));
        Button editButton = new Button("Edit");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(enterNewPublisherData(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox enterNewPublisherData(Pane wrapper, String pubName)
    {
        VBox ret = new VBox();
        Label nameLabel = new Label("Publisher Name");
        Label cityLabel = new Label("Publisher City");
        Label stateLabel = new Label("Publisher State");
        Label countryLabel = new Label("Publisher Country");

        TextField pubNameInput = new TextField();
        pubNameInput.setMinWidth(200);

        TextField pubCityInput = new TextField();
        pubCityInput.setMinWidth(200);

        TextField pubStateInput = new TextField();
        pubStateInput.setMinWidth(200);

        TextField pubCountryInput = new TextField();
        pubCountryInput.setMinWidth(200);

        //Add labels, textfields and buttons to the vbox
        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,pubNameInput);

        VBox cityBox = new VBox();
        cityBox.getChildren().addAll(cityLabel,pubCityInput);

        VBox stateBox = new VBox();
        stateBox.getChildren().addAll(stateLabel,pubStateInput);

        VBox countryBox = new VBox();
        countryBox.getChildren().addAll(countryLabel,pubCountryInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.updatePublisher(pubName,pubNameInput.getText(),pubCityInput.getText(),pubStateInput.getText(),pubCountryInput.getText());
            pubNameInput.clear();
            pubCityInput.clear();
            pubStateInput.clear();
            pubCountryInput.clear();
        });

        ret.getChildren().addAll(nameBox,cityBox,stateBox,countryBox,enterButton);

        ret.setSpacing(20);
        ret.setMargin(nameBox,new Insets(15, 40, 0, 40));
        ret.setMargin(cityBox,new Insets(10, 40, 0, 40));
        ret.setMargin(stateBox,new Insets(10, 40, 0, 40));
        ret.setMargin(countryBox,new Insets(10, 40, 15, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }

    public VBox editReviewerData(Pane wrapper)
    {
        List<String> reviewerNames = dbOps.getReviewerNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Reviewer you would like to edit");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));
        Button editButton = new Button("Edit");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(enterNewReviewerData(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox enterNewReviewerData(Pane wrapper, String reviewerName)
    {
        VBox ret = new VBox();
        Label nameLabel = new Label("Reviewer Name");

        TextField reviewerNameInput = new TextField();
        reviewerNameInput.setMinWidth(200);

        //Add labels, textfields and buttons to the vbox
        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameLabel,reviewerNameInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.updateReviewer(reviewerName,reviewerNameInput.getText());
            reviewerNameInput.clear();
        });

        ret.getChildren().addAll(nameBox,enterButton);

        ret.setSpacing(20);
        ret.setMargin(nameBox,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }


    public VBox selectReviewer(Pane wrapper)
    {
        List<String> reviewerNames = dbOps.getReviewerNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Reviewer");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));
        Button editButton = new Button("Enter");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(selectGame(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox selectGame(Pane wrapper,String reviewer)
    {
        List<String> gameNames = dbOps.gamesReviewed(reviewer);
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Game");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(gameNames));
        Button editButton = new Button("Edit");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(enterNewReviewData(wrapper,reviewer,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox enterNewReviewData(Pane wrapper, String reviewerName, String gameName)
    {
        VBox ret = new VBox();
        Label scoreLabel = new Label("Score");
        Label commentLabel = new Label("Comment");

        TextField scoreInput = new TextField();
        scoreInput.setMinWidth(200);

        TextField commentInput = new TextField();
        scoreInput.setMinWidth(200);

        //Add labels, textfields and buttons to the vbox
        VBox scoreBox = new VBox();
        scoreBox.getChildren().addAll(scoreLabel,scoreInput);

        VBox commentBox = new VBox();
        commentBox.getChildren().addAll(commentLabel,commentInput);

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        enterButton.setOnAction(e -> {
            dbOps.updateReview(gameName,reviewerName,Integer.parseInt(scoreInput.getText()),commentInput.getText());
            scoreInput.clear();
            commentInput.clear();
        });

        ret.getChildren().addAll(scoreBox,commentBox,enterButton);

        ret.setSpacing(20);
        ret.setMargin(scoreBox,new Insets(15, 40, 0, 40));
        ret.setMargin(commentBox,new Insets(10, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }

    public VBox deleteData(Pane wrapper)
    {
        VBox ret = new VBox();
        //Labels instructing the user which data to enter
        Label selectionLabel = new Label("Select what you would like to Remove");
        String game = "Game";
        String publisher = "Publisher";
        String reviewer = "Reviewer";
        String review = "Review";

        List<String> options = new ArrayList<>();
        options.add(game);
        options.add(publisher);
        options.add(reviewer);
        options.add(review);

        //The user must select from a publisher already stored in the DB
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(options));

        Button enterButton = new Button("Enter");
        enterButton.setMinWidth(50);

        //Add labels, textfields and buttons to the vbox
        VBox selectionBox = new VBox();
        selectionBox.getChildren().addAll(selectionLabel,comboBox);

        //When the button is clicked, all new game info is inserted into the DB
        enterButton.setOnAction(e -> {
            switch (comboBox.getValue())
            {
                case "Game":
                    ret.getChildren().clear();
                    ret.getChildren().add(removeGame(wrapper));
                    break;
                case "Publisher":
                    ret.getChildren().clear();
                    ret.getChildren().add(removePublisher(wrapper));
                    break;
                case "Reviewer":
                    ret.getChildren().clear();
                    ret.getChildren().add(removeReviewer(wrapper));
                    break;
                case "Review":
                    ret.getChildren().clear();
                    ret.getChildren().add(removeReview(wrapper));
                    break;
            }

        });

        //Adjust spacing/margins of items
        ret.getChildren().addAll(selectionBox,enterButton);
        ret.setSpacing(20);
        ret.setMargin(selectionBox,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        //bind vbox to parent element
        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());

        return ret;
    }

    public VBox removeGame(Pane wrapper)
    {
        VBox ret = new VBox();
        List<String> gameNames = dbOps.getGameNames();

        Label selectionLabel = new Label("Select game you would like to remove");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(gameNames));
        Button editButton = new Button("Remove");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            dbOps.removeGame(choice.getValue());
            ret.getChildren().clear();
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox removePublisher(Pane wrapper)
    {
        List<String> pubNames = dbOps.getPublisherNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select publisher you would like to remove");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(pubNames));
        Button editButton = new Button("Remove");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            dbOps.removePublisher(choice.getValue());
            ret.getChildren().clear();
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox removeReviewer(Pane wrapper)
    {
        List<String> reviewerNames = dbOps.getReviewerNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Reviewer You wish to remove");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));
        Button editButton = new Button("Remove");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            dbOps.removeReviewer(choice.getValue());
            ret.getChildren().clear();
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox removeReview(Pane wrapper)
    {
        List<String> reviewerNames = dbOps.getReviewerNames();
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Reviewer");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(reviewerNames));
        Button editButton = new Button("Enter");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            ret.getChildren().clear();
            ret.getChildren().addAll(selectGameForDeletion(wrapper,choice.getValue()));
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

    public VBox selectGameForDeletion(Pane wrapper,String reviewer)
    {
        List<String> gameNames = dbOps.gamesReviewed(reviewer);
        VBox ret = new VBox();
        Label selectionLabel = new Label("Select Game");

        ComboBox<String> choice = new ComboBox<>(FXCollections.observableArrayList(gameNames));
        Button editButton = new Button("Remove");
        VBox select = new VBox();

        select.getChildren().addAll(selectionLabel,choice);

        editButton.setOnAction(e -> {
            dbOps.removeReview(reviewer,choice.getValue());
            ret.getChildren().clear();
        });

        ret.getChildren().addAll(select,editButton);

        ret.setSpacing(20);
        ret.setMargin(select,new Insets(15, 40, 0, 40));
        ret.setAlignment(Pos.CENTER);

        ret.prefHeightProperty().bind(wrapper.heightProperty());
        ret.prefWidthProperty().bind(wrapper.widthProperty());


        return ret;
    }

}
