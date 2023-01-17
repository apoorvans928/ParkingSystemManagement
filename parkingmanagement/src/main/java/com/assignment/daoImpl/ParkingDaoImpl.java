package com.assignment.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.assignment.dao.ParkingDao;
import com.assignment.util.ConnectionManager;

/**
 * 
 * @author Apoorva N S
 * Its a DAL layer
 *
 */
@Repository
public class ParkingDaoImpl implements ParkingDao {

	private Connection connection;
	private PreparedStatement prpStmnt;
	private PreparedStatement prpStmnt1;
	private ResultSet rs;
	private int status=0;
	private int status1=0;
	
	private static final Logger LOGGER=LogManager.getLogger(ParkingDaoImpl.class);

	/**
	 * 
	 * @author Apoorva N S
	 * This method is to book a available parking slot and to insert details in DB
	 * @param parkingLotId,  size,  floor,  bay
	 * Output:boolean indicator for success or failure of process
	 *
	 */
	@Override
	public boolean getParkingSlot(String parkingLotId, String size, int floor, int bay) throws SQLException {
		String insertToAllocationTable="insert into allocation_table (floorid,bayid,status) values ('"+String.valueOf(floor+"F")+"','"+String.valueOf(bay+"B"+size.toUpperCase().charAt(0))+"','allocated')";
		String insertToParkingTable="insert into parking_table (parkingid,size,floorid,bayid) values ('"+parkingLotId+"','"+size+"','"+String.valueOf(floor+"F")+"','"+String.valueOf(bay+"B"+size.toUpperCase().charAt(0))+"')";
		boolean flag=false;
		try {
			connection = ConnectionManager.getConnection();
			prpStmnt=connection.prepareStatement(insertToAllocationTable);
			status=prpStmnt.executeUpdate();
			
			prpStmnt1=connection.prepareStatement(insertToParkingTable);
			status1=prpStmnt1.executeUpdate();
			
			if(status==1&&status1==1) {
				flag=true;
			}

		} catch (SQLException e) {
			LOGGER.error("Error occured while alloting parking slot",e);

		} finally {
			if (connection != null) {
				connection.close();
			}
			if (prpStmnt != null) {
				prpStmnt.close();
			}
			if (prpStmnt1 != null) {
				prpStmnt1.close();
			}

		}
		return flag;
	}

	/**
	 * 
	 * @author Apoorva N S
	 * This method is to validate and get a free slots
	 * Output:List of occupied slots
	 *
	 */
	@Override
	public List<String> validateIsSlotFree() throws SQLException{
		String validateQuery="select floorid,bayid from allocation_table";
		List<String> allocatedList=new ArrayList<>();
		try {
			connection = ConnectionManager.getConnection();
			prpStmnt=connection.prepareStatement(validateQuery);
			rs=prpStmnt.executeQuery();
			while(rs.next()) {
				allocatedList.add(rs.getString("floorid")+":"+rs.getString("bayid"));
			}

		} catch (SQLException e) {
			LOGGER.error("Error occured while validating for free parking slot",e);
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return allocatedList;
	}
	
	/**
	 * 
	 * @author Apoorva N S
	 * This method is to release a occupied parking slot and to remove details from DB
	 * @param parkingLotId, floorid, bayid
	 * Output:boolean indicator for success or failure of process
	 *
	 */

	@Override
	public boolean releaseParkingSlot(String parkingLotId, String floorid, String bayid) throws SQLException {
		String deleteQuery="delete from allocation_table where floorid='"+floorid+"' and bayid='"+bayid+"'";
		String deleteParkingDetailsQuery="delete from parking_table where parkingid='"+parkingLotId+"'";
		boolean flag=false;
		try {
			connection = ConnectionManager.getConnection();
			prpStmnt1=connection.prepareStatement(deleteParkingDetailsQuery);
			status1=prpStmnt1.executeUpdate();
			
			prpStmnt=connection.prepareStatement(deleteQuery);
			status=prpStmnt.executeUpdate();
			
			
			
			if(status==1&&status1==1) {
				flag=true;
			}

		} catch (SQLException e) {
			LOGGER.error("Error occured while releasing parking slot",e);
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (prpStmnt != null) {
				prpStmnt.close();
			}
			if (prpStmnt1 != null) {
				prpStmnt1.close();
			}

		}
		
		return flag;
	}

}
