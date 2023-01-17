package com.assignment.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.serviceImpl.ParkingServiceImpl;

/**
 * 
 * @author Apoorva N S
 * This is a controller class a enter level for an api
 *
 */
@RestController
public class ParkingController {
	
	@Autowired
	ParkingServiceImpl parkingServiceImpl;
	
	/**
	 * 
	 * @author Apoorva N S
	 * This method is to get a parking slot available w.r.t to vehicle size
	 * @param parkingLotId and size
	 * Output:Response Entity : with slot or respective message
	 *
	 */
	@GetMapping("/getslot/{parkingLotId}/{size}")
	public ResponseEntity getParkingSlot(@PathVariable String parkingLotId,@PathVariable String size) throws SQLException{
		try {
			return parkingServiceImpl.getParkingSlot(parkingLotId,size);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @author Apoorva N S
	 * This method is to release a occupied parking slot 
	 * @param parkingLotId and slotid
	 * Output:Response Entity : with respective message
	 *
	 */
	@DeleteMapping("/releaseslot/{parkingLotId}/{slotId}")
	public ResponseEntity releaseParkingSlot(@PathVariable String parkingLotId,@PathVariable String slotId){
		try {
				return parkingServiceImpl.releaseParkingSlot(parkingLotId,slotId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
