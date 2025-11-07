package com.example.cherrymanagement;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class InfoController {
    private Stage stage;

    @FXML public Label nomeLabel = new Label();
    @FXML public Label cognomeLabel = new Label();
    @FXML public Label nomeAziendaLabel = new Label();
    @FXML public Label usernameLabel = new Label();
    @FXML public Label ricaviLabel= new Label();
    @FXML public Label costiLabel = new Label();
    @FXML public Label utileperditaLabel = new Label();
    @FXML public Label differenzaLabel = new Label();
    @FXML public PieChart pieChartCiliegia = new PieChart();
    @FXML public PieChart pieChartDipendente = new PieChart();


    public void initialize(){
        ObservableList<Ciliegia> pieChartDataCiliegia = MenuApplication.getDatabase().getPieChartDataCiliegia();
        ObservableList<PieChart.Data> chartDataCiliegia = FXCollections.observableArrayList();

        pieChartDataCiliegia.forEach(data ->
                chartDataCiliegia.add(new PieChart.Data(data.getQualita() + " " + data.getKgVenduti() + " kg", data.getKgVenduti()))
        );
        pieChartCiliegia.setData(chartDataCiliegia);


        ObservableList<Dipendente> pieChartDataDipendente = MenuApplication.getDatabase().getPieChartDataDipendente();
        ObservableList<PieChart.Data> chartDataDipendente = FXCollections.observableArrayList();

        pieChartDataDipendente.forEach(data ->
                chartDataDipendente.add(new PieChart.Data(data.getNome() + " " + data.getCognome() + " " + data.getOre() + "h", data.getOre()))
        );
        pieChartDipendente.setData(chartDataDipendente);

    }

    public void showInfo(){
        Utente utente = MenuApplication.getDatabase().exportInfoFromDatabase();
        nomeLabel.textProperty().set(utente.getNomeUtente());
        cognomeLabel.textProperty().set(utente.getCognomeUtente());
        nomeAziendaLabel.textProperty().set(utente.getAzienda());
        usernameLabel.textProperty().set(utente.getUsarname());
        if (utente != null) {
            nomeLabel.textProperty().set(utente.getNomeUtente());
            cognomeLabel.textProperty().set(utente.getCognomeUtente());
            nomeAziendaLabel.textProperty().set(utente.getAzienda());
            usernameLabel.textProperty().set(utente.getUsarname());
        }
        ricaviLabel.textProperty().set(MenuApplication.getDatabase().getCiliegieRicavi()+" €");
        costiLabel.textProperty().set((MenuApplication.getDatabase().getDipendentiStipendi() + MenuApplication.getDatabase().getCostiAmmontare())+" €");
        double differenza = MenuApplication.getDatabase().getCiliegieRicavi() - (MenuApplication.getDatabase().getDipendentiStipendi() + MenuApplication.getDatabase().getCostiAmmontare());
        if(differenza>=0){
            utileperditaLabel.textProperty().set("Utile: ");
            differenzaLabel.textProperty().set(differenza+" €");
        }else{
            utileperditaLabel.textProperty().set("Perdita: ");
            differenzaLabel.textProperty().set(-differenza+" €");
        }
    }

    @FXML
    public void deleteUtente() throws SQLException {
        MenuApplication.getDatabase().showConfirmationAlertUtente();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void goBack() {
        MenuController.navigateToMenuPage();
    }
}