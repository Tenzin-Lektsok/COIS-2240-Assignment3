import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class RentalSystem {
	
	// Singleton instance
    private static RentalSystem instance;
    
 // --- Private constructor ---
    private RentalSystem() {
        loadData();
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

    public boolean addVehicle(Vehicle vehicle) {
    	//check if vehicle with the same plate already exist.
    	if(findVehicleByPlate(vehicle.getLicensePlate())!=null) {
    		System.out.println("A vehicle with plate " + vehicle.getLicensePlate() +
                    " already exists. Vehicle not added.");
             return false;// duplication
    	}
    	//No duplicate - safe to add
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public boolean addCustomer(Customer customer) {
    	
    	//check if a customer with the same ID is already exists
    	if(findCustomerById(customer.getCustomerId())!= null) {
    		System.out.println("A customer with ID " + customer.getCustomerId() + "already exists, so customer not added.");
    		return false;
    		
    }
    	// Now no duplicate -safe to add customer
        customers.add(customer); // at to memory list
        saveCustomer(customer); //save to file
        return true;
    }
    

    public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            
          //Create a RentalRecord object
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");

            //Add the record into rentalHistory
            rentalHistory.addRecord(record);

            //Save the same record to a file
            saveRecord(record);

            System.out.println("Vehicle rented to " + customer.getCustomerName());
            return true;
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
		return false;
    }

    public boolean returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            
          //create the record
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");

            // Add to in-memory history
            rentalHistory.addRecord(record);

            //Also save to file
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            return true;
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
		return false;
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
    // Load Data from files
    private void loadData() {
    	loadVehicles();
    	loadCustomers();
    	loadRecords();
    }
    
    private void loadVehicles() {
    	try(BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"))){
    		
    		String line;
        	while((line =reader.readLine())!= null) {
        		String[] parts = line.split(",");
        		
        		if(parts.length >= 5) {
        			String plate = parts[0];
        			String make = parts[1];
        			String model = parts[2];
        			int year = Integer.parseInt(parts[3]);
        			String status = parts[4];
        			
        			//create vehicle(default to Car with 5 seat if type unknown
        			Vehicle vehicle = new Car(make,model, year,5);
        			vehicle.setLicensePlate(plate);
        			
        			// Convert string into enum value
        			vehicle.setStatus(Vehicle.VehicleStatus.valueOf(status));
        			
        			//add to the in-memory list
        			vehicles.add(vehicle);
        		}
        	}System.out.println("Loaded " + vehicles.size() + " vehicles from file");
    	}
    	catch(IOException e) {
    		System.out.println("No vehicles to load.");
    	}
    	
    
    }
    
    
 // Load customers from file
    private  void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //expect : id, name
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    
                    Customer customer = new Customer(id, name);
                    customers.add(customer);
                }
            }
            System.out.println("Loaded " + customers.size() + " customers from file");
        
        } catch (IOException e) {
        	System.out.println("No customers to load or error reading customers.txt");
        }
    }
    
    
    
 // Load rental records from file
    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String recordType = parts[0];
                    String plate = parts[1];
                    int customerId = Integer.parseInt(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);
                    double amount = Double.parseDouble(parts[4]);
                    
                    // Find the vehicle and customer
                    Vehicle vehicle = findVehicleByPlate(plate);
                    Customer customer = findCustomerById(customerId);
                    
                    if (vehicle != null && customer != null) {
                        RentalRecord record = new RentalRecord(vehicle, customer, date, amount, recordType);
                        rentalHistory.addRecord(record);
                    }
                }
            }
            System.out.println("Loaded " + rentalHistory.getRentalHistory().size() + " rental records from file");
        
        } catch (IOException e) {
        	 System.out.println("No rental records to load or error reading rental_records.txt");
        }
    }

    

    
}