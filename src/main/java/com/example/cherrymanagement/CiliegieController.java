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

public class CiliegieController {
    private Stage stage;
    @FXML private TableView<Ciliegia> ciliegiaTable;
    @FXML private TableColumn<Ciliegia,String> qualitaColumn;
    @FXML private TableColumn<Ciliegia,Double> kgVendutiColumn;
    @FXML private TableColumn<Ciliegia,String> descrizioneColumn;
    @FXML private TableColumn<Ciliegia,Double> prezzomedioColumn;
    @FXML private TableColumn<Ciliegia,Double> ricavoColumn;
    @FXML public Label ricavoTotaleLabel=new Label();

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize(){

        qualitaColumn.setCellValueFactory(new PropertyValueFactory<>("qualita"));
        kgVendutiColumn.setCellValueFactory(new PropertyValueFactory<>("kgVenduti"));
        descrizioneColumn.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        prezzomedioColumn.setCellValueFactory(new PropertyValueFactory<>("prezzomedio"));
        ricavoColumn.setCellValueFactory(new PropertyValueFactory<>("ricavo"));

        ciliegiaTable.setItems(getCiliegiaData());
        ciliegiaTable.getSelectionModel().selectedItemProperty();
        ricavoTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCiliegieRicavi()));
    }


    ObservableList<Ciliegia> getCiliegiaData(){
        ObservableList<Ciliegia> ciliegie = MenuApplication.getDatabase().getCiliegie(MenuApplication.getDatabase().getUsername_utente());
        ricavoTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCiliegieRicavi()));
        return ciliegie;
    }

    int selectedIndex() {
        int selectedIndex = ciliegiaTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            throw new NoSuchElementException();
        }
@@ -69,53 +62,53 @@ public class CiliegieController {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuova Qualità");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if(!controller.getCiliegia().getQualita().equals("")  && controller.getCiliegia().getKgVenduti() > 0
                && !controller.getCiliegia().getDescrizione().equals("") && controller.getCiliegia().getRicavo() > 0) {

                    MenuApplication.getDatabase().executeUpdate("INSERT INTO CILIEGIE(Qualità,Kg_Venduti,Descrizione,Ricavo,Username_utente) VALUES(?,?,?,?,?)",
                            controller.getCiliegia().getQualita(),controller.getCiliegia().getKgVenduti(),controller.getCiliegia().getDescrizione(),
                            controller.getCiliegia().getRicavo(),MenuApplication.getDatabase().getUsername_utente());

                    ciliegiaTable.setItems(getCiliegiaData());
                    ricavoTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCiliegieRicavi()));
                }else{
                    Alert alert= new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Non puoi lasciare un campo vuoto");
                    alert.showAndWait();
                    handleNewCiliegia();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di inserimento delle ciliegie", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il salvataggio della ciliegia", e);
        }
    }

    @FXML
    public void handleEditCiliegia() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CiliegieEditView.fxml"));
            DialogPane view = loader.load();
            CiliegieEditController controller = loader.getController();

            int selectedIndex = selectedIndex();
            controller.setCiliegia(new Ciliegia(ciliegiaTable.getItems().get(selectedIndex)));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Qualita");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            DialogPane dialogPane = dialog.getDialogPane();

            Ciliegia selectedCiliegia = ciliegiaTable.getItems().get(selectedIndex);

            TextField qualita = (TextField)dialogPane.lookup("#qualitaField");
            qualita.setText(selectedCiliegia.getQualita());
@@ -127,53 +120,53 @@ public class CiliegieController {
            ricavo.setText(String.valueOf(selectedCiliegia.getRicavo()));

            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if(!controller.getCiliegia().getQualita().equals("") && controller.getCiliegia().getKgVenduti() > 0
                        && !controller.getCiliegia().getDescrizione().equals("") && controller.getCiliegia().getRicavo() > 0) {

                    MenuApplication.getDatabase().executeUpdate("DELETE FROM Ciliegie WHERE Qualità = ?",selectedCiliegia.getQualita());
                    MenuApplication.getDatabase().executeUpdate("INSERT INTO CILIEGIE(Qualità,Kg_Venduti,Descrizione,Ricavo,Username_utente) VALUES(?,?,?,?,?)",
                            controller.getCiliegia().getQualita(),controller.getCiliegia().getKgVenduti(),controller.getCiliegia().getDescrizione(),
                            controller.getCiliegia().getRicavo(),MenuApplication.getDatabase().getUsername_utente());

                    ciliegiaTable.getItems().remove(selectedIndex);
                    ciliegiaTable.setItems(getCiliegiaData());
                    ricavoTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCiliegieRicavi()));
                }else{
                    Alert alert= new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Non puoi lasciare un campo vuoto");
                    alert.showAndWait();
                    handleEditCiliegia();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Impossibile aprire la finestra di modifica delle ciliegie", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante l'aggiornamento della ciliegia", e);
        }catch (NoSuchElementException e){
            showNoCiliegiaSelectedAlert();
        }
    }

    @FXML
    private void handleDeleteCiliegia() {
        try {
            int selectedIndex = selectedIndex();
            showConfirmationAlert(selectedIndex);
            ricavoTotaleLabel.textProperty().bind(Bindings.format("%.2f €", MenuApplication.getDatabase().getCiliegieRicavi()));
        } catch (NoSuchElementException e) {
            showNoCiliegiaSelectedAlert();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showConfirmationAlert(int index) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Sei sicuro di voler eliminare questa qualità?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MenuApplication.getDatabase().executeUpdate("DELETE FROM Ciliegie WHERE Qualità = ?",getCiliegiaData().get(index).getQualita());