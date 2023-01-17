package com.assignment.service;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;

public interface ParkingService {

	ResponseEntity getParkingSlot(String parkingLotId, String size) throws SQLException;

	ResponseEntity releaseParkingSlot(String parkingLotId, String slotId) throws SQLException;
}
