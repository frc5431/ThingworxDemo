package com.frc5431.thingworx.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.lwjgl.util.vector.Vector4f;

import com.jumbo.core.Jumbo;

public class Properties {
	public static Map<String, Property> properties = new HashMap<>();

	static {
		properties.put("ballIn", new Property(false));
		properties.put("rFlywheel", new Property(0));
		properties.put("lFlywheel", new Property(0));
		properties.put("intake", new Property(0));
		properties.put("rDrive", new Property(0.0f));
		properties.put("lDrive", new Property(0.0f));
		properties.put("leftDistance", new Property(0.0f));
		properties.put("rightDistance", new Property(0.0f));
		properties.put("driveAverage", new Property(0.0f));
		properties.put("choppers", new Property(false));
		properties.put("auton", new Property(false));
		properties.put("teleop", new Property(false));
		properties.put("enabled", new Property(false));
		properties.put("gyroX", new Property((0)));
		properties.put("gyroY", new Property((0)));
		properties.put("gyroZ", new Property((0)));
		properties.put("accelX", new Property(0));
		properties.put("accelY", new Property(0));
		properties.put("accelZ", new Property(0));
		properties.put("towerdistance", new Property(0.0f));
		properties.put("battery", new Property(0.0));
		properties.put("fromcenter", new Property(0.0f));

		Jumbo.setCloseListener(() -> {
			System.out.println("Commiting changes and closing database!");
			RobotData.closeDB();
		}); // Straight to my thighs choo choo

		final Executor exe = Executors.newSingleThreadExecutor();
		exe.execute(() -> {
			while (true) {
				RobotData.tick(); // Update database
				final double[] drive = RobotData.getRobotDrive(), flyRPM = RobotData.getRobotFlyWheel(),
						distances = RobotData.getRobotDistance(), gyro = RobotData.getRobotGyro(),
						accel = RobotData.getRobotAccel();

				boolean isAuto = RobotData.isAuton(), isTeleop = RobotData.isTeleop(),
						isEnabled = RobotData.isEnabled(), chopperState = RobotData.getChopperState();

				properties.get("rDrive").setValue((float) drive[0]);
				properties.get("lDrive").setValue((float) drive[1]);
				properties.get("leftDistance").setValue((float) distances[0]);
				properties.get("rightDistance").setValue((float) distances[1]);
				properties.get("driveAverage").setValue((float) distances[2]);
				properties.get("rFlywheel").setValue((int) ((flyRPM[0])));
				properties.get("lFlywheel").setValue((int) ((flyRPM[1])));
				properties.get("choppers").setValue((boolean) chopperState);
				properties.get("auton").setValue((boolean) isAuto);
				properties.get("teleop").setValue((boolean) isTeleop);
				properties.get("enabled").setValue((boolean) isEnabled);
				properties.get("gyroX").setValue(gyro[0]);
				properties.get("gyroY").setValue(gyro[1]);
				properties.get("gyroZ").setValue(gyro[2]);
				properties.get("accelX").setValue(accel[0]);
				properties.get("accelY").setValue(accel[1]);
				properties.get("accelZ").setValue(accel[2]);
				properties.get("battery").setValue((double) RobotData.getBattery());
				properties.get("ballIn").setValue((boolean) RobotData.isBallIn());
				properties.get("intake").setValue((int) RobotData.getIntake());
				properties.get("towerdistance").setValue((float) RobotData.getTowerDistance());
				properties.get("fromcenter").setValue((float) RobotData.getFromCenter());

				if (RobotData.isUpdating()) { // Timestamp checker
					// System.out.println("ROBOT IS ON AND UPDATING!!");

					// DO CRAP (NOT YET THERE IS A BUG)
				} else {
					Date currentStamp = RobotData.getTimeStamp();
					if (currentStamp != null) {
						System.out.println("Last (NEW) OKAY pull was at " + currentStamp.toString());
					}
				}

				/*
				 * LIAV THIS IS HOW TO DO A REPLAY EXAMPLE
				 */
				boolean replay = false;
				if (replay) {
					RobotData.updateByDB(1);
					Date lastStamp = RobotData.getTimeStamp();
					// Do initial stuff here such as camera angle and what not

					for (int stamp = 2; stamp < RobotData.getCommits(); stamp++) {
						RobotData.updateByDB(stamp);
						double delayBetweenUpdate = RobotData.getTimeStamp().getTime() - lastStamp.getTime();
						final double[] driveDB = RobotData.getRobotDrive(), flyRPMDB = RobotData.getRobotFlyWheel(),
								distancesDB = RobotData.getRobotDistance(), gyroDB = RobotData.getRobotGyro(),
								accelDB = RobotData.getRobotAccel();

						boolean isAutoDB = RobotData.isAuton(), isTeleopDB = RobotData.isTeleop(),
								isEnabledDB = RobotData.isEnabled(), chopperStateDB = RobotData.getChopperState();
					}
				}

				boolean closeDatabase = false; // YOU MUST CLOSE BY THE END OF
												// THE
												// PROGRAM
				if (closeDatabase) {
					RobotData.closeDB();
				}
			}

		});
	}

	public static final int maxFlywheel = 4500;

	public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1), RED = new Vector4f(1, 0, 0, 1),
			GREEN = new Vector4f(0, 1, 0, 1), BLACK = new Vector4f(0, 0, 0, 1),
			GREY = new Vector4f(0.5f, 0.5f, 0.5f, 1);

	public static Vector4f rDriveColor, lDriveColor, rFlywheelColor = WHITE, lFlywheelColor = WHITE, intakeColor, batteryColor;

	// public static final boolean CHEAT_MODE = true;

	public static void update() {

		// if (CHEAT_MODE) {
		// if (JumboInputHandler.isKeyDown(JumboKey.ONE)) {
		// if ((float) properties.get("rDrive").getValue() < 1) {
		// properties.get("rDrive").setValue((float)
		// properties.get("rDrive").getValue() + 0.05f);
		// }
		// } else if (JumboInputHandler.isKeyDown(JumboKey.TWO)) {
		// if ((float) properties.get("rDrive").getValue() > -1) {
		// properties.get("rDrive").setValue((float)
		// properties.get("rDrive").getValue() - 0.05f);
		// }
		// }
		// if (JumboInputHandler.isKeyDown(JumboKey.THREE)) {
		// if ((float) properties.get("lDrive").getValue() < 1) {
		// properties.get("lDrive").setValue((float)
		// properties.get("lDrive").getValue() + 0.05f);
		// }
		// } else if (JumboInputHandler.isKeyDown(JumboKey.FOUR)) {
		// if ((float) properties.get("lDrive").getValue() > -1) {
		// properties.get("lDrive").setValue((float)
		// properties.get("lDrive").getValue() - 0.05f);
		// }
		// }
		// if (JumboInputHandler.isKeyDown(JumboKey.FIVE)) {
		// properties.get("intake").setValue((int)
		// properties.get("intake").getValue() != 0 ? 0 : 1);
		// } else if (JumboInputHandler.isKeyDown(JumboKey.SIX)) {
		// properties.get("intake").setValue((int)
		// properties.get("intake").getValue() != 0 ? 0 : -1);
		// }
		// if (JumboInputHandler.isKeyDown(JumboKey.SEVEN)) {
		// if ((int) properties.get("flywheelRPM").getValue() < maxFlywheel) {
		// properties.get("flywheelRPM").setValue((int)
		// properties.get("flywheelRPM").getValue() + 50);
		// }
		// } else if (JumboInputHandler.isKeyDown(JumboKey.EIGHT)) {
		// if ((int) properties.get("flywheelRPM").getValue() > 0) {
		// properties.get("flywheelRPM").setValue((int)
		// properties.get("flywheelRPM").getValue() - 50);
		// }
		// }
		// if (JumboInputHandler.isKeyDown(41)) {
		// properties.get("ballIn").setValue(!(boolean)
		// properties.get("ballIn").getValue());
		// }
		// }

		intakeColor = (int) properties.get("intake").getValue() != 0
				? (int) properties.get("intake").getValue() == 1 ? GREEN : RED : GREY;

		float flywheelModifier = (float) (int) properties.get("lFlywheel").getValue() / maxFlywheel;
		lFlywheelColor = new Vector4f(0.5f - (0.5f * flywheelModifier), 0.5f + (0.5f * flywheelModifier),
				0.5f - (0.5f * flywheelModifier), 1);

		flywheelModifier = (float) (int) properties.get("rFlywheel").getValue() / maxFlywheel;
		rFlywheelColor = new Vector4f(0.5f - (0.5f * flywheelModifier), 0.5f + (0.5f * flywheelModifier),
				0.5f - (0.5f * flywheelModifier), 1);

		final float lDrive = -((float) properties.get("lDrive").getValue()),
				rDrive = -((float) properties.get("rDrive").getValue());

		if (rDrive >= 0) {
			rDriveColor = new Vector4f(0.5f - (0.5f * rDrive), 0.5f + (0.5f * rDrive), 0.5f - (0.5f * rDrive), 1);
		} else {
			rDriveColor = new Vector4f(0.5f - (0.5f * rDrive), 0.5f + (0.5f * rDrive), 0.5f + (0.5f * rDrive), 1);
		}
		if (lDrive >= 0) {
			lDriveColor = new Vector4f(0.5f - (0.5f * lDrive), 0.5f + (0.5f * lDrive), 0.5f - (0.5f * lDrive), 1);
		} else {
			lDriveColor = new Vector4f(0.5f - (0.5f * lDrive), 0.5f + (0.5f * lDrive), 0.5f + (0.5f * lDrive), 1);
		}
		
		final double batteryLevel =  (double) properties.get("battery").getValue();
		final double batteryDifference = batteryLevel-7.0;
		batteryColor = new Vector4f(1.0f - (float)batteryDifference/6.0f, ((float)batteryDifference/6.0f),0.0f,1.0f);
		
	}

}
