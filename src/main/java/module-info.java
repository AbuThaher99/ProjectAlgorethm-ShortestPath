module com.example.projectonealgorethm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectonealgorethm to javafx.fxml;
    exports com.example.projectonealgorethm;
}