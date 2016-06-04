package com.frc5431.thingworx.core;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector4f;

import com.jumbo.tools.input.JumboInputHandler;
import com.jumbo.tools.input.JumboKey;

public class Properties {
	public static Map<String, Property> properties = new HashMap<>();;

	static {
		properties.put("ballIn", new Property(false));
		properties.put("flywheelRPM", new Property(0));
		properties.put("intake", new Property(0));
		properties.put("rDrive", new Property(0.0f));
		properties.put("lDrive", new Property(0.0f));
	}

	public static final int maxFlywheel = 4500;

	public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1), RED = new Vector4f(1, 0, 0, 1),
			GREEN = new Vector4f(0, 1, 0, 1), BLACK = new Vector4f(0, 0, 0, 1),
			GREY = new Vector4f(0.5f, 0.5f, 0.5f, 1);

	public static Vector4f rDriveColor, lDriveColor, flywheelColor = WHITE, intakeColor;

	public static final boolean CHEAT_MODE = true;

	public static void update() {
		if (CHEAT_MODE) {
			if (JumboInputHandler.isKeyDown(JumboKey.ONE)) {
				if ((float) properties.get("rDrive").getValue() < 1) {
					properties.get("rDrive").setValue((float) properties.get("rDrive").getValue() + 0.05f);
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.TWO)) {
				if ((float) properties.get("rDrive").getValue() > -1) {
					properties.get("rDrive").setValue((float) properties.get("rDrive").getValue() - 0.05f);
				}
			}
			if (JumboInputHandler.isKeyDown(JumboKey.THREE)) {
				if ((float) properties.get("lDrive").getValue() < 1) {
					properties.get("lDrive").setValue((float) properties.get("lDrive").getValue() + 0.05f);
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.FOUR)) {
				if ((float) properties.get("lDrive").getValue() > -1) {
					properties.get("lDrive").setValue((float) properties.get("lDrive").getValue() - 0.05f);
				}
			}
			if (JumboInputHandler.isKeyDown(JumboKey.FIVE)) {
				properties.get("intake").setValue((int) properties.get("intake").getValue() != 0 ? 0 : 1);
			} else if (JumboInputHandler.isKeyDown(JumboKey.SIX)) {
				properties.get("intake").setValue((int) properties.get("intake").getValue() != 0 ? 0 : -1);
			}
			if (JumboInputHandler.isKeyDown(JumboKey.SEVEN)) {
				if ((int) properties.get("flywheelRPM").getValue() < maxFlywheel) {
					properties.get("flywheelRPM").setValue((int) properties.get("flywheelRPM").getValue() + 50);
				}
			} else if (JumboInputHandler.isKeyDown(JumboKey.EIGHT)) {
				if ((int) properties.get("flywheelRPM").getValue() > 0) {
					properties.get("flywheelRPM").setValue((int) properties.get("flywheelRPM").getValue() - 50);
				}
			}
			if (JumboInputHandler.isKeyDown(41)) {
				properties.get("ballIn").setValue(!(boolean) properties.get("ballIn").getValue());
			}
		}

		intakeColor = (int) properties.get("intake").getValue() != 0
				? (int) properties.get("intake").getValue() == 1 ? GREEN : RED : GREY;

		float flywheelModifier = (float) (int) properties.get("flywheelRPM").getValue() / maxFlywheel;
		flywheelColor = new Vector4f(0.5f - (0.5f * flywheelModifier), 0.5f + (0.5f * flywheelModifier),
				0.5f - (0.5f * flywheelModifier), 1);

		if ((float) properties.get("rDrive").getValue() >= 0) {
			rDriveColor = new Vector4f(0.5f - (0.5f * (float) properties.get("rDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("rDrive").getValue()),
					0.5f - (0.5f * (float) properties.get("rDrive").getValue()), 1);
		} else {
			rDriveColor = new Vector4f(0.5f - (0.5f * (float) properties.get("rDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("rDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("rDrive").getValue()), 1);
		}
		if ((float) properties.get("lDrive").getValue() >= 0) {
			lDriveColor = new Vector4f(0.5f - (0.5f * (float) properties.get("lDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("lDrive").getValue()),
					0.5f - (0.5f * (float) properties.get("lDrive").getValue()), 1);
		} else {
			lDriveColor = new Vector4f(0.5f - (0.5f * (float) properties.get("lDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("lDrive").getValue()),
					0.5f + (0.5f * (float) properties.get("lDrive").getValue()), 1);
		}
	}

}
