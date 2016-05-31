package com.frc5431.thingworx.core;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.jumbo.tools.input.console.JumboConsole;
import com.jumbo.tools.loaders.JumboStringHandler;

public class RobotModel {
	private static final String location = System.getProperty("user.dir") + "robot.obj";

	// load stuff into memory
	static {
		JumboConsole.log("Loading " + location);
		final String file = JumboStringHandler.loadAsString(location);
		JumboConsole.log(file);
	}

	public static ArrayList<Vector3f> normals, vertices, textures;
}
