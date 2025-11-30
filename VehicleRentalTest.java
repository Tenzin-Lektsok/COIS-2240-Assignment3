import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.Test;                

public class VehicleRentalTest {

    @Test
	public void testLicensePlate(){
    	
		//Test valid plates with instantiate multiple Vehicle objects
		//Test valid plate: AAA100
    	//format- vehicle = new Car(make, model, year, seats);
    	Car car1 = new Car ("Toyota","Corolla", 2019, 5);
    	car1.setLicensePlate("AAA100");
    	assertTrue(car1.getLicensePlate() == "AAA100"); //AAA100 should match
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
    

}
