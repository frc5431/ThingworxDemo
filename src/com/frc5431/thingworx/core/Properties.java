package com.frc5431.thingworx.core;

import org.lwjgl.util.vector.Vector4f;

import com.jumbo.tools.input.JumboInputHandler;
import com.jumbo.tools.input.JumboKey;

public class Properties {
	/* start of changeable values */
	public static int flywheelRPM = 0, intake = 0; // flywheelRPM is a value
													// from 0 to maxFlywheel,
													// represents rpm of the
													// flywheel. intake is
													// either -1, 0, or 1. -1
													// means it is in reverse, 0
													// means off, 1 menas
													// forward.
	public static float rDrive = 0, lDrive = 0;// both values are from -1.0 to
												// 1.0. they are raw joystick
												// values
	public static boolean ballIn = false;// true if ball is in, false otherwise
	/* end of changeable values */

	public static final int maxFlywheel = 4500;

	public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1), RED = new Vector4f(1, 0, 0, 1),
			GREEN = new Vector4f(0, 1, 0, 1), BLACK = new Vector4f(0, 0, 0, 1),
			GREY = new Vector4f(0.5f, 0.5f, 0.5f, 1);

	public static Vector4f rDriveColor, lDriveColor, flywheelColor = WHITE, intakeColor;

	public static final boolean CHEAT_MODE = true;

	public static void update() {
		if (CHEAT_MODE) {
			if (JumboInputHandler.isKeyDown(JumboKey.ONE)) {
				if (rDrive < 1) {
					rDrive += 0.05f;
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.TWO)) {
				if (rDrive > -1) {
					rDrive -= 0.05f;
				}
			}
			if (JumboInputHandler.isKeyDown(JumboKey.THREE)) {
				if (lDrive < 1) {
					lDrive += 0.05f;
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.FOUR)) {
				if (lDrive > -1) {
					lDrive -= 0.05f;
				}
			}
			if (JumboInputHandler.isKeyDown(JumboKey.FIVE)) {
				intake = intake != 0 ? 0 : 1;
			} else if (JumboInputHandler.isKeyDown(JumboKey.SIX)) {
				intake = intake != 0 ? 0 : -1;
			}
			if (JumboInputHandler.isKeyDown(JumboKey.SEVEN)) {
				if (flywheelRPM < maxFlywheel) {
					flywheelRPM += 50;
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.EIGHT)) {
				if (flywheelRPM > 0) {
					flywheelRPM -= 50;
				}
			}
			if (JumboInputHandler.isKeyDown(41)) {
				ballIn = !ballIn;
			}
		}

		intakeColor = intake != 0 ? intake == 1 ? GREEN : RED : GREY;

		float flywheelModifier = (float) flywheelRPM / maxFlywheel;
		flywheelColor = new Vector4f(0.5f - (0.5f * flywheelModifier), 0.5f + (0.5f * flywheelModifier),
				0.5f - (0.5f * flywheelModifier), 1);

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
	}

}
