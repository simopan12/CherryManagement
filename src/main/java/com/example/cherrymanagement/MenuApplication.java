package com.example.cherrymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MenuApplication extends Application {
    private static Database database;

    public static Database getDatabase() {
        return database;
    }

    @Override
    public void start(Stage stage) throws IOException {
        StageManager.initialise(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(MenuApplication.class.getResource("SignInPage.fxml"));
        Parent root= fxmlLoader.load();
        Parent root = fxmlLoader.load();
        stage.getIcons().add(new Image("/image/cherryimage.jpg"));

        //Ottenere il controller dalla scena principale
        SignInController signInController = fxmlLoader.getController();
        signInController.setStage(stage);

        //mostra la scena principale
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
        Scene scene = new Scene(root);
        StageManager.setScene(scene);
    }


    public static void main(String[] args) throws SQLException {
        database =new Database("jdbc:sqlserver://localhost:1433;database=CherryManagementDB","sa","Nicolo23.");
    public static void main(String[] args) {
        database = new Database(resolveDefaultDatabaseUrl());
        launch();
    }

    private static String resolveDefaultDatabaseUrl() {
        try {
            Path dataDirectory = Paths.get(System.getProperty("user.home"), ".cherrymanagement");
            Files.createDirectories(dataDirectory);
            Path databaseFile = dataDirectory.resolve("cherrymanagement.db");
            return "jdbc:sqlite:" + databaseFile.toAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to prepare application data directory", e);
        }
    }
}