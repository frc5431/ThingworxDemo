package com.frc5431.thingworx.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.frc5431.thingworx.json.JsonObject;

public class RobotData {
	
	static double
		xangle = 0.0,
		yangle = 0.0,
		zangle = 0.0,
		leftrpm = 0.0,
		rightrpm = 0.0,
		ldistance = 0.0,
		leftdrivepower = 0.0,
		rdistance = 0.0,
		rightdrivepower = 0.0,
		xaccel = 0.0,
		yaccel = 0.0,
		zaccel = 0.0,
		choppers = 0.0,
		ballIn = 0.0,
		intake = 0.0,
		towerdistance = 0.0,
		fromcenter = 0.0,
		auton = 0.0,
		teleop = 0.0,
		enabled = 0.0;
		
	static String
		timestamp = null,
		laststamp = " ";
	
	static int currentRow = 1;
	
	static boolean updating = false;

	static ThingWorx worx = null;
	static Connection conn = null;
	static Statement stmt = null;
	
	public static double toDigit(String value) {
		return Double.valueOf(value.replaceAll("[^\\d.]", ""));
	}
	
	public static double JD(ResultSet set, String toGrab) throws SQLException {
		return toDigit(set.getString(toGrab));
	}
	
	public static double JD(JsonObject got, String toGrab) {
		return toDigit(got.get(toGrab).asString());
	}
	
	public static void closeDB() {
		if(conn != null) {
			try {
				if(stmt != null) {
					stmt.close();
					conn.commit();
				}
				conn.close();
			} catch(Exception err){
				System.out.println("Error closing DB!");
				err.printStackTrace();
			}
		}
	}
	
	public static void tick() {
		try {
			if(worx == null) worx = new ThingWorx();
			if(conn == null) {
				try {
					Class.forName("org.sqlite.JDBC");
					conn = DriverManager.getConnection("jdbc:sqlite:robot.db");
					conn.setAutoCommit(false);
					System.out.println("Opened database!");
				
					try {
						stmt = conn.createStatement();
						String sql = "DROP TABLE ROBOTDATA";
						stmt.executeQuery(sql);
						stmt.close();
					} catch(Exception dropNotFound){}
					
				      stmt = conn.createStatement();
				      String sql = "CREATE TABLE ROBOTDATA " +
				                   "(ID INT PRIMARY KEY     NOT NULL," +
				                   " TIMESTAMP           TEXT    NOT NULL, " + 
				                   " XANGLE        DOUBLE     NOT NULL, " + 
				                   " YANGLE        DOUBLE	  NOT NULL," + 
				                   " ZANGLE        DOUBLE	  NOT NULL," + 
				                   " XACCEL        DOUBLE	  NOT NULL," + 
				                   " YACCEL        DOUBLE	  NOT NULL," + 
				                   " ZACCEL        DOUBLE	  NOT NULL," + 
				                   " LEFTRPM        DOUBLE	  NOT NULL," + 
				                   " RIGHTRPM       DOUBLE	  NOT NULL," + 
				                   " LDISTANCE        DOUBLE	  NOT NULL," + 
				                   " RDISTANCE        DOUBLE	  NOT NULL," + 
				                   " LEFTDRIVEPOWER        DOUBLE	  NOT NULL," + 
				                   " RIGHTDRIVEPOWER        DOUBLE	  NOT NULL," + 
				                   " INTAKE        DOUBLE	  NOT NULL," + 
				                   " TOWERDISTANCE        DOUBLE	  NOT NULL," + 
				                   " FROMCENTER        DOUBLE	  NOT NULL," + 
				                   " CHOPPERS        DOUBLE	  NOT NULL," + 
				                   " AUTON        BOOLEAN	  NOT NULL," + 
				                   " TELEOP       BOOLEAN	  NOT NULL," + 
				                   " BALLIN       BOOLEAN	  NOT NULL," + 
				                   " ENABLED        DOUBLE	  NOT NULL)"; 
				      stmt.executeUpdate(sql);
				      stmt.close();
				      conn.commit();
				} catch(Exception sqlerr) {
					System.out.println("Error getting connection to database");
					sqlerr.printStackTrace();
				}
			}
			JsonObject got = worx.get_property();
			timestamp = got.get("timestamp").asString();
			updating = (timestamp != laststamp);
			laststamp = timestamp;
			xangle = JD(got, "xangle");
			yangle = JD(got, "yangle");
			zangle = JD(got, "zangle");
			xaccel = JD(got, "xaccel");
			yaccel = JD(got, "yaccel");
			zaccel = JD(got, "zaccel");
			leftrpm = JD(got, "leftrpm");
			rightrpm = JD(got, "rightrpm");
			ldistance = JD(got, "ldistance");
			rdistance = JD(got, "rdistance");
			leftdrivepower = JD(got, "leftdrivepower");
			rightdrivepower = JD(got, "rightdrivepower");
			choppers = JD(got, "choppers");
			auton = JD(got, "auton");
			teleop = JD(got, "teleop");
			enabled = JD(got, "enabled");
			ballIn = JD(got, "ballIn");
			intake = JD(got, "intake");
			towerdistance = JD(got, "towerdistance");
			fromcenter = JD(got, "fromcenter");
		      stmt = conn.createStatement();
		      String sql = String.format("INSERT INTO ROBOTDATA (ID,TIMESTAMP,XANGLE,YANGLE,ZANGLE,XACCEL,YACCEL,ZACCEL,LEFTRPM,RIGHTRPM,LDISTANCE,RDISTANCE,LEFTDRIVEPOWER,RIGHTDRIVEPOWER,INTAKE,TOWERDISTANCE,FROMCENTER,CHOPPERS,AUTON,TELEOP,BALLIN,ENABLED) " +
		                   "VALUES (%d, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s );", currentRow, timestamp, fmt(xangle),
		                   fmt(yangle), fmt(zangle), fmt(xaccel), fmt(yaccel), fmt(zaccel), fmt(leftrpm), fmt(rightrpm), 
		                   fmt(ldistance), fmt(rdistance), fmt(leftdrivepower), fmt(rightdrivepower), fmt(intake), fmt(towerdistance),fmt(fromcenter), fmt(choppers), 
		                   fmt(auton), fmt(teleop), fmt(ballIn), fmt(enabled)); 
		      stmt.executeUpdate(sql);
		      stmt.close();
		      conn.commit();
		      currentRow += 1;
		} catch(Exception fail) {
			System.out.println("Failed to update values...");
			fail.printStackTrace();
		}
	}
	
	public static int getCommits() {
		return currentRow;
	}
	
	public static void updateByDB(int row) {
		try {
			stmt = conn.createStatement();
			ResultSet got = stmt.executeQuery("SELECT * FROM ROBOTDATA WHERE Id=" + String.valueOf(row) + ";");
			while(got.next()) {
				timestamp = got.getString("timestamp");
				xangle = JD(got, "xangle");
				yangle = JD(got, "yangle");
				zangle = JD(got, "zangle");
				xaccel = JD(got, "xaccel");
				yaccel = JD(got, "yaccel");
				zaccel = JD(got, "zaccel");
				leftrpm = JD(got, "leftrpm");
				rightrpm = JD(got, "rightrpm");
				ldistance = JD(got, "ldistance");
				rdistance = JD(got, "rdistance");
				leftdrivepower = JD(got, "leftdrivepower");
				rightdrivepower = JD(got, "rightdrivepower");
				choppers = JD(got, "choppers");
				auton = JD(got, "auton");
				teleop = JD(got, "teleop");
				enabled = JD(got, "enabled");
				ballIn = JD(got, "ballIn");
				intake = JD(got, "intake");
				towerdistance = JD(got, "towerdistance");
				fromcenter = JD(got, "fromcenter");
				//System.out.println("Updated values from DataBase");
			}
		} catch(Exception readDBError) {
			readDBError.printStackTrace();
		}
		
	}
	
	
	public static String fmt(double d)
	{
	    if(d == (long) d)
	        return String.format("%d",(long)d);
	    else
	        return String.format("%s",d);
	}
	
	public static boolean isUpdating() {
		return updating;
	}
	
	public static Date getTimeStamp() {
		try {
			DateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
			Date date = format.parse(timestamp);
			return date;
		} catch(Exception err) {
			return null;
		}
 	}
	
	public static double[] getRobotGyro() {
		final double[] current = {xangle, yangle, zangle};
		return current;
	}
	
	public static double[] getRobotAccel() {
		final double[] current = {xaccel, yaccel, zaccel};
		return current;
	}
	
	public static double[] getRobotDistance() {
		final double[] current = {ldistance, rdistance, ((ldistance + rdistance) / 2)};
		return current;
	}
	
	public static double[] getRobotFlyWheel() {
		final double[] current = {leftrpm, rightrpm};
		return current;
	}
	
	public static double[] getRobotDrive() {
		final double[] current = {leftdrivepower, rightdrivepower};
		return current;
	}
	
	public static int getIntake() {
		return (int) intake;
	}
	
	public static double getTowerDistance() {
		return towerdistance;
	}
	
	public static double getFromCenter() {
		return fromcenter;
	}
	
	public static boolean getChopperState() {
		return (choppers > 0.9);
	}
	
	public static boolean isAuton() {
		return (auton > 0.9);
	}
	
	public static boolean isTeleop() {
		return (teleop > 0.9);
	}
	
	public static boolean isEnabled() {
		return (enabled > 0.9);
	}
	
	public static boolean isBallIn() {
		return (ballIn > 0.9);
	}
}
