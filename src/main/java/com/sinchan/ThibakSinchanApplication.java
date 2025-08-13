package com.sinchan;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ThibakSinchanApplication {
	
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_NAME = "sinchan_temp";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "12345678";

	public static void main(String[] args) {
		
		SpringApplication.run(ThibakSinchanApplication.class, args);
	}
	
	
	private static boolean checkDatabaseExists() {
		
		boolean result = false;
		try(Connection conn = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
			Statement state = conn.createStatement()){
			
			ResultSet rs = state.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+DB_NAME+"'");

			result =  rs.next();
		}catch(Exception e) {
			log.info("Exception : "+e.getCause());
		}
		
		log.info("returning : "+result);
		
		return result;
		
	}
	
	private static void takeDump(){
		
		log.info("Taking dump...");
		try {
			
			String backupDir = "/backups";
			new File(backupDir).mkdir();
			
			String today = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String backupFile = backupDir + "/" + DB_NAME + "_" + today + ".sql";
			
			log.info("backupfile : "+new File(backupFile).getAbsolutePath());
			
			ProcessBuilder processBuilder = new ProcessBuilder(
					"mysqldump", "-u", USERNAME, "-p" + PASSWORD, DB_NAME);
			
			processBuilder.redirectOutput(new File(backupFile));
			
			Process process = processBuilder.start();
			
			int processComplete = process.waitFor();
			
            if (processComplete == 0) {
            	log.info("Backup successful: " + backupFile);
            } else {
            	log.info("Backup failed!");
            }			
			
		}catch(Exception e) {
			log.info("Exception : "+e.getCause());
		}
	}
	
	private static void initializedSchema() {
	    try {
	        ProcessBuilder pb = new ProcessBuilder(
	            "mysql",
	            "-u", USERNAME,
	            "--password=" + PASSWORD
	        );

	        // Feed SQL file to MySQL
	        pb.redirectInput(new File("SQL/user_temp_schema.sql"));

	        Process process = pb.start();
	        int processComplete = process.waitFor();

	        if (processComplete == 0) {
	            log.info("Database created and initialized successfully!");

//	            System.setProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/"+DB_NAME);

	        } else {
	            log.info("Something went wrong during initialization.");
	        }

	    } catch (Exception e) {
	        log.info("Exception: " + e.getMessage());
	        e.printStackTrace();
	    }
	}	
	

}
