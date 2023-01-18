package com.assignment.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.daoImpl.ParkingDaoImpl;
import com.assignment.entities.Slot;
import com.assignment.service.ParkingService;

/**
 * 
 * @author Apoorva N S
 * Its a Service Layer
 *
 */
@Service
public class ParkingServiceImpl implements ParkingService{

	@Autowired
	ParkingDaoImpl parkingDaoImpl;
	List<String> allocatedList;
	ArrayList<String> sizeList=new ArrayList<String>(Arrays.asList("small","medium","large","x-large"));
	String slot="";
	private static final Logger LOGGER=LogManager.getLogger(ParkingServiceImpl.class);
	
	/**
	 * 
	 * @author Apoorva N S
	 * This method is to get a parking slot available w.r.t to vehicle size
	 * @param parkingLotId and size
	 * Output:Response Entity : with slot or respective message
	 *
	 */
	@Override
	public ResponseEntity getParkingSlot(String parkingLotId, String size) throws SQLException {
		boolean flag=false;
		Slot allotedSlot=new Slot();
		allocatedList=new ArrayList<>(); 
		if(!(size.equalsIgnoreCase("small")||size.equalsIgnoreCase("large")||size.equalsIgnoreCase("medium")||size.equalsIgnoreCase("x-large"))) {
			return new ResponseEntity<>("Please enter valid size from the list (small,large,medium,x-large)",HttpStatus.BAD_REQUEST);
		}
		allocatedList=parkingDaoImpl.validateIsSlotFree();
		 for(int s=sizeList.indexOf(size.toLowerCase()); s<sizeList.size();s++){
			if(flag==false) {
				for(int floor=1;floor<=3;floor++){
					if(flag==false) {
						for(int bay=1;bay<=100;bay++) {
							slot=floor+"F:"+bay+"B"+size.toUpperCase().charAt(0);
							if(!allocatedList.contains(slot)) {
								flag=parkingDaoImpl.getParkingSlot(parkingLotId,size,floor,bay);
								allotedSlot.setFloorId(floor+"F");
								allotedSlot.setBayId(bay+"B"+size.toUpperCase().charAt(0));
								break;
							}
						}
					}
				}
			}
			if(flag==true) {
				break;
			}
		}
		if(flag==true) {
			LOGGER.info("Successfully allocated the Parking Slot");
			return new ResponseEntity<>("Slot : "+allotedSlot.getFloorId()+"-"+allotedSlot.getBayId(),HttpStatus.OK);
		}else {
			LOGGER.info("Failed in allocating the Parking Slot");
			return new ResponseEntity<>("Failed to allocate Parking lot !!! Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}


	/**
	 * 
	 * @author Apoorva N S
	 * This method is to release a occupied parking slot 
	 * @param parkingLotId and slotid
	 * Output:Response Entity : with respective message
	 *
	 */
	@Override
	public ResponseEntity releaseParkingSlot(String parkingLotId, String slotId) throws SQLException {
		String [] slots;
		String bayid="";
		String floorid="";
		boolean flag=false;
		if(slotId.contains(":")) {
			slots=slotId.split(":");
			floorid=slots[0];
			bayid=slots[1];
		}else {
			return new ResponseEntity<>("Please enter slot id in correct format like floorid:bayid",HttpStatus.BAD_REQUEST);
		}
		flag=parkingDaoImpl.releaseParkingSlot(parkingLotId,floorid,bayid);
		if(flag==true) {
			LOGGER.info("Successfully released the Parking Slot for parkingId: "+parkingLotId+"and Slot: "+slotId);
			return new ResponseEntity<>("Released Parking Lot with ParkingLotId: "+parkingLotId+"and Slot: "+slotId,HttpStatus.OK);
		}else {
			LOGGER.info("Failed in releasing the Parking Slot for parkingId: "+parkingLotId+"and Slot: "+slotId);
			return new ResponseEntity<>("Failed to allocate Parking lot !!! Please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
