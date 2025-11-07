module com.example.cherrymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires org.xerial.sqlitejdbc;

    opens com.example.cherrymanagement to javafx.fxml;
    exports com.example.cherrymanagement;
}