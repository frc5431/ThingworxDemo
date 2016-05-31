package com.frc5431.thingworx.core;

import java.awt.Dimension;

import org.lwjgl.LWJGLException;

import com.jumbo.core.Jumbo;
import com.jumbo.core.JumboLaunchConfig;
import com.jumbo.tools.JumboSettings;

public class Demo {
	public static void main(String... args) throws LWJGLException {
		JumboSettings.fps = 60;
		Jumbo.setLaunchAction(() -> {

		});
		Jumbo.start(new JumboLaunchConfig("FRC 5431", new Dimension(400, 300)));
	}
}
