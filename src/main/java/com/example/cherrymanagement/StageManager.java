package com.example.cherrymanagement;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Centralises access to the primary {@link Stage} so controllers no longer need to
 * stash static references to the JavaFX window.  This keeps navigation logic in a
 * single, well defined place and avoids hard-to-debug {@link NullPointerException}s
 * when controllers are created by the FXML loader.
 */
public final class StageManager {

    private static Stage primaryStage;

    private StageManager() {
    }

    public static void initialise(Stage stage) {
        primaryStage = Objects.requireNonNull(stage, "stage must not be null");
    }

    public static Stage getStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("StageManager has not been initialised");
        }
        return primaryStage;
    }

    public static void setScene(Scene scene) {
        Stage stage = getStage();
        stage.setScene(scene);
        stage.show();
    }
}