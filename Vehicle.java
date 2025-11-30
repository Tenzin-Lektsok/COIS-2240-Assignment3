public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    //Constructor
    public Vehicle(String make, String model, int year) {
    	this.make = capitalize(make);
    	this.model = capitalize(model);
    	
    	 this.year = year;
         this.status = VehicleStatus.Available;
         this.licensePlate = null;
         
         
         //convert to helper method called capitalize which capitalize first letter, rest lower cases.
    	/*if (make == null || make.isEmpty())
    		this.make = null;
    	else
    		this.make = make.substring(0, 1).toUpperCase() + make.substring(1).toLowerCase();
    	
    	if (model == null || model.isEmpty())
    		this.model = null;
    	else
    		this.model = model.substring(0, 1).toUpperCase() + model.substring(1).toLowerCase();*/
         
         //helper method 
         
    }
    //helper method
    private String capitalize(String input) {
    	if(input ==null || input.isEmpty()) {
   		 return null;
   	 }
   	 return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
	}

	public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
       // this.licensePlate = plate == null ? null : plate.toUpperCase();
    	
    	//if license plate format not valid, threw error message
    	if(!isValidPlate(plate)) {
    		throw new IllegalArgumentException("Invalid license plate format. Must be 3 letters followed by 3 numbers ");
    	}
    	this.licensePlate = plate.toUpperCase(); //Write valid plate in capital form
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
    
    private boolean isValidPlate(String plate) {
    	//check -not null or not empty
    	if(plate == null || plate.isEmpty()) {
    		return false;
    	}
    	
    	//check exactly 6 character or not
    	if (plate.length() != 6) {
    		return false;
    	}
    	
    	//Check if first 3 are letters or not
    	for(int i = 0; i < 3; i++) {
    		if(!Character.isLetter(plate.charAt(i))) {
    			return false;
    		}
    	}
    	
    	//Check if last 3 character is number or not
    	for(int i = 3; i < 6; i++) {
    		if(!Character.isDigit(plate.charAt(i))) {
    			return false;
    		}
    	}return true; //if first 3- letters, last 3 is numbers
    }

}
