## JavaFX Setup with IntelliJ & Add Driver

Documentation: https://openjfx.io/openjfx-docs/

Download javaFX: https://gluonhq.com/products/javafx/ 

I used "JavaFX Mac OS X SDK" version 11.0.2 with Java 11, download and upzip it and get in to /lib then copy the path.

1. File -> Project Structure -> Project, and set the project SDK to 11 or 15 (java version currently you are using)

2. Create JavaFX library - File -> Project Structure -> Libraries and add the JavaFX SDK (the path of '..../lib' that copied) as a library to the project. Apply & OK.

3. Add VM options to remove 'java.lang.RuntimeException' exception - Run -> Edit Configurations -> select 'Main' under 'Application' -> 

In 'VM options:' -> insert command, the /lib path that copied previously.
```
Linux/Mac:    --module-path /path/to/javafx-sdk-11.0.1/lib --add-modules javafx.controls,javafx.fxml
Window:       --module-path "\path\to\javafx-sdk-11.0.1\lib" --add-modules javafx.controls,javafx.fxml
```

4. Add Driver: File -> Project Structure -> Modules -> Add -> JARS or Directories... -> select your sql version .Jar file

5. Run -> Run 'Main'

If Error 'ONLY_FULL_GROUP_BY' pop up.
Run this query in mysql to disable ONLY_FULL_GROUP_BY
```
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```

## Application Features
- [x] GUI - User inputs URL, DB username, password, driver (game_db need to be ready in mysql)
- [x] List All Games, List All Publisher, List All Reviewers.
- [ ] Filter Games by genre, rating, platform. (Query 1~3).
- [ ] Filter Reviews by Reviewers name (Query 4).
- [ ] Add a Game, Add a Publisher, Add a Reviewer, Add a Review (Query 5).
- [ ] Update A Game (Query 6).
- [ ] Delete A Game (Query 7).