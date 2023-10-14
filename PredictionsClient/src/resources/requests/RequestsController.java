package resources.requests;

import com.google.gson.Gson;
import engine.AllData;
import generated.PRDWorld;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import resources.app.ClientAppController;
import resources.utils.Constants;
import resources.utils.HttpClientUtil;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestsController {
    ClientAppController mainController;

    // Requests
    @FXML private AnchorPane requestsAnchorPane;
    @FXML private TableColumn<Request, String> simulationNameCol;
    @FXML private TableColumn<Request, Integer> numberOfRunsCol;
    @FXML private TableColumn<Request, Boolean> endByUserCol;
    @FXML private TableColumn<Request, Integer> secondsToEndCol;
    @FXML private TableColumn<Request, Integer> ticksToEndCol;
    @FXML private TableView<Request> requestsTableView;

    // Allowed
    @FXML private TableColumn<PRDWorld, Integer> IDCol;
    @FXML private TableColumn<PRDWorld, String> nameCol;
    @FXML private TableColumn<PRDWorld, Integer> simulationsAllowedCol;
    @FXML private TableColumn<PRDWorld, String> statusCol;
    @FXML private TableColumn<PRDWorld, Integer> runningCol;
    @FXML private TableColumn<PRDWorld, Integer> finishedCol;
    @FXML private TableView<PRDWorld> allowedTableView;

    // Buttons
    @FXML private Button submitButton;
    @FXML private Button executeButton;

    List<Request> allRequests = new ArrayList<>();

    public void setClientAppController(ClientAppController mainController){
        this.mainController = mainController;
    }

    public void submitButtonOnClick(){
        List<Request> allRequests = requestsTableView.getItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Request request = allRequests.get(0);

        String answer = "World name: " + request.getRequestWorldName() + ". \n";
        answer += "Number of runs: " + request.getNumberOfRuns() + ". \n";
        answer += "End by user value: " + request.getEndByUser() + ". \n";
        answer += "End by user property: " + request.getPropertyFromEndByUser().get() + ". \n";
        answer += "End by seconds: " + request.getEndBySeconds() + ". \n";
        answer += "End by ticks: " + request.getEndByTicks() + ". \n";
        alert.setContentText(answer);

        alert.showAndWait();
    }

    public void executeButtonOnClick(){

    }

    public void updateScreensOneTime(){

    }

    private void setCellValueForColumns(){
        simulationNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRequestWorldName()));
        // endByUserCol.setCellFactory(CheckBoxTableCell.forTableColumn(endByUserCol));
        endByUserCol.setCellFactory(CheckBoxTableCell.forTableColumn(new javafx.util.Callback<Integer, ObservableValue<Boolean>>()
        {

            @Override
            public ObservableValue<Boolean> call(Integer param) {
                allRequests.get(param).setEndByUser((!allRequests.get(param).getEndByUser()));

                return allRequests.get(param).getPropertyFromEndByUser();
            }
        }));
        numberOfRunsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        secondsToEndCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ticksToEndCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    }

    private void setOnEditCommitForCols(){
        numberOfRunsCol.setOnEditCommit(event -> {
            Request request = event.getRowValue();
            request.setNumberOfRuns(event.getNewValue());
        });

//        endByUserCol.setOnEditCommit(event -> {
//            Request request = event.getRowValue();
//            if(event.getNewValue().equals(true)){
//                request.setEndByUser(true);
//            }
//            else{
//                request.setEndByUser(false);
//            }
//        });

        secondsToEndCol.setOnEditCommit(event->{
            Request request = event.getRowValue();
            request.setEndByUser(false);
            endByUserCol.editableProperty().bind(request.getPropertyFromEndByUser().not());
            request.setEndBySeconds(event.getNewValue());});

        ticksToEndCol.setOnEditCommit(event->{
            Request request = event.getRowValue();
            request.setEndByUser(false);
            endByUserCol.editableProperty().bind(request.getPropertyFromEndByUser().not());
            request.setEndByTicks(event.getNewValue());});


        endByUserCol.setEditable(true);
        secondsToEndCol.setEditable(true);
        ticksToEndCol.setEditable(true);
        numberOfRunsCol.setEditable(true);
    }

    public void updateScreensEverySecond() {
        String finalUrl = "";
        try { // http://localhost:8080/WebApp_Web_exploded/getAllWorlds
            finalUrl = HttpUrl
                    .parse(Constants.ALL_WORLDS_PAGE)
                    .newBuilder()
                    .build()
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace(); // Log the exception
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                Gson gson = new Gson();
                PRDWorld[] prdWorldArray = gson.fromJson(jsonResponse, PRDWorld[].class);
                List<PRDWorld> allPRDWorlds = Arrays.asList(prdWorldArray);
                allRequests.clear();
                for(PRDWorld world : allPRDWorlds){
                    allRequests.add(new Request(world));
                }

                Platform.runLater(() -> {
                    requestsTableView.getItems().addAll(allRequests);
                    setCellValueForColumns();
                    setOnEditCommitForCols();
                });
            }
        });
    }
}
