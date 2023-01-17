package com.assignment.dao;

import java.sql.SQLException;
import java.util.List;

public interface ParkingDao {
	 boolean getParkingSlot(String parkingLotId, String size, int floor, int bay) throws SQLException;
	
	 List<String> validateIsSlotFree() throws SQLException;
	
	 boolean releaseParkingSlot(String parkingLotId, String floorid, String bayid) throws SQLException ;

}
