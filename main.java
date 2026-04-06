import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.effect.*;
import java.sql.*;
import java.util.*;

public class main extends Application {

    // Tables
    TableView<pet> petTable = new TableView<>();
    TableView<adopter> adopterTable = new TableView<>();
    TableView<adoptionRequest> requestTable = new TableView<>();
    TableView<medicalRecord> medicalTable = new TableView<>();

    ObservableList<pet> allPets = FXCollections.observableArrayList();

    // Search/filter
    TextField searchField = new TextField();
    ComboBox<String> statusFilter = new ComboBox<>();
    ComboBox<String> breedFilter = new ComboBox<>();

    // Main stage reference
    Stage primaryStage;

    // Color palette
    static final String PRIMARY   = "#2C3E7A";
    static final String ACCENT    = "#F4845F";
    static final String SUCCESS   = "#27AE60";
    static final String WARNING   = "#E67E22";
    static final String DANGER    = "#C0392B";
    static final String BG        = "#F0F4FF";
    static final String CARD      = "#FFFFFF";
    static final String TEXT_DARK = "#1A1F3C";
    static final String TEXT_GRAY = "#7F8C9A";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("🐾 Paws & Hearts Adoption Centre");
        showEntryPage();
        stage.show();
    }

    // ─────────────────────────────────────────────
    // ENTRY / LANDING PAGE
    // ─────────────────────────────────────────────
    void showEntryPage() {
        // Background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        // Decorative circles
        Circle c1 = new Circle(220);
        c1.setFill(Color.web(PRIMARY, 0.07));
        c1.setTranslateX(-280); c1.setTranslateY(-180);

        Circle c2 = new Circle(160);
        c2.setFill(Color.web(ACCENT, 0.10));
        c2.setTranslateX(320); c2.setTranslateY(220);

        // Logo / paw emoji label
        Label paw = new Label("🐾");
        paw.setStyle("-fx-font-size: 72px;");

        // Title
        Label title = new Label("Paws & Hearts");
        title.setStyle(
            "-fx-font-size: 44px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + PRIMARY + "; " +
            "-fx-font-family: 'Georgia', serif;"
        );

        Label subtitle = new Label("Adoption Centre");
        subtitle.setStyle(
            "-fx-font-size: 22px; -fx-text-fill: " + ACCENT + "; " +
            "-fx-font-family: 'Georgia', serif; -fx-font-style: italic;"
        );

        Label tagline = new Label("Every pet deserves a loving home.");
        tagline.setStyle(
            "-fx-font-size: 15px; -fx-text-fill: " + TEXT_GRAY + ";"
        );

        // Stats bar
        HBox stats = new HBox(40);
        stats.setAlignment(Pos.CENTER);
        stats.setPadding(new Insets(24, 0, 24, 0));
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Pet WHERE status='Available'");
            rs1.next(); int avail = rs1.getInt(1);
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Pet WHERE status='Adopted'");
            rs2.next(); int adopted = rs2.getInt(1);
            ResultSet rs3 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Adopter");
            rs3.next(); int adopters = rs3.getInt(1);
            conn.close();
            stats.getChildren().addAll(
                statCard("🐶", String.valueOf(avail),   "Available Pets"),
                statCard("🏠", String.valueOf(adopted), "Adopted"),
                statCard("👥", String.valueOf(adopters),"Adopters")
            );
        } catch (Exception e) {
            stats.getChildren().add(new Label("Could not load stats: " + e.getMessage()));
        }

        // VIEW button
        Button viewBtn = new Button("View Available Pets  →");
        viewBtn.setStyle(
            "-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-background-radius: 30; -fx-padding: 14 40; -fx-cursor: hand;"
        );
        viewBtn.setOnMouseEntered(e -> viewBtn.setStyle(
            "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-background-radius: 30; -fx-padding: 14 40; -fx-cursor: hand;"
        ));
        viewBtn.setOnMouseExited(e -> viewBtn.setStyle(
            "-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-background-radius: 30; -fx-padding: 14 40; -fx-cursor: hand;"
        ));
        viewBtn.setOnAction(e -> showMainApp());

        Button adminBtn = new Button("Admin Panel");
        adminBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + PRIMARY + "; " +
            "-fx-font-size: 13px; -fx-border-color: " + PRIMARY + "; " +
            "-fx-border-radius: 20; -fx-background-radius: 20; " +
            "-fx-padding: 8 24; -fx-cursor: hand;"
        );
        adminBtn.setOnAction(e -> showMainApp());

        HBox buttons = new HBox(16, viewBtn, adminBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox center = new VBox(10, paw, title, subtitle, tagline, stats, buttons);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(600);

        root.getChildren().addAll(c1, c2, center);

        Scene scene = new Scene(root, 860, 580);
        primaryStage.setScene(scene);
    }

    VBox statCard(String icon, String number, String label) {
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 28px;");
        Label num = new Label(number);
        num.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_GRAY + ";");
        VBox card = new VBox(4, ico, num, lbl);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16, 24, 16, 24));
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(44,62,122,0.10), 12, 0, 0, 4);"
        );
        return card;
    }

    // ─────────────────────────────────────────────
    // MAIN APP — Tab layout
    // ─────────────────────────────────────────────
    void showMainApp() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle(
            "-fx-tab-min-height: 40px; -fx-tab-max-height: 40px; " +
            "-fx-font-size: 13px; -fx-font-weight: bold;"
        );

        Tab homeTab     = new Tab("🏠  Home",             buildHomeTab());
        Tab petsTab     = new Tab("🐾  Pets",             buildPetsTab());
        Tab adoptTab    = new Tab("💛  Adopt",            buildAdoptTab());
        Tab adoptersTab = new Tab("👤  Adopters",         buildAdoptersTab());
        Tab requestsTab = new Tab("📋  Requests",         buildRequestsTab());
        Tab medicalTab  = new Tab("🏥  Medical Records",  buildMedicalTab());
        Tab analyticsTab= new Tab("📊  Analytics",        buildAnalyticsTab());

        tabPane.getTabs().addAll(homeTab, petsTab, adoptTab, adoptersTab, requestsTab, medicalTab, analyticsTab);

        Scene scene = new Scene(tabPane, 900, 580);
        scene.getRoot().setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("🐾 Paws & Hearts — Admin");
    }

    // ─────────────────────────────────────────────
    // HOME TAB  — with paw doodle background + shelter info
    // ─────────────────────────────────────────────
    VBox buildHomeTab() {
        // ── Stat cards (fix: use separate statements) ──
        HBox cards = new HBox(16);
        cards.setAlignment(Pos.CENTER_LEFT);
        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();

            ResultSet r1 = st.executeQuery("SELECT COUNT(*) FROM Pet WHERE status='Available'");
            r1.next(); int avail = r1.getInt(1); r1.close();

            ResultSet r2 = st.executeQuery("SELECT COUNT(*) FROM Pet WHERE status='Adopted'");
            r2.next(); int adopted = r2.getInt(1); r2.close();

            ResultSet r3 = st.executeQuery("SELECT COUNT(*) FROM Adoption_Request WHERE status='Pending'");
            r3.next(); int pending = r3.getInt(1); r3.close();

            ResultSet r4 = st.executeQuery("SELECT COUNT(*) FROM Medical_Record");
            r4.next(); int med = r4.getInt(1); r4.close();

            st.close(); conn.close();

            cards.getChildren().addAll(
                bigStatCard("🐶", avail   + "", "Available Pets",   PRIMARY),
                bigStatCard("🏠", adopted + "", "Adopted",           SUCCESS),
                bigStatCard("📋", pending + "", "Pending Requests",  WARNING),
                bigStatCard("🏥", med     + "", "Medical Records",   "#8e44ad")
            );
        } catch (Exception e) {
            cards.getChildren().add(new Label("DB error: " + e.getMessage()));
        }

        // ── Paw doodle background canvas ──
        Canvas pawBg = new Canvas(860, 320);
        GraphicsContext gc = pawBg.getGraphicsContext2D();
        gc.setFill(Color.web(PRIMARY, 0.055));
        // Draw scattered paw prints across the canvas
        int[][] pawPositions = {
            {60,40},{200,20},{380,55},{550,30},{720,50},{820,80},
            {30,140},{150,160},{320,130},{480,155},{640,140},{800,160},
            {80,240},{240,260},{420,240},{590,255},{750,240},{840,270},
            {10,300},{170,290},{350,310},{520,295},{690,305}
        };
        for (int[] pos : pawPositions) {
            drawPaw(gc, pos[0], pos[1], 18 + (pos[0] % 10));
        }

        // ── Shelter info text on top of canvas ──
        Label heading = new Label("🐾  About Paws & Hearts");
        heading.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; " +
            "-fx-text-fill: " + PRIMARY + "; -fx-font-family: 'Georgia', serif;"
        );

        String msg1 = "Every pet deserves a loving home — and every home deserves the joy of a pet.\n"
            + "At Paws & Hearts, we believe that adoption is not just a transaction; it is a lifelong commitment\n"
            + "built on love, patience, and responsibility.";

        String msg2 = "We ask every adopter to promise:\n"
            + "  🐾  To love your pet unconditionally, through every season of life.\n"
            + "  🏥  To provide proper nutrition, regular vet check-ups, and a safe environment.\n"
            + "  💛  To never abandon your pet, whatever circumstances arise.\n"
            + "  🤝  To be patient — pets need time to adjust to a new home.\n"
            + "  🌿  To give them exercise, play, and the attention they deserve.";

        String msg3 = "Adoption is forever. When you open your home to one of our animals,\n"
            + "you are making a promise that their well-being comes first — always.";

        Label para1 = styledPara(msg1, 14);
        Label para2 = styledPara(msg2, 13);
        Label para3 = styledPara(msg3, 14);
        para3.setStyle(para3.getStyle() + "-fx-font-style: italic; -fx-text-fill: " + PRIMARY + ";");

        Separator sep1 = new Separator(); sep1.setOpacity(0.3);
        Separator sep2 = new Separator(); sep2.setOpacity(0.3);

        VBox textContent = new VBox(14, heading, sep1, para1, para2, sep2, para3);
        textContent.setPadding(new Insets(26, 32, 26, 32));
        textContent.setMaxWidth(820);
        textContent.setStyle(
            "-fx-background-color: rgba(255,255,255,0.82); " +
            "-fx-background-radius: 16;"
        );

        // Overlay text on paw canvas
        StackPane infoCard = new StackPane(pawBg, textContent);
        infoCard.setStyle(
            "-fx-background-color: white; -fx-background-radius: 16; " +
            "-fx-effect: dropshadow(gaussian, rgba(44,62,122,0.10), 14, 0, 0, 4);"
        );
        infoCard.setAlignment(Pos.TOP_LEFT);

        Button backBtn = styledButton("← Back to Entry Page", "#888");
        backBtn.setOnAction(e -> showEntryPage());

        Label statsLabel = new Label("Today's Shelter Stats");
        statsLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        VBox tab = new VBox(16, statsLabel, cards, infoCard, backBtn);
        tab.setPadding(new Insets(24));
        tab.setStyle("-fx-background-color: " + BG + ";");
        return tab;
    }

    // Draw a simple paw print at (x,y) with given size
    void drawPaw(GraphicsContext gc, double x, double y, double size) {
        double s = size;
        // Main pad
        gc.fillOval(x, y + s * 0.4, s * 1.0, s * 0.9);
        // Top toes
        gc.fillOval(x - s * 0.15, y, s * 0.45, s * 0.45);
        gc.fillOval(x + s * 0.35, y - s * 0.08, s * 0.45, s * 0.45);
        gc.fillOval(x + s * 0.75, y, s * 0.45, s * 0.45);
        gc.fillOval(x + s * 1.1, y + s * 0.2, s * 0.38, s * 0.38);
    }

    Label styledPara(String text, int size) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setStyle(
            "-fx-font-size: " + size + "px; -fx-text-fill: " + TEXT_DARK + "; " +
            "-fx-line-spacing: 4px;"
        );
        return lbl;
    }

    VBox bigStatCard(String icon, String number, String label, String color) {
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 32px;");
        Label num = new Label(number);
        num.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_GRAY + ";");
        VBox card = new VBox(6, ico, num, lbl);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20, 28, 20, 28));
        card.setPrefWidth(170);
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 14; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );
        return card;
    }

    // ─────────────────────────────────────────────
    // PETS TAB (click row to see health record)
    // ─────────────────────────────────────────────
    VBox buildPetsTab() {
        // Columns
        TableColumn<pet, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id")); idCol.setPrefWidth(50);
        TableColumn<pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name")); nameCol.setPrefWidth(100);
        TableColumn<pet, String> breedCol = new TableColumn<>("Breed");
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed")); breedCol.setPrefWidth(130);
        TableColumn<pet, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status")); statusCol.setPrefWidth(100);

        statusCol.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                setStyle(s.equals("Available")
                    ? "-fx-text-fill: " + SUCCESS + "; -fx-font-weight: bold;"
                    : "-fx-text-fill: " + WARNING + "; -fx-font-weight: bold;");
            }
        });

        petTable.getColumns().setAll(idCol, nameCol, breedCol, statusCol);
        petTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        petTable.setStyle("-fx-background-color: white;");

        Label hint = new Label("💡 Click on any pet row to view its health records");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_GRAY + "; -fx-font-style: italic;");

        // Click row → show health popup
        petTable.setRowFactory(tv -> {
            TableRow<pet> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1 && !row.isEmpty()) {
                    showHealthRecords(row.getItem());
                }
            });
            return row;
        });

        // Search & filters
        searchField.setPromptText("🔍  Search by name or breed...");
        searchField.setPrefWidth(220);
        searchField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #dde; -fx-padding: 6 12;");
        searchField.textProperty().addListener((obs, o, v) -> applyFilters());

        statusFilter.setPromptText("Status");
        statusFilter.getItems().addAll("All", "Available", "Adopted");
        statusFilter.setValue("All");
        statusFilter.setOnAction(e -> applyFilters());

        breedFilter.setPromptText("Breed");
        breedFilter.setOnAction(e -> applyFilters());

        Button loadBtn = styledButton("Load Pets", PRIMARY);
        loadBtn.setOnAction(e -> loadPets());

        Button clearBtn = styledButton("Clear", "#aaa");
        clearBtn.setOnAction(e -> { searchField.clear(); statusFilter.setValue("All"); breedFilter.setValue(null); petTable.setItems(allPets); });

        HBox toolbar = new HBox(10, loadBtn, searchField, statusFilter, breedFilter, clearBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox tab = new VBox(10, toolbar, hint, petTable);
        tab.setPadding(new Insets(16));
        tab.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(petTable, Priority.ALWAYS);
        return tab;
    }

    // ─────────────────────────────────────────────
    // ADOPTION FORM — auto-creates adopter if new
    // ─────────────────────────────────────────────
    void showAdoptionForm(pet selected, Button refreshBtn) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Adopt " + selected.getName());
        dlg.setHeaderText(null);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Header label
        Label heading = new Label("💛  Adopting: " + selected.getName() + " (" + selected.getBreed() + ")");
        heading.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        Label infoLbl = new Label("Already registered? Enter your Adopter ID below.\nNew adopter? Fill in your details and we'll register you automatically.");
        infoLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_GRAY + "; -fx-wrap-text: true;");
        infoLbl.setWrapText(true);

        // Existing adopter section
        Label existingLbl = new Label("Existing Adopter ID (leave blank if new):");
        existingLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        TextField existingIdField = new TextField();
        existingIdField.setPromptText("e.g. 201");
        existingIdField.setPrefWidth(200);

        Separator sep = new Separator();
        sep.setPadding(new Insets(4, 0, 4, 0));

        Label newLbl = new Label("— OR — Register as New Adopter:");
        newLbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";");

        // New adopter fields
        TextField nameField    = new TextField(); nameField.setPromptText("Full name");
        TextField phoneField   = new TextField(); phoneField.setPromptText("Phone number");
        TextField addressField = new TextField(); addressField.setPromptText("Address");
        nameField.setPrefWidth(240);
        phoneField.setPrefWidth(240);
        addressField.setPrefWidth(240);

        // Disable new fields when existing ID is typed, and vice versa
        existingIdField.textProperty().addListener((obs, o, v) -> {
            boolean hasId = !v.trim().isEmpty();
            nameField.setDisable(hasId);
            phoneField.setDisable(hasId);
            addressField.setDisable(hasId);
            if (hasId) {
                nameField.setStyle("-fx-opacity: 0.5;");
                phoneField.setStyle("-fx-opacity: 0.5;");
                addressField.setStyle("-fx-opacity: 0.5;");
            } else {
                nameField.setStyle("");
                phoneField.setStyle("");
                addressField.setStyle("");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.setPrefWidth(400);

        grid.add(heading,       0, 0, 2, 1);
        grid.add(infoLbl,       0, 1, 2, 1);
        grid.add(new Label(),   0, 2); // spacer
        grid.add(existingLbl,   0, 3, 2, 1);
        grid.add(existingIdField, 0, 4, 2, 1);
        grid.add(sep,           0, 5, 2, 1);
        grid.add(newLbl,        0, 6, 2, 1);
        grid.add(new Label("Name:"),    0, 7); grid.add(nameField,    1, 7);
        grid.add(new Label("Phone:"),   0, 8); grid.add(phoneField,   1, 8);
        grid.add(new Label("Address:"), 0, 9); grid.add(addressField, 1, 9);

        dlg.getDialogPane().setContent(grid);

        dlg.showAndWait().ifPresent(result -> {
            if (result != ButtonType.OK) return;
            try {
                Connection conn = DBConnection.getConnection();
                int adopterId;

                String existingIdStr = existingIdField.getText().trim();

                if (!existingIdStr.isEmpty()) {
                    // Use existing adopter ID — check it exists
                    adopterId = Integer.parseInt(existingIdStr);
                    ResultSet check = conn.createStatement().executeQuery(
                        "SELECT COUNT(*) FROM Adopter WHERE adopter_id=" + adopterId
                    );
                    check.next();
                    if (check.getInt(1) == 0) {
                        check.close(); conn.close();
                        showError("Adopter ID " + adopterId + " not found! Please fill in your details as a new adopter instead.");
                        return;
                    }
                    check.close();

                } else {
                    // New adopter — validate fields
                    String name    = nameField.getText().trim();
                    String phone   = phoneField.getText().trim();
                    String address = addressField.getText().trim();

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        conn.close();
                        showError("Please fill in all fields: Name, Phone, and Address.");
                        return;
                    }

                    // Get next adopter_id
                    ResultSet rs = conn.createStatement().executeQuery(
                        "SELECT NVL(MAX(adopter_id), 200) + 1 FROM Adopter"
                    );
                    rs.next(); adopterId = rs.getInt(1); rs.close();

                    // Insert new adopter
                    PreparedStatement psA = conn.prepareStatement(
                        "INSERT INTO Adopter VALUES (?, ?, ?, ?)"
                    );
                    psA.setInt(1, adopterId);
                    psA.setString(2, name);
                    psA.setString(3, phone);
                    psA.setString(4, address);
                    psA.executeUpdate();
                }

                // Get next request_id
                ResultSet rs2 = conn.createStatement().executeQuery(
                    "SELECT NVL(MAX(request_id), 300) + 1 FROM Adoption_Request"
                );
                rs2.next(); int reqId = rs2.getInt(1); rs2.close();

                // Insert adoption request
                PreparedStatement psR = conn.prepareStatement(
                    "INSERT INTO Adoption_Request VALUES (?, ?, ?, SYSDATE, 'Pending')"
                );
                psR.setInt(1, reqId);
                psR.setInt(2, adopterId);
                psR.setInt(3, selected.getId());
                psR.executeUpdate();

                conn.close();

                showSuccess("🎉 Adoption request submitted for " + selected.getName() + "!\n"
                    + "Your Adopter ID is: " + adopterId + "\n"
                    + "Please save this — you'll need it for future adoptions.\n"
                    + "An admin will review and approve your request shortly.");

                if (refreshBtn != null) refreshBtn.fire();

            } catch (NumberFormatException ex) {
                showError("Adopter ID must be a number!");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    void showHealthRecords(pet p) {
        Dialog<Void> dlg = new Dialog<>();
        dlg.setTitle("Health Records — " + p.getName());
        dlg.setHeaderText(null);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Label title = new Label("🏥  Health Records for " + p.getName() + " (" + p.getBreed() + ")");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        TableView<medicalRecord> tbl = new TableView<>();
        TableColumn<medicalRecord, Integer> ridCol = new TableColumn<>("Record ID");
        ridCol.setCellValueFactory(new PropertyValueFactory<>("recordId")); ridCol.setPrefWidth(80);
        TableColumn<medicalRecord, String> treatCol = new TableColumn<>("Treatment");
        treatCol.setCellValueFactory(new PropertyValueFactory<>("treatment")); treatCol.setPrefWidth(200);
        TableColumn<medicalRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("treatmentDate")); dateCol.setPrefWidth(110);
        tbl.getColumns().addAll(ridCol, treatCol, dateCol);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(200);

        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT record_id, pet_id, treatment, TO_CHAR(treatment_date,'DD-Mon-YYYY') FROM Medical_Record WHERE pet_id=" + p.getId()
            );
            while (rs.next()) {
                tbl.getItems().add(new medicalRecord(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
            conn.close();
        } catch (Exception e) {
            tbl.setPlaceholder(new Label("Error: " + e.getMessage()));
        }

        if (tbl.getItems().isEmpty()) tbl.setPlaceholder(new Label("No medical records found for this pet."));

        VBox content = new VBox(12, title, tbl);
        content.setPadding(new Insets(16));
        content.setPrefWidth(440);

        dlg.getDialogPane().setContent(content);
        dlg.showAndWait();
    }

    // ─────────────────────────────────────────────
    // ADOPT TAB — browse available pets and adopt
    // ─────────────────────────────────────────────
    VBox buildAdoptTab() {
        Label heading = new Label("Find Your Perfect Companion 💛");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        TableView<pet> availTable = new TableView<>();
        TableColumn<pet, Integer> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(new PropertyValueFactory<>("id")); idC.setPrefWidth(50);
        TableColumn<pet, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(new PropertyValueFactory<>("name")); nameC.setPrefWidth(110);
        TableColumn<pet, String> breedC = new TableColumn<>("Breed");
        breedC.setCellValueFactory(new PropertyValueFactory<>("breed")); breedC.setPrefWidth(140);
        TableColumn<pet, String> statusC = new TableColumn<>("Status");
        statusC.setCellValueFactory(new PropertyValueFactory<>("status")); statusC.setPrefWidth(90);
        availTable.getColumns().addAll(idC, nameC, breedC, statusC);
        availTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Color available pets green
        statusC.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                setStyle(s.equals("Available")
                    ? "-fx-text-fill: " + SUCCESS + "; -fx-font-weight: bold;"
                    : "-fx-text-fill: " + WARNING + "; -fx-font-weight: bold;");
            }
        });

        Button loadAvailBtn = styledButton("Load Available Pets", PRIMARY);
        loadAvailBtn.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT pet_id, name, breed, status FROM Pet WHERE status='Available' ORDER BY pet_id"
                );
                availTable.getItems().clear();
                while (rs.next()) availTable.getItems().add(new pet(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                conn.close();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        Button adoptNowBtn = styledButton("💛  Adopt Selected Pet", ACCENT);
        adoptNowBtn.setStyle(
            "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-font-weight: bold; " +
            "-fx-background-radius: 10; -fx-padding: 10 28; -fx-cursor: hand;"
        );
        adoptNowBtn.setOnAction(e -> {
            pet selected = availTable.getSelectionModel().getSelectedItem();
            if (selected == null) { showError("Please select a pet to adopt!"); return; }
            if (!selected.getStatus().equals("Available")) { showError("This pet is not available!"); return; }
            showAdoptionForm(selected, loadAvailBtn);
        });

        Label hint = new Label("👆 Select a pet then click Adopt to submit a request");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_GRAY + "; -fx-font-style: italic;");

        HBox toolbar = new HBox(14, loadAvailBtn, adoptNowBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox tab = new VBox(14, heading, toolbar, hint, availTable);
        tab.setPadding(new Insets(20));
        tab.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(availTable, Priority.ALWAYS);
        return tab;
    }

    // ─────────────────────────────────────────────
    // ADOPTERS TAB
    // ─────────────────────────────────────────────
    VBox buildAdoptersTab() {
        TableColumn<adopter, Integer> aId = new TableColumn<>("ID");
        aId.setCellValueFactory(new PropertyValueFactory<>("adopterId")); aId.setPrefWidth(60);
        TableColumn<adopter, String> aName = new TableColumn<>("Name");
        aName.setCellValueFactory(new PropertyValueFactory<>("name")); aName.setPrefWidth(140);
        TableColumn<adopter, String> aPhone = new TableColumn<>("Phone");
        aPhone.setCellValueFactory(new PropertyValueFactory<>("phone")); aPhone.setPrefWidth(130);
        TableColumn<adopter, String> aAddr = new TableColumn<>("Address");
        aAddr.setCellValueFactory(new PropertyValueFactory<>("address")); aAddr.setPrefWidth(180);
        adopterTable.getColumns().addAll(aId, aName, aPhone, aAddr);
        adopterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button loadBtn = styledButton("Load Adopters", PRIMARY);
        loadBtn.setOnAction(e -> loadAdopters());

        VBox tab = new VBox(12, loadBtn, adopterTable);
        tab.setPadding(new Insets(16));
        tab.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(adopterTable, Priority.ALWAYS);
        return tab;
    }

    // ─────────────────────────────────────────────
    // REQUESTS TAB
    // ─────────────────────────────────────────────
    VBox buildRequestsTab() {
        TableColumn<adoptionRequest, Integer> rId = new TableColumn<>("Request ID");
        rId.setCellValueFactory(new PropertyValueFactory<>("requestId")); rId.setPrefWidth(90);
        TableColumn<adoptionRequest, Integer> rPet = new TableColumn<>("Pet ID");
        rPet.setCellValueFactory(new PropertyValueFactory<>("petId")); rPet.setPrefWidth(70);
        TableColumn<adoptionRequest, Integer> rAdopter = new TableColumn<>("Adopter ID");
        rAdopter.setCellValueFactory(new PropertyValueFactory<>("adopterId")); rAdopter.setPrefWidth(90);
        TableColumn<adoptionRequest, String> rStatus = new TableColumn<>("Status");
        rStatus.setCellValueFactory(new PropertyValueFactory<>("status")); rStatus.setPrefWidth(110);

        rStatus.setCellFactory(col -> new TableCell<>() {
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                if (s.equals("Approved")) setStyle("-fx-text-fill: " + SUCCESS + "; -fx-font-weight: bold;");
                else if (s.equals("Pending")) setStyle("-fx-text-fill: " + WARNING + "; -fx-font-weight: bold;");
                else setStyle("-fx-text-fill: " + DANGER + "; -fx-font-weight: bold;");
            }
        });

        requestTable.getColumns().addAll(rId, rPet, rAdopter, rStatus);
        requestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button loadBtn    = styledButton("Load Requests", PRIMARY);
        Button approveBtn = styledButton("✅  Approve", SUCCESS);
        Button rejectBtn  = styledButton("❌  Reject", DANGER);

        loadBtn.setOnAction(e -> loadRequests());
        approveBtn.setOnAction(e -> approveRequest());
        rejectBtn.setOnAction(e -> rejectRequest());

        HBox toolbar = new HBox(10, loadBtn, approveBtn, rejectBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox tab = new VBox(12, toolbar, requestTable);
        tab.setPadding(new Insets(16));
        tab.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(requestTable, Priority.ALWAYS);
        return tab;
    }

    // ─────────────────────────────────────────────
    // MEDICAL RECORDS TAB
    // ─────────────────────────────────────────────
    VBox buildMedicalTab() {
        TableColumn<medicalRecord, Integer> mId = new TableColumn<>("Record ID");
        mId.setCellValueFactory(new PropertyValueFactory<>("recordId")); mId.setPrefWidth(90);
        TableColumn<medicalRecord, Integer> mPet = new TableColumn<>("Pet ID");
        mPet.setCellValueFactory(new PropertyValueFactory<>("petId")); mPet.setPrefWidth(70);
        TableColumn<medicalRecord, String> mTreat = new TableColumn<>("Treatment");
        mTreat.setCellValueFactory(new PropertyValueFactory<>("treatment")); mTreat.setPrefWidth(220);
        TableColumn<medicalRecord, String> mDate = new TableColumn<>("Date");
        mDate.setCellValueFactory(new PropertyValueFactory<>("treatmentDate")); mDate.setPrefWidth(130);
        medicalTable.getColumns().addAll(mId, mPet, mTreat, mDate);
        medicalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button loadBtn = styledButton("Load Records", PRIMARY);
        loadBtn.setOnAction(e -> loadMedicalRecords());
        Button addBtn = styledButton("+ Add Record", "#8e44ad");
        addBtn.setOnAction(e -> addMedicalRecord());

        HBox toolbar = new HBox(10, loadBtn, addBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox tab = new VBox(12, toolbar, medicalTable);
        tab.setPadding(new Insets(16));
        tab.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(medicalTable, Priority.ALWAYS);
        return tab;
    }

    // ─────────────────────────────────────────────
    // ANALYTICS TAB — bar charts drawn on Canvas
    // ─────────────────────────────────────────────
    VBox buildAnalyticsTab() {
        Label heading = new Label("📊  Adoption Analytics");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY + ";");

        // ── Chart 1: Adoptions by Breed ──
        Label breedTitle = new Label("Most Adopted Breeds");
        breedTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        Canvas breedCanvas = new Canvas(420, 220);
        GraphicsContext gc1 = breedCanvas.getGraphicsContext2D();

        // ── Chart 2: Adoptions by Age ──
        Label ageTitle = new Label("Most Adopted Ages");
        ageTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        Canvas ageCanvas = new Canvas(380, 220);
        GraphicsContext gc2 = ageCanvas.getGraphicsContext2D();

        try {
            Connection conn = DBConnection.getConnection();

            // Breed data
            Map<String, Integer> breedData = new LinkedHashMap<>();
            ResultSet rs1 = conn.createStatement().executeQuery(
                "SELECT p.breed, COUNT(*) as cnt FROM Adoption_Request ar " +
                "JOIN Pet p ON ar.pet_id = p.pet_id " +
                "WHERE ar.status='Approved' " +
                "GROUP BY p.breed ORDER BY cnt DESC"
            );
            while (rs1.next()) breedData.put(rs1.getString(1), rs1.getInt(2));

            // Age data
            Map<String, Integer> ageData = new LinkedHashMap<>();
            ResultSet rs2 = conn.createStatement().executeQuery(
                "SELECT p.age, COUNT(*) as cnt FROM Adoption_Request ar " +
                "JOIN Pet p ON ar.pet_id = p.pet_id " +
                "WHERE ar.status='Approved' " +
                "GROUP BY p.age ORDER BY p.age"
            );
            while (rs2.next()) ageData.put(rs2.getString(1) + " yr", rs2.getInt(2));

            conn.close();

            // If no approved adoptions yet, use sample data
            if (breedData.isEmpty()) {
                breedData.put("Labrador", 3); breedData.put("Beagle", 2);
                breedData.put("Pug", 2); breedData.put("Persian Cat", 1);
            }
            if (ageData.isEmpty()) {
                ageData.put("1 yr", 2); ageData.put("2 yr", 4);
                ageData.put("3 yr", 3); ageData.put("4 yr", 1);
            }

            drawBarChart(gc1, breedData, "Breed", new Color[]{
                Color.web("#3a7bd5"), Color.web("#F4845F"), Color.web("#27AE60"),
                Color.web("#8e44ad"), Color.web("#E67E22"), Color.web("#16a085")
            });

            drawBarChart(gc2, ageData, "Age", new Color[]{
                Color.web("#F4845F"), Color.web("#3a7bd5"), Color.web("#27AE60"),
                Color.web("#E67E22"), Color.web("#8e44ad")
            });

        } catch (Exception e) {
            gc1.setFill(Color.web(DANGER));
            gc1.fillText("Error: " + e.getMessage(), 10, 100);
        }

        VBox breedBox = new VBox(8, breedTitle, breedCanvas);
        breedBox.setPadding(new Insets(16));
        breedBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8, 0, 0, 2);");

        VBox ageBox = new VBox(8, ageTitle, ageCanvas);
        ageBox.setPadding(new Insets(16));
        ageBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8, 0, 0, 2);");

        HBox charts = new HBox(20, breedBox, ageBox);
        charts.setAlignment(Pos.TOP_LEFT);

        Label note = new Label("* Charts show approved adoptions. If no approved requests exist, sample data is shown.");
        note.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_GRAY + "; -fx-font-style: italic;");

        VBox tab = new VBox(16, heading, charts, note);
        tab.setPadding(new Insets(20));
        tab.setStyle("-fx-background-color: " + BG + ";");
        return tab;
    }

    void drawBarChart(GraphicsContext gc, Map<String, Integer> data, String xLabel, Color[] colors) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        double padL = 40, padB = 50, padT = 20, padR = 10;
        double chartW = w - padL - padR;
        double chartH = h - padB - padT;

        // Background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        if (data.isEmpty()) {
            gc.setFill(Color.GRAY);
            gc.fillText("No data", w / 2 - 20, h / 2);
            return;
        }

        int maxVal = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        int n = data.size();
        double barW = (chartW / n) * 0.55;
        double gap   = (chartW / n) * 0.45;

        // Y axis grid lines
        gc.setStroke(Color.web("#EEEEEE"));
        gc.setLineWidth(1);
        for (int i = 1; i <= maxVal; i++) {
            double y = padT + chartH - (chartH * i / maxVal);
            gc.strokeLine(padL, y, padL + chartW, y);
            gc.setFill(Color.web(TEXT_GRAY));
            gc.setFont(Font.font(10));
            gc.fillText(String.valueOf(i), padL - 20, y + 4);
        }

        // Axes
        gc.setStroke(Color.web("#CCCCCC"));
        gc.setLineWidth(1.5);
        gc.strokeLine(padL, padT, padL, padT + chartH);
        gc.strokeLine(padL, padT + chartH, padL + chartW, padT + chartH);

        // Bars
        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double x = padL + i * (barW + gap) + gap / 2;
            double barH = (chartH * entry.getValue()) / maxVal;
            double y = padT + chartH - barH;

            Color c = colors[i % colors.length];
            gc.setFill(c);
            // Rounded top bar
            gc.fillRoundRect(x, y, barW, barH, 6, 6);

            // Value label on top
            gc.setFill(Color.web(TEXT_DARK));
            gc.setFont(Font.font(11));
            gc.fillText(String.valueOf(entry.getValue()), x + barW / 2 - 4, y - 5);

            // X label (rotated text via translate)
            gc.save();
            gc.translate(x + barW / 2, padT + chartH + 14);
            gc.rotate(xLabel.equals("Breed") ? -28 : 0);
            gc.setFill(Color.web(TEXT_DARK));
            gc.setFont(Font.font(10));
            gc.fillText(entry.getKey(), xLabel.equals("Breed") ? -18 : -10, 0);
            gc.restore();
            i++;
        }
    }

    // ─────────────────────────────────────────────
    // DATA METHODS
    // ─────────────────────────────────────────────
    void applyFilters() {
        String search = searchField.getText().toLowerCase().trim();
        String status = statusFilter.getValue();
        String breed  = breedFilter.getValue();
        petTable.setItems(allPets.filtered(p -> {
            boolean ms = search.isEmpty() || p.getName().toLowerCase().contains(search) || p.getBreed().toLowerCase().contains(search);
            boolean mst = status == null || status.equals("All") || p.getStatus().equals(status);
            boolean mb  = breed  == null || breed.equals("All")  || p.getBreed().equals(breed);
            return ms && mst && mb;
        }));
    }

    void loadPets() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT pet_id, name, breed, status FROM Pet ORDER BY pet_id"
            );
            allPets.clear();
            while (rs.next()) allPets.add(new pet(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            conn.close();
            petTable.setItems(allPets);
            ObservableList<String> breeds = FXCollections.observableArrayList("All");
            allPets.stream().map(pet::getBreed).distinct().sorted().forEach(breeds::add);
            breedFilter.setItems(breeds);
            breedFilter.setValue("All");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    void loadAdopters() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT adopter_id, name, phone, address FROM Adopter ORDER BY adopter_id"
            );
            adopterTable.getItems().clear();
            while (rs.next()) adopterTable.getItems().add(new adopter(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            conn.close();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    void loadRequests() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT request_id, pet_id, adopter_id, status FROM Adoption_Request ORDER BY request_id"
            );
            requestTable.getItems().clear();
            while (rs.next()) requestTable.getItems().add(new adoptionRequest(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
            conn.close();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    void loadMedicalRecords() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT record_id, pet_id, treatment, TO_CHAR(treatment_date,'DD-Mon-YYYY') FROM Medical_Record ORDER BY record_id"
            );
            medicalTable.getItems().clear();
            while (rs.next()) medicalTable.getItems().add(new medicalRecord(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
            conn.close();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    void addMedicalRecord() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Medical Record");
        dlg.setHeaderText("Enter medical record details");
        TextField petIdField = new TextField(); petIdField.setPromptText("Pet ID");
        TextField treatField = new TextField(); treatField.setPromptText("Treatment description");
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(16));
        grid.add(new Label("Pet ID:"), 0, 0); grid.add(petIdField, 1, 0);
        grid.add(new Label("Treatment:"), 0, 1); grid.add(treatField, 1, 1);
        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dlg.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    int petId = Integer.parseInt(petIdField.getText().trim());
                    String treatment = treatField.getText().trim();
                    if (treatment.isEmpty()) { showError("Treatment cannot be empty!"); return; }
                    Connection conn = DBConnection.getConnection();
                    ResultSet rs = conn.createStatement().executeQuery("SELECT NVL(MAX(record_id),0)+1 FROM Medical_Record");
                    rs.next(); int newId = rs.getInt(1);
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO Medical_Record VALUES(?,?,?,SYSDATE)");
                    ps.setInt(1, newId); ps.setInt(2, petId); ps.setString(3, treatment);
                    ps.executeUpdate(); conn.close();
                    showSuccess("Medical record added!"); loadMedicalRecords();
                } catch (NumberFormatException ex) { showError("Pet ID must be a number!");
                } catch (Exception ex) { showError(ex.getMessage()); }
            }
        });
    }

    void approveRequest() {
        adoptionRequest sel = requestTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select a request first!"); return; }
        if (sel.getStatus().equals("Approved")) { showError("Already approved!"); return; }
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps1 = conn.prepareStatement("UPDATE Adoption_Request SET status='Approved' WHERE request_id=?");
            ps1.setInt(1, sel.getRequestId()); ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("UPDATE Pet SET status='Adopted' WHERE pet_id=?");
            ps2.setInt(1, sel.getPetId()); ps2.executeUpdate();
            conn.close(); showSuccess("Request approved! Pet marked as Adopted.");
            loadRequests(); loadPets();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    void rejectRequest() {
        adoptionRequest sel = requestTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Select a request first!"); return; }
        if (!sel.getStatus().equals("Pending")) { showError("Only pending requests can be rejected!"); return; }
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE Adoption_Request SET status='Rejected' WHERE request_id=?");
            ps.setInt(1, sel.getRequestId()); ps.executeUpdate(); conn.close();
            showSuccess("Request rejected."); loadRequests();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    Button styledButton(String label, String color) {
        Button btn = new Button(label);
        btn.setStyle(
            "-fx-background-color: " + color + "; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-background-radius: 8; " +
            "-fx-padding: 8 18; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setOpacity(0.82));
        btn.setOnMouseExited(e  -> btn.setOpacity(1.0));
        return btn;
    }

    void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success"); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    public static void main(String[] args) { launch(); }
}