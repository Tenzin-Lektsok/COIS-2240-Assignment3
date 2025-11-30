import static org.junit.jupiter.api.Assertions.*; //JUmit 5

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;                

public class VehicleRentalTest {
	
	//class attribute
	private RentalSystem rentalSystem;
	
	@BeforeEach
	public void setUp() {
		//Runs before each test method
		rentalSystem = RentalSystem.getInstance();
	}

    @Test
	public void testLicensePlate(){
    	
		//Test valid plates with instantiate multiple Vehicle objects
		//Test valid plate: AAA100
    	//format- vehicle = new Car(make, model, year, seats);
    	Car car1 = new Car ("Toyota","Corolla", 2019, 5);
    	car1.setLicensePlate("AAA100");
    	assertTrue(car1.getLicensePlate().equals("AAA100")); //AAA100 should match
    	assertFalse(car1.getLicensePlate() == null);  //AAA100 should not be null
    	
    	
    	//test valid plate- ABC567
    	Car car2 = new Car("Honda", "Civic", 2021, 5);
    	car2.setLicensePlate("ABC567");
    	assertTrue(car2.getLicensePlate().equals("ABC567"));  //ABC567 should match
    	assertFalse(car1.getLicensePlate() == "ABC567"); //AAA100 should not equal ABC567
    	assertFalse(car2.getLicensePlate() == null); //ABC567 should not be null
    	
    	  // Test valid plate: ZZZ999
        Car car3 = new Car("Ford", "Focus", 2022, 5);
        car3.setLicensePlate("ZZZ999");
        assertTrue(car3.getLicensePlate().equals("ZZZ999")); //ZZZ999 should match
        assertFalse(car3.getLicensePlate() == null); //ZZZ999 should not be null
        assertFalse(car3.getLicensePlate() == " AAA100"); //ZZZ999 should not equal AAA100
        
        //----TEST INVALID PLATE-----
        
        //Test invalid: null
    	Car car4 = new Car("Honda","Civic-R", 2021, 5 );
    	assertThrows(IllegalArgumentException.class, () -> {
            car4.setLicensePlate(null);
        });
        
    	
    	// Test invalid: empty string
        Car car5 = new Car("Toyata", "GR-Corolla", 2021, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car5.setLicensePlate(" ");
        });
        
     // Test invalid: AAA1000 > (6 character format)
        Car car6 = new Car("Toyota", "CR", 2023, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car6.setLicensePlate("AAA1000"); 
        });
        
     // Test invalid: ZZZ99 < (6 character format)
        Car car7 = new Car("Honda", "HR", 2022, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car7.setLicensePlate("ZZZ99");
        });
	}
    
    @Test
    public void testReturnVehicle() {
    	
    	//create vehicle objects
    	//Parameters format-String make, String model, int year, double cargoSize, boolean hasTrailer
    	PickupTruck vehicle = new PickupTruck ("Ford", "F-150", 2022, 7, true);
    	
    	//set the license plate to AAA111
    	vehicle.setLicensePlate("AAA111");
    	
    	//instantiate customer with ID 1, name =Johnny Deo
    	Customer customer = new Customer(1, "Johnny Deo");
    	
    	//assertEqual check vehicle status equal to "Available"(enum value)
    	assertEquals(Vehicle.VehicleStatus.Available,vehicle.getStatus());
    	
    	//Rent vehicle for first time.
    	boolean rentSuccess = rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), 100.0);
    	
    	//check rental succeeded
    	assertTrue(rentSuccess);
    	assertEquals(Vehicle.VehicleStatus.Rented, vehicle.getStatus());
    	
    	//Try renting the same vehicle again and assert that it fails.
    	boolean rentAgain = rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), 100.0);
    	
    	//Check rental failed
    	assertFalse(rentAgain);
    	
    	//Call returnVehicle() for the same vehicle and customer objects,
    	boolean returnSuccess = rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), 15.0);
    	
    	//check returning is successful
    	assertTrue(returnSuccess);
    	assertEquals(Vehicle.VehicleStatus.Available, vehicle.getStatus());
    	
    	//Try returning same vehicle again and assert failed
    	boolean returnAgain = rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), 15.0);
    	assertFalse(returnAgain);
    
    }
    

}
