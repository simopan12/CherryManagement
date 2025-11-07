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

public class CostiController {
    private Stage stage;

    @FXML private TableView<Costo> costoTable;
    @FXML private TableColumn<Costo,String> idColumn;
    @FXML private TableColumn<Costo,String> tipoColumn;
    @FXML private TableColumn<Costo,Double> ammontareColumn;
    @FXML public Label totaleCostiLabel = new Label();


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize(){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        ammontareColumn.setCellValueFactory(new PropertyValueFactory<>("ammontare"));

        costoTable.setItems(getCostoData());
        costoTable.getSelectionModel().selectedItemProperty();
        totaleCostiLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCostiAmmontare()));
    }


    private ObservableList<Costo> getCostoData() {
        ObservableList<Costo> costi = MenuApplication.getDatabase().getCosti(MenuApplication.getDatabase().getUsername_utente());;
        totaleCostiLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCostiAmmontare()));
        return costi;
    }


    int selectedIndex() {
        int selectedIndex = costoTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            throw new NoSuchElementException();
        }
        return selectedIndex;
public class CostiController {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuovo Costo");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if(!controller.getCosto().getId().equals("")  && !controller.getCosto().getTipo().equals("")
                        && controller.getCosto().getAmmontare()>0) {

                    MenuApplication.getDatabase().executeUpdate("INSERT INTO Costi(ID,Tipo,Ammontare,Username_utente) VALUES(?,?,?,?)",
                            controller.getCosto().getId(),controller.getCosto().getTipo(),controller.getCosto().getAmmontare(),
                            MenuApplication.getDatabase().getUsername_utente());

                    costoTable.setItems(getCostoData());
                    totaleCostiLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCostiAmmontare()));
                }else{
                    Alert alert= new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Non puoi lasciare un campo vuoto");
                    alert.showAndWait();
                    handleNewCosto();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di inserimento dei costi", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il salvataggio del costo", e);
        }
    }


    @FXML
    public void handleEditCosto() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CostiEditView.fxml"));
            DialogPane view = loader.load();
            CostiEditController controller = loader.getController();

            int selectedIndex = selectedIndex();
            controller.setCosto(new Costo(costoTable.getItems().get(selectedIndex)));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Costo");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            DialogPane dialogPane = dialog.getDialogPane();

            Costo selectedCosto = costoTable.getItems().get(selectedIndex);

            TextField id = (TextField)dialogPane.lookup("#idField");
public class CostiController {

            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if(!controller.getCosto().getId().equals("") && !controller.getCosto().getTipo().equals("")
                        && controller.getCosto().getAmmontare()>0) {

                    MenuApplication.getDatabase().executeUpdate("DELETE FROM Costi WHERE ID = ?",selectedCosto.getId());
                    MenuApplication.getDatabase().executeUpdate("INSERT INTO Costi(ID,Tipo,Ammontare,Username_utente) VALUES(?,?,?,?)",
                            controller.getCosto().getId(),controller.getCosto().getTipo(),controller.getCosto().getAmmontare(),
                            MenuApplication.getDatabase().getUsername_utente());

                    costoTable.getItems().remove(selectedIndex);
                    costoTable.setItems(getCostoData());
                    totaleCostiLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCostiAmmontare()));
                }else{
                    Alert alert= new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Non puoi lasciare un campo vuoto");
                    alert.showAndWait();
                    handleEditCosto();
                }
            }

        } catch (NoSuchElementException e) {
            showNoCostoSelectedAlert();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di modifica dei costi", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante l'aggiornamento del costo", e);
        }
    }

    @FXML
    private void handleDeleteCosto() {
        try {
            int selectedIndex = selectedIndex();
            showConfirmationAlert(selectedIndex);
            totaleCostiLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCostiAmmontare()));
        } catch (NoSuchElementException e) {
            showNoCostoSelectedAlert();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showConfirmationAlert(int index) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Sei sicuro di voler eliminare questo costo?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MenuApplication.getDatabase().executeUpdate("DELETE FROM Costi WHERE ID = ?",getCostoData().get(index).getId());
            costoTable.getItems().remove(index);
        }