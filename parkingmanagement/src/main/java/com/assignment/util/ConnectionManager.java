package com.assignment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Apoorva N S
 * This class is implemented to establish a Database Connection 
 *
 */

@Component
@ConfigurationProperties
public class ConnectionManager {
	
	private static String username;
	
	private static String password;
	
	private static String url;
	
	private static String schema;
	
	private static Connection con;
	private static final Logger LOGGER=LogManager.getLogger(ConnectionManager.class);
	
	
	@Autowired
	public ConnectionManager(@Value("${spring.datasource.username}")String username,@Value("${spring.datasource.password}")String password,
			@Value("${spring.datasource.url}")String url,@Value("${spring.datasource.schema}")String schema) {
		this.username=username;
		this.password=password;
		this.url=url;
		this.schema=schema;
	}



	public static Connection getConnection() throws SQLException {
		try {
			con=DriverManager.getConnection(url,username,password);
			con.setSchema(schema);
		}catch(SQLException ex) {
			LOGGER.error("Failed to create the database connection: ",ex);
		}
		return con;
	}

}
