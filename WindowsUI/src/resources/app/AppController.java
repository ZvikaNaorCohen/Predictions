package resources.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import resources.body.DetailsBodyController;
import resources.header.HeaderController;

public class AppController {
    @FXML
    private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane bodyComponent;
    @FXML private DetailsBodyController bodyComponentController;

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
        }
    }

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setBodyComponentController(DetailsBodyController bodyComponentController) {
        this.bodyComponentController = bodyComponentController;
        bodyComponentController.setMainController(this);
    }
}
