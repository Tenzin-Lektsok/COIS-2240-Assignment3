import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class RentalSystemGUI extends Application {
	  // Rental system instance
    private RentalSystem rentalSystem;
 // Text area for displaying output messages
    private TextArea outputArea;
    
    //JavaFX launcher
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        rentalSystem = RentalSystem.getInstance();
        
        // Create vertical layout with 10px spacing
        VBox layout = new VBox(10);
        
        // buttons for operations
        Button addVehicleButton = new Button("Add Vehicle");
        Button addCustomerButton = new Button("Add Customer");
        Button rentButton = new Button("Rent Vehicle");
        Button returnButton = new Button("Return Vehicle");
        Button viewButton = new Button("View Vehicles");
        Button historyButton = new Button("View History");
     // Create output text area
        outputArea = new TextArea();
        outputArea.setPrefHeight(400);
        outputArea.setEditable(false);
        
        //event handlers
        addVehicleButton.setOnAction(e -> addVehicle());
        addCustomerButton.setOnAction(e -> addCustomer());
        rentButton.setOnAction(e -> rentVehicle());
        returnButton.setOnAction(e -> returnVehicle());
        viewButton.setOnAction(e -> viewVehicles());
        historyButton.setOnAction(e -> viewHistory());
        
     // Add all components to layout
        layout.getChildren().addAll(
            new Label("Vehicle Rental System"),
            addVehicleButton,
            addCustomerButton,
            rentButton,
            returnButton,
            viewButton,
            historyButton,
            new Label("Output:"),
            outputArea
        );
        
        // Create scene and show window
        Scene scene = new Scene(layout, 500, 600);
        primaryStage.setTitle("Rental System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Add a new vehicle to the system
    private void addVehicle() {
        try {
        	  // Get vehicle details from user
            String licensePlate = getUserInput("License Plate:");
            if (licensePlate.isEmpty()) return;
            
            String make = getUserInput("Make:");
            String model = getUserInput("Model:");
            int year = Integer.parseInt(getUserInput("Year:"));
            
            Car vehicle = new Car(make, model, year, 5);
            vehicle.setLicensePlate(licensePlate);
            
            if (rentalSystem.addVehicle(vehicle)) {
                outputArea.setText("Vehicle added successfully: " + licensePlate);
            } else {
                outputArea.setText("Failed to add vehicle");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
 // Add a new customer to the system
    private void addCustomer() {
        try {
        	// Get customer details from user
            int customerId = Integer.parseInt(getUserInput("Customer ID:"));
            String customerName = getUserInput("Customer Name:");
            // Create customer object
            Customer newCustomer = new Customer(customerId, customerName);
            
            if (rentalSystem.addCustomer(newCustomer)) {
                outputArea.setText("Customer added successfully: " + customerName);
            } else {
                outputArea.setText("Failed to add customer");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    // Rent a vehicle to a customer
    private void rentVehicle() {
        try {
        	 // Get rental details from user
            String vehiclePlate = getUserInput("Vehicle Plate:");
            int customerId = Integer.parseInt(getUserInput("Customer ID:"));
            double rentalAmount = Double.parseDouble(getUserInput("Amount:"));
         // Find vehicle and customer in system
            Vehicle vehicle = rentalSystem.findVehicleByPlate(vehiclePlate);
            Customer customer = rentalSystem.findCustomerById(customerId);
            // Check if vehicle exists
            if (vehicle == null) {
                outputArea.setText("Vehicle not found");
                return;
            }
            if (customer == null) {
                outputArea.setText("Customer not found");
                return;
            }
            
            if (rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), rentalAmount)) {
                outputArea.setText("Vehicle rented to " + customer.getCustomerName());
            } else {
                outputArea.setText("Failed to rent vehicle");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
 // Return a rented vehicle
    private void returnVehicle() {
        try {
            String vehiclePlate = getUserInput("Vehicle Plate:");
            int customerId = Integer.parseInt(getUserInput("Customer ID:"));
         // Find vehicle and customer in system
            Vehicle vehicle = rentalSystem.findVehicleByPlate(vehiclePlate);
            Customer customer = rentalSystem.findCustomerById(customerId);
            
            if (vehicle == null || customer == null) {
                outputArea.setText("Vehicle or Customer not found");
                return;
            }
            
            if (rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), 0)) {
                outputArea.setText("Vehicle returned successfully");
            } else {
                outputArea.setText("Failed to return vehicle");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    // Display all vehicles
    private void viewVehicles() {
        outputArea.clear();
        // Capture console output to show in GUI
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        
        rentalSystem.displayVehicles(null);
        
        System.out.flush();
        System.setOut(old);
        outputArea.setText(baos.toString());
    }
    // Display rental history
    private void viewHistory() {
        outputArea.clear();
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        
        rentalSystem.displayRentalHistory();
        
        System.out.flush();
        System.setOut(old);
        outputArea.setText(baos.toString());
    }
    // Helper method to get user input
    private String getUserInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(prompt);
        return dialog.showAndWait().orElse("");
    }
}