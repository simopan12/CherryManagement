package com.example.cherrymanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

    @TempDir
    Path tempDir;

    @Test
    void initialisesSchemaAndSeedData() throws SQLException {
        Path databaseFile = tempDir.resolve("test.db");
        Database database = new Database("jdbc:sqlite:" + databaseFile.toAbsolutePath());

        try (ResultSet tableResultSet = database.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='Utenti'")) {
            assertTrue(tableResultSet.next(), "La tabella Utenti dovrebbe esistere");
        }

        try (ResultSet userResultSet = database.executeQuery(
                "SELECT Nome_Utente FROM Utenti WHERE Username = ?", "demo")) {
            assertTrue(userResultSet.next(), "L'utente demo dovrebbe essere inizializzato");
            assertEquals("Mario", userResultSet.getString("Nome_Utente"));
        }

        database.disconnect();
    }

    @Test
    void returnsCompanyNameForLoggedUser() {
        Path databaseFile = tempDir.resolve("logged.db");
        Database database = new Database("jdbc:sqlite:" + databaseFile.toAbsolutePath());
        database.setUsername_utente("demo");

        String company = database.getNomeAziendaData();
        assertEquals("Cherry Farm", company);
    }
}