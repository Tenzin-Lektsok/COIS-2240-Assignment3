import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class RentalSystem {
	
	// Singleton instance
    private static RentalSystem instance;
    
 // --- Private constructor ---
    private RentalSystem() {
        
    }
    
    //getInstance method
    public static RentalSystem getInstance() {
    	if(instance == null) {
    		instance = new RentalSystem();
    	}
    	return instance;
    }
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }
    

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            
          //Create a RentalRecord object
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");

            //Add the record into rentalHistory
            rentalHistory.addRecord(record);

            //Save the same record to a file
            saveRecord(record);

            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            
          //create the record
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");

            // Add to in-memory history
            rentalHistory.addRecord(record);

            //Also save to file
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
    
    private void saveVehicle(Vehicle vehicle) { 
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true)
            );
            writer.write(vehicle.getLicensePlate() + ",");
            writer.write(vehicle.getMake() + ",");
            writer.write(vehicle.getModel() + ",");
            writer.write(vehicle.getYear() + ",");
            writer.write(vehicle.getStatus() + ",");
            writer.newLine();
            writer.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveCustomer(Customer customer) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true)
            );
            writer.write(customer.getCustomerId() + ",");
            writer.write(customer.getCustomerName());
            writer.newLine();
            writer.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private void saveRecord(RentalRecord record) { 
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true)
            );
            writer.write(record.getRecordType() + ",");
            writer.write(record.getVehicle().getLicensePlate() + ",");
            writer.write(record.getCustomer().getCustomerId() + ",");
            writer.write(record.getRecordDate() + ",");
            writer.write(record.getTotalAmount() + ",");
            writer.newLine();
            writer.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    

    
}