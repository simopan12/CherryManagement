package com.example.cherrymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Database {
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;
    private String username_utente;

    public String getUsername_utente() {
        return username_utente;
    }

    public void setUsername_utente(String username_utente) {
        this.username_utente = username_utente;
    }

    public Database(String url) {
        this(url, null, null);
    }

    public Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = null;
        connect();
    }

    private void checkConnection() {
        if (this.connection == null)connect();
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to verify database connection", e);
        }
    }

    public void connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println(connection);
        } catch(SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
            Class.forName("org.sqlite.JDBC");
            if (username == null || username.isBlank()) {
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, username, password);
            }
            enableForeignKeys();
            initialiseSchema();
            bootstrapDemoData();
        } catch (SQLException | ClassNotFoundException ex) {
            throw new IllegalStateException("Unable to connect to the database", ex);
        }
    }

    private void enableForeignKeys() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
    }

    private void initialiseSchema() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Utenti (" +
                    "Username TEXT PRIMARY KEY," +
                    "Password TEXT NOT NULL," +
                    "Nome_Utente TEXT NOT NULL," +
                    "Cognome_Utente TEXT NOT NULL," +
                    "Azienda TEXT NOT NULL" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS Ciliegie (" +
                    "\"Qualità\" TEXT NOT NULL," +
                    "\"Kg_Venduti\" REAL NOT NULL," +
                    "Descrizione TEXT NOT NULL," +
                    "Ricavo REAL NOT NULL," +
                    "Username_utente TEXT NOT NULL," +
                    "PRIMARY KEY (\"Qualità\", Username_utente)," +
                    "FOREIGN KEY (Username_utente) REFERENCES Utenti(Username) ON DELETE CASCADE" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS Costi (" +
                    "ID TEXT NOT NULL," +
                    "Tipo TEXT NOT NULL," +
                    "Ammontare REAL NOT NULL," +
                    "Username_utente TEXT NOT NULL," +
                    "PRIMARY KEY (ID, Username_utente)," +
                    "FOREIGN KEY (Username_utente) REFERENCES Utenti(Username) ON DELETE CASCADE" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS Dipendenti (" +
                    "CF TEXT NOT NULL," +
                    "Nome TEXT NOT NULL," +
                    "Cognome TEXT NOT NULL," +
                    "Mansione TEXT NOT NULL," +
                    "Paga REAL NOT NULL," +
                    "Ore REAL NOT NULL," +
                    "Username_utente TEXT NOT NULL," +
                    "PRIMARY KEY (CF, Username_utente)," +
                    "FOREIGN KEY (Username_utente) REFERENCES Utenti(Username) ON DELETE CASCADE" +
                    ")");
        }
    }

    private void bootstrapDemoData() throws SQLException {
        try (ResultSet resultSet = executeQuery("SELECT 1 FROM Utenti WHERE Username = ?", "demo")) {
            if (resultSet.next()) {
                return;
            }
        }

        executeUpdate("INSERT INTO Utenti (Username, Password, Nome_Utente, Cognome_Utente, Azienda) VALUES (?, ?, ?, ?, ?)",
                "demo", "demo", "Mario", "Rossi", "Cherry Farm");

        executeUpdate("INSERT INTO Ciliegie (\"Qualità\", \"Kg_Venduti\", Descrizione, Ricavo, Username_utente) VALUES (?, ?, ?, ?, ?)",
                "Duroni", 150.0, "Raccolta principale", 1200.0, "demo");
        executeUpdate("INSERT INTO Costi (ID, Tipo, Ammontare, Username_utente) VALUES (?, ?, ?, ?)",
                "C-001", "Imballaggi", 250.0, "demo");
        executeUpdate("INSERT INTO Dipendenti (CF, Nome, Cognome, Mansione, Paga, Ore, Username_utente) VALUES (?, ?, ?, ?, ?, ?, ?)",
                "RSSMRA90A01H501U", "Lucia", "Bianchi", "Raccolta", 12.5, 120.0, "demo");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    public ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        checkConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.closeOnCompletion();
        setParameters(statement, parameters);
        return statement.executeQuery();
    }

    public int executeUpdate(String query, Object... parameters) throws SQLException {
        checkConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, parameters);
        return statement.executeUpdate();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, parameters);
            return statement.executeUpdate();
        }
    }

    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
        }
    }

    public void SignUp(TextField usernameField, TextField passwordField,TextField nomeUtenteField,TextField cognomeUtenteField,TextField aziendaField){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nomeUtente = nomeUtenteField.getText();
        String cognomeUtente = cognomeUtenteField.getText();
        String azienda = aziendaField.getText();
    public void SignUp(TextField usernameField, TextField passwordField, TextField nomeUtenteField,
                       TextField cognomeUtenteField, TextField aziendaField) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String nomeUtente = nomeUtenteField.getText().trim();
        String cognomeUtente = cognomeUtenteField.getText().trim();
        String azienda = aziendaField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || nomeUtente.isEmpty() || cognomeUtente.isEmpty() || azienda.isEmpty()) {
            showRegistrationError("Compila tutti i campi prima di procedere.");
            return;
        }

        // Effettua la connessione al database
        try{
            // Verifica se l'utente esiste già
        try {
            if (checkUserExists(username)) {
                showRegistrationError("L'utente esiste già.");
                return;
            }

            // Esegui l'inserimento dell'utente nel database
            String query = "INSERT INTO Utenti (Username, Password, Nome_Utente, Cognome_Utente, Azienda) VALUES (?, ?, ?, ?, ?)";
            executeUpdate(query,username,password,nomeUtente,cognomeUtente,azienda);
            executeUpdate(query, username, password, nomeUtente, cognomeUtente, azienda);

            // Registrazione riuscita
            showRegistrationSuccess();
        } catch (SQLException e) {
            System.out.println("Errore di connessione al database: " + e.getMessage());
            showRegistrationError("Errore di connessione al database.");
        }
    }

    public void SignIn(TextField usernameField, PasswordField passwordField){
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Effettua la connessione al database
        try {
            // Query per verificare le credenziali di accesso
            String query = "SELECT * FROM Utenti WHERE Username = ? AND password = ?";

            ResultSet resultSet = executeQuery(query,username,password);
    public void SignIn(TextField usernameField, PasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        try (ResultSet resultSet = executeQuery("SELECT 1 FROM Utenti WHERE Username = ? AND Password = ?", username, password)) {
            if (resultSet.next()) {
                setUsername_utente(username);
                // Login riuscito
                MenuController.navigateToMenuPage();
            } else {
                // Login fallito
                showLoginError("Credenziali non valide.");
            }
        } catch (SQLException e) {
            System.out.println("Errore di connessione al database: " + e.getMessage());
            showLoginError("Errore di connessione al database.");
        }
    }


    private void showLoginError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean checkUserExists(String username) throws SQLException {
        String query = "SELECT * FROM Utenti WHERE Username = ?";
        ResultSet resultSet = executeQuery(query,username);
        return resultSet.next();
        try (ResultSet resultSet = executeQuery("SELECT 1 FROM Utenti WHERE Username = ?", username)) {
            return resultSet.next();
        }
    }


    private void showRegistrationSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrazione riuscita");
        alert.setHeaderText(null);
        alert.setContentText("La registrazione è avvenuta con successo. Effettua il login.");
        alert.showAndWait();
        navigateToSignInPage();
    }

    public void navigateToSignInPage(){
        try{
    public void navigateToSignInPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("SignInPage.fxml"));
            Parent root = fxmlLoader.load();


            SignInController signInController =fxmlLoader.getController();
            signInController.setStage(SignInController.getStage());


            Scene scene = new Scene(root);
            SignInController.getStage().setScene(scene);
        }catch (Exception e){
            System.out.println("Errore");
            StageManager.setScene(scene);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile caricare la schermata di login", e);
        }
    }


    private void showRegistrationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di registrazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public ObservableList<Ciliegia> getCiliegie(String uuid) {
        checkConnection();
        try {
            final String query = "SELECT * FROM Ciliegie JOIN Utenti ON Ciliegie.Username_utente = Utenti.Username WHERE Ciliegie.Username_utente = ?";

            ResultSet resultSet = executeQuery(query,uuid);
        try (ResultSet resultSet = executeQuery(
                "SELECT * FROM Ciliegie JOIN Utenti ON Ciliegie.Username_utente = Utenti.Username WHERE Ciliegie.Username_utente = ?",
                uuid)) {

            Ciliegia ciliegia = null;
            List<Ciliegia> ciliegie = new ArrayList<>();

            if (resultSet.next()) {
                do {
                    ciliegia = new Ciliegia(resultSet.getString("Qualità"),
                            resultSet.getDouble("Kg_Venduti"),
                            resultSet.getString("Descrizione"),
                            resultSet.getDouble("Ricavo"));
                    ciliegie.add(ciliegia);
                } while (resultSet.next());
            while (resultSet.next()) {
                Ciliegia ciliegia = new Ciliegia(resultSet.getString("Qualità"),
                        resultSet.getDouble("Kg_Venduti"),
                        resultSet.getString("Descrizione"),
                        resultSet.getDouble("Ricavo"));
                ciliegie.add(ciliegia);
            }

            resultSet.close();
            return FXCollections.observableList(ciliegie);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Errore durante il recupero delle ciliegie", ex);
        }
        return null;
    }


    public ObservableList<Costo> getCosti(String uuid) {
        checkConnection();
        try {
            final String query = "SELECT * FROM Costi JOIN Utenti ON Costi.Username_utente = Utenti.Username WHERE Costi.Username_utente = ?";
        try (ResultSet resultSet = executeQuery(
                "SELECT * FROM Costi JOIN Utenti ON Costi.Username_utente = Utenti.Username WHERE Costi.Username_utente = ?",
                uuid)) {

            ResultSet resultSet = executeQuery(query,uuid);

            Costo costo = null;
            List<Costo> costi = new ArrayList<>();

            if (resultSet.next()) {
                do {
                    costo = new Costo(resultSet.getString("ID"),
                            resultSet.getString("Tipo"),
                            resultSet.getDouble("Ammontare"));
                    costi.add(costo);
                } while (resultSet.next());
            while (resultSet.next()) {
                Costo costo = new Costo(resultSet.getString("ID"),
                        resultSet.getString("Tipo"),
                        resultSet.getDouble("Ammontare"));
                costi.add(costo);
            }

            resultSet.close();
            return FXCollections.observableList(costi);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Errore durante il recupero dei costi", ex);
        }
        return null;
    }

    public ObservableList<Dipendente> getDipendenti(String uuid) {
        checkConnection();
        try {
            final String query = "SELECT * FROM Dipendenti JOIN Utenti ON Dipendenti.Username_utente = Utenti.Username WHERE Dipendenti.Username_utente = ?";

            ResultSet resultSet = executeQuery(query,uuid);
        try (ResultSet resultSet = executeQuery(
                "SELECT * FROM Dipendenti JOIN Utenti ON Dipendenti.Username_utente = Utenti.Username WHERE Dipendenti.Username_utente = ?",
                uuid)) {

            Dipendente dipendente = null;
            List<Dipendente> dipendenti = new ArrayList<>();

            if (resultSet.next()) {
                do {
                    dipendente = new Dipendente(resultSet.getString("CF"),
                            resultSet.getString("Nome"),
                            resultSet.getString("Cognome"),
                            resultSet.getString("Mansione"),
                            resultSet.getDouble("Paga"),
                            resultSet.getDouble("Ore"));
                    dipendenti.add(dipendente);
                } while (resultSet.next());
            while (resultSet.next()) {
                Dipendente dipendente = new Dipendente(resultSet.getString("CF"),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getString("Mansione"),
                        resultSet.getDouble("Paga"),
                        resultSet.getDouble("Ore"));
                dipendenti.add(dipendente);
            }

            resultSet.close();
            return FXCollections.observableList(dipendenti);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Errore durante il recupero dei dipendenti", ex);
        }
        return null;
    }

    public Utente exportInfoFromDatabase(){
        checkConnection();
        try {
            final String query= "SELECT * FROM Utenti WHERE Utenti.Username=?";
            ResultSet resultSet= executeQuery(query,username_utente);

            Utente utente =null;

        try (ResultSet resultSet = executeQuery("SELECT * FROM Utenti WHERE Utenti.Username=?", username_utente)) {
            if(resultSet.next()){
                utente = new Utente(resultSet.getString("Username"),
                return new Utente(resultSet.getString("Username"),
                        resultSet.getString("Password"),
                        resultSet.getString("Nome_Utente"),
                        resultSet.getString("Cognome_Utente"),
                        resultSet.getString("Azienda"));
            }
            resultSet.close();
            return utente;
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il recupero delle informazioni utente", e);
        }
    }

    public ObservableList<Ciliegia> getPieChartDataCiliegia(){
        checkConnection();
        try {
            final String query="SELECT * FROM Ciliegie WHERE Ciliegie.Username_utente = ?";
            ResultSet resultSet =executeQuery(query,username_utente);

            Ciliegia ciliegia=null;
        try (ResultSet resultSet = executeQuery("SELECT * FROM Ciliegie WHERE Ciliegie.Username_utente = ?", username_utente)) {
            ObservableList<Ciliegia> pieChartData = FXCollections.observableArrayList();
            if(resultSet.next()){
                do {
                    ciliegia = new Ciliegia(resultSet.getString("Qualità"),
                            resultSet.getDouble("Kg_Venduti"),
                            resultSet.getString("Descrizione"),
                            resultSet.getDouble("Ricavo"));
                    pieChartData.add(ciliegia);
                }while (resultSet.next());
            while(resultSet.next()){
                Ciliegia ciliegia = new Ciliegia(resultSet.getString("Qualità"),
                        resultSet.getDouble("Kg_Venduti"),
                        resultSet.getString("Descrizione"),
                        resultSet.getDouble("Ricavo"));
                pieChartData.add(ciliegia);
            }
            resultSet.close();
            return pieChartData;

        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il recupero dei dati per il grafico delle ciliegie", e);
        }
    }

    public String getNomeAziendaData(){
        checkConnection();
        try {
            final String query = "SELECT Utenti.Azienda FROM Utenti WHERE Utenti.Username = ?";
            ResultSet resultSet = executeQuery(query,username_utente);

            String nomeAzienda = null;
        try (ResultSet resultSet = executeQuery("SELECT Utenti.Azienda FROM Utenti WHERE Utenti.Username = ?", username_utente)) {
            if(resultSet.next()){
                nomeAzienda = resultSet.getString("Azienda");
                return resultSet.getString("Azienda");
            }

            resultSet.close();
            return nomeAzienda;
            return "";

        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il recupero del nome dell'azienda", e);
        }
    }

    public ObservableList<Dipendente> getPieChartDataDipendente(){
        checkConnection();
        try {
            final String query="SELECT * FROM Dipendenti WHERE Dipendenti.Username_utente = ?";
            ResultSet resultSet =executeQuery(query,username_utente);

            Dipendente dipendente=null;
        try (ResultSet resultSet = executeQuery("SELECT * FROM Dipendenti WHERE Dipendenti.Username_utente = ?", username_utente)) {
            ObservableList<Dipendente> pieChartData = FXCollections.observableArrayList();
            while(resultSet.next()){
                dipendente = new Dipendente(resultSet.getString("CF"),
                Dipendente dipendente = new Dipendente(resultSet.getString("CF"),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getString("Mansione"),
                        resultSet.getDouble("Paga"),
                        resultSet.getDouble("Ore"));
                pieChartData.add(dipendente);
            }
            resultSet.close();
            return pieChartData;

        } catch (SQLException e) {
            throw new RuntimeException(e);
            throw new IllegalStateException("Errore durante il recupero dei dati per il grafico dei dipendenti", e);
        }
    }

    public double getCiliegieRicavi(){
        ObservableList<Ciliegia> ciliegie = getCiliegie(username_utente);
        double ricavi = 0;
        for(Ciliegia ciliegia : ciliegie){
            ricavi += ciliegia.getRicavo();
        }
        return ricavi;
    }

    public double getDipendentiStipendi(){
        ObservableList<Dipendente> dipendenti = getDipendenti(username_utente);
        double stipendi = 0;
        for(Dipendente dipendente : dipendenti){
            stipendi += dipendente.getStipendio();
        }
        return stipendi;
    }

    public double getCostiAmmontare(){
        ObservableList<Costo> costi = getCosti(username_utente);
        double ammontare = 0;


