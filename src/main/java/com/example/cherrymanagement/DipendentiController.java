package com.example.cherrymanagement;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DipendentiController {
    private Stage stage;

    @FXML private TableView<Dipendente> dipendenteTable;
    @FXML private TableColumn<Dipendente,String> cfColumn;
    @FXML private TableColumn<Dipendente,String> nomeColumn;
    @FXML private TableColumn<Dipendente,String> cognomeColumn;
    @FXML private TableColumn<Dipendente,String> mansioneColumn;
    @FXML private TableColumn<Dipendente,Double> pagaColumn;
    @FXML private TableColumn<Dipendente, Double> oreColumn;
    @FXML private TableColumn<Dipendente,Double> stipendioColumn;

    @FXML public Label spesaTotaleLabel=new Label();


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize(){

        cfColumn.setCellValueFactory(new PropertyValueFactory<>("cf"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cognomeColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        mansioneColumn.setCellValueFactory(new PropertyValueFactory<>("mansione"));
        pagaColumn.setCellValueFactory(new PropertyValueFactory<>("paga"));
        oreColumn.setCellValueFactory(new PropertyValueFactory<>("ore"));
        stipendioColumn.setCellValueFactory(new PropertyValueFactory<>("stipendio"));


        dipendenteTable.setItems(getDipendenteData());
        dipendenteTable.getSelectionModel().selectedItemProperty();
        spesaTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getDipendentiStipendi()));
    }

    private ObservableList<Dipendente> getDipendenteData() {
        ObservableList<Dipendente> dipendenti = MenuApplication.getDatabase().getDipendenti(MenuApplication.getDatabase().getUsername_utente());;
        spesaTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getDipendentiStipendi()));
        return dipendenti;
    }


    int selectedIndex() {
        int selectedIndex = dipendenteTable.getSelectionModel().getSelectedIndex();
public class DipendentiController {
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if(!controller.getDipendente().getCf().equals("")  && !controller.getDipendente().getNome().equals("")
                      && !controller.getDipendente().getCognome().equals("") && !controller.getDipendente().getMansione().equals("")
                        && controller.getDipendente().getPaga()>0) {

                      MenuApplication.getDatabase().executeUpdate("INSERT INTO Dipendenti(CF,Nome,Cognome,Mansione,Paga,Ore,Username_utente) VALUES(?,?,?,?,?,?,?)",
                            controller.getDipendente().getCf(),controller.getDipendente().getNome(),controller.getDipendente().getCognome(),
                            controller.getDipendente().getMansione(),controller.getDipendente().getPaga(),
                            controller.getDipendente().getOre(),MenuApplication.getDatabase().getUsername_utente());

                    dipendenteTable.setItems(getDipendenteData());
                    spesaTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getDipendentiStipendi()));
                }else{
                  Alert alert= new Alert(Alert.AlertType.WARNING);
                   alert.setTitle("Attenzione");
                  alert.setHeaderText("Non puoi lasciare un campo vuoto");
                   alert.showAndWait();
                   handleNewDipendente();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di inserimento dei dipendenti", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il salvataggio del dipendente", e);
        }
    }

    @FXML
    public void handleEditDipendente() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DipendentiEditView.fxml"));
            DialogPane view = loader.load();
            DipendentiEditController controller = loader.getController();

            int selectedIndex = selectedIndex();
            controller.setDipendente(new Dipendente(dipendenteTable.getItems().get(selectedIndex)));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Dipendente");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            DialogPane dialogPane = dialog.getDialogPane();

            Dipendente selectedDipendente = dipendenteTable.getItems().get(selectedIndex);

            TextField cf = (TextField)dialogPane.lookup("#cfField");
            cf.setText(selectedDipendente.getCf());
public class DipendentiController {
                if(!controller.getDipendente().getCf().equals("") && !controller.getDipendente().getNome().equals("")
                        && !controller.getDipendente().getCognome().equals("") && !controller.getDipendente().getMansione().equals("")
                        && controller.getDipendente().getPaga()>0) {

                    MenuApplication.getDatabase().executeUpdate("DELETE FROM Dipendenti WHERE CF = ?",selectedDipendente.getCf());
                    MenuApplication.getDatabase().executeUpdate("INSERT INTO Dipendenti(CF,Nome,Cognome,Mansione,Paga,Ore,Username_utente) VALUES(?,?,?,?,?,?,?)",
                            controller.getDipendente().getCf(),controller.getDipendente().getNome(),controller.getDipendente().getCognome(),
                            controller.getDipendente().getMansione(),controller.getDipendente().getPaga(),
                            controller.getDipendente().getOre(),MenuApplication.getDatabase().getUsername_utente());

                    dipendenteTable.getItems().remove(selectedIndex);
                    dipendenteTable.setItems(getDipendenteData());
                    spesaTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getDipendentiStipendi()));
                }else{
                    Alert alert= new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Non puoi lasciare un campo vuoto");
                    alert.showAndWait();
                    handleEditDipendente();
                }
            }

        } catch (NoSuchElementException e) {
            showNoDipendenteSelectedAlert();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di modifica dei dipendenti", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante l'aggiornamento del dipendente", e);
        }
    }

    @FXML
    private void handleDeleteDipendente() {
        try {
            int selectedIndex = selectedIndex();
            showConfirmationAlert(selectedIndex);
            spesaTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getDipendentiStipendi()));
        } catch (NoSuchElementException e) {
            showNoDipendenteSelectedAlert();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showConfirmationAlert(int index) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Sei sicuro di voler eliminare questo dipendente?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MenuApplication.getDatabase().executeUpdate("DELETE FROM Dipendenti WHERE CF = ?",getDipendenteData().get(index).getCf());
            dipendenteTable.getItems().remove(index);
        }