import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import java.sql.*;

public class main extends Application {

    TableView<pet> petTable = new TableView<>();
    TableView<adopter> adopterTable = new TableView<>();
    TableView<adoptionRequest> requestTable = new TableView<>();

    @Override
    public void start(Stage stage) {

        // --- PETS TAB ---
        TableColumn<pet, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<pet, String> breedCol = new TableColumn<>("Breed");
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));
        TableColumn<pet, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        petTable.getColumns().addAll(idCol, nameCol, breedCol, statusCol);

        Button loadPetsBtn = new Button("Load Pets");
        loadPetsBtn.setOnAction(e -> loadPets());

        // Adopt button
        Button adoptBtn = new Button("Request Adoption");
        adoptBtn.setOnAction(e -> requestAdoption());

        VBox petsTab = new VBox(10, new HBox(10, loadPetsBtn, adoptBtn), petTable);

        // --- ADOPTERS TAB ---
        TableColumn<adopter, Integer> aIdCol = new TableColumn<>("ID");
        aIdCol.setCellValueFactory(new PropertyValueFactory<>("adopterId"));
        TableColumn<adopter, String> aNameCol = new TableColumn<>("Name");
        aNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<adopter, String> aPhoneCol = new TableColumn<>("Phone");
        aPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<adopter, String> aAddrCol = new TableColumn<>("Address");
        aAddrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        adopterTable.getColumns().addAll(aIdCol, aNameCol, aPhoneCol, aAddrCol);

        Button loadAdoptersBtn = new Button("Load Adopters");
        loadAdoptersBtn.setOnAction(e -> loadAdopters());

        VBox adoptersTab = new VBox(10, loadAdoptersBtn, adopterTable);

        // --- ADOPTION REQUESTS TAB ---
        TableColumn<adoptionRequest, Integer> rIdCol = new TableColumn<>("Request ID");
        rIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        TableColumn<adoptionRequest, Integer> rPetCol = new TableColumn<>("Pet ID");
        rPetCol.setCellValueFactory(new PropertyValueFactory<>("petId"));
        TableColumn<adoptionRequest, Integer> rAdopterCol = new TableColumn<>("Adopter ID");
        rAdopterCol.setCellValueFactory(new PropertyValueFactory<>("adopterId"));
        TableColumn<adoptionRequest, String> rStatusCol = new TableColumn<>("Status");
        rStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        requestTable.getColumns().addAll(rIdCol, rPetCol, rAdopterCol, rStatusCol);

        Button loadRequestsBtn = new Button("Load Requests");
        loadRequestsBtn.setOnAction(e -> loadRequests());

        // Approve button
        Button approveBtn = new Button("Approve Request");
        approveBtn.setOnAction(e -> approveRequest());

        VBox requestsTab = new VBox(10, new HBox(10, loadRequestsBtn, approveBtn), requestTable);

        // --- TABS ---
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Pets", petsTab);
        Tab tab2 = new Tab("Adopters", adoptersTab);
        Tab tab3 = new Tab("Adoption Requests", requestsTab);
        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        tabPane.getTabs().addAll(tab1, tab2, tab3);

        stage.setScene(new Scene(tabPane, 700, 400));
        stage.setTitle("Pet Adoption System");
        stage.show();
    }

    // --- LOAD PETS ---
    void loadPets() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT pet_id, name, breed, status FROM Pet");
            petTable.getItems().clear();
            while (rs.next()) {
                petTable.getItems().add(new pet(
                    rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4)
                ));
            }
            conn.close();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // --- LOAD ADOPTERS ---
    void loadAdopters() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT adopter_id, name, phone, address FROM Adopter");
            adopterTable.getItems().clear();
            while (rs.next()) {
                adopterTable.getItems().add(new adopter(
                    rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4)
                ));
            }
            conn.close();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // --- LOAD REQUESTS ---
    void loadRequests() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT request_id, pet_id, adopter_id, status FROM Adoption_Request");
            requestTable.getItems().clear();
            while (rs.next()) {
                requestTable.getItems().add(new adoptionRequest(
                    rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getString(4)
                ));
            }
            conn.close();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // --- REQUEST ADOPTION ---
    void requestAdoption() {
        pet selected = petTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a pet first!");
            return;
        }
        // Ask for adopter ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Request Adoption");
        dialog.setHeaderText("Adopting: " + selected.getName());
        dialog.setContentText("Enter your Adopter ID:");
        dialog.showAndWait().ifPresent(adopterIdStr -> {
            try {
                int adopterId = Integer.parseInt(adopterIdStr);
                Connection conn = DBConnection.getConnection();
                // Get next request ID
                ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT NVL(MAX(request_id),300) + 1 FROM Adoption_Request");
                rs.next();
                int newId = rs.getInt(1);
                // Insert request
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Adoption_Request VALUES (?, ?, ?, SYSDATE, 'Pending')"
                );
                ps.setInt(1, newId);
                ps.setInt(2, adopterId);
                ps.setInt(3, selected.getId());
                ps.executeUpdate();
                conn.close();
                showSuccess("Adoption request submitted!");
                loadRequests();
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    // --- APPROVE REQUEST ---
    void approveRequest() {
        adoptionRequest selected = requestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a request first!");
            return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            // Update request status
            PreparedStatement ps1 = conn.prepareStatement(
                "UPDATE Adoption_Request SET status='Approved' WHERE request_id=?"
            );
            ps1.setInt(1, selected.getRequestId());
            ps1.executeUpdate();
            // Update pet status
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE Pet SET status='Adopted' WHERE pet_id=?"
            );
            ps2.setInt(1, selected.getPetId());
            ps2.executeUpdate();
            conn.close();
            showSuccess("Request approved! Pet marked as Adopted.");
            loadRequests();
            loadPets();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.show();
    }

    void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }

    public static void main(String[] args) { launch(); }
}