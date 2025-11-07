package com.example.cherrymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private TextField nomeUtenteField;
    @FXML private TextField cognomeUtenteField;
    @FXML private TextField aziendaField;

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public TextField getNomeUtenteField() {
        return nomeUtenteField;
    }

    public TextField getCognomeUtenteField() {
        return cognomeUtenteField;
    }

    public TextField getAziendaField() {
        return aziendaField;
    }



    private static Stage stage;


    public void setStage(Stage stage){
        this.stage=stage;
    }


    @FXML private void handleRegisterButtonAction() {
        MenuApplication.getDatabase().SignUp(getUsernameField(),getPasswordField(),getNomeUtenteField(),getCognomeUtenteField(),getAziendaField());

    }

    @FXML
    public void navigateToSignInPage(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("SignInPage.fxml"));
            Parent root = fxmlLoader.load();


            SignInController signInController =fxmlLoader.getController();
            signInController.setStage(SignInController.getStage());


            Scene scene = new Scene(root);
            SignInController.getStage().setScene(scene);
        }catch (Exception e){
            System.out.println("Errore");
            StageManager.setScene(scene);
        }catch (IOException e){
            throw new IllegalStateException("Impossibile caricare la schermata di accesso", e);
        }
    }



}