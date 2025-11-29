import java.util.List;
import java.util.ArrayList;

public class RentalHistory {
	//private data storage
    private List<RentalRecord> rentalRecords = new ArrayList<>();

    // add a record
    public void addRecord(RentalRecord record) {
        rentalRecords.add(record);
    }
    
    //Get all record
    public List<RentalRecord> getRentalHistory() {
        return rentalRecords;
    }

    //search by customer name
    public List<RentalRecord> getRentalRecordsByCustomer(String customerName) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : rentalRecords) {
            if (record.getCustomer().toString().toLowerCase().contains(customerName.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }

    //search by vehicle  
    public List<RentalRecord> getRentalRecordsByVehicle(String licensePlate) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : rentalRecords) {
            if (record.getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate)) {
                result.add(record);
            }
        }
        return result;
    }
}