module lista2.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens lista2.demo to javafx.fxml;
    exports lista2.demo;
}