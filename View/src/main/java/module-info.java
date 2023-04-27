module View {
    requires Model;
    requires javafx.fxml;
    requires javafx.controls;

    opens pl.view to javafx.fxml;
    exports pl.view;
}