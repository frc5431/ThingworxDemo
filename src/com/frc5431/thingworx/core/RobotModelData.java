package com.frc5431.thingworx.core;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.jumbo.tools.input.console.JumboConsole;
import com.jumbo.tools.loaders.JumboStringHandler;

public class RobotModelData {
	private static final String location = System.getProperty("user.dir") + File.separator + "robot.obj";
	private static final int OBJ_SPACE_NUMBER = 2;

	/**
	 * Loads {@value #location} into the 3 arraylists for model data.
	 * <p>
	 * .obj files are literally text files that go like this:
	 * <ul>
	 * <li>Lines that start with v represents a vertex, with 3 values for
	 * position.</li>
	 * <li>Lines that start with vt represents a texture coordinate, for texture
	 * mapping</li>
	 * <li>Lines that start with vn represents a normal for lighting</li>
	 * </ul>
	 */
	public static void init() {
		JumboConsole.log("Loading " + location);
		final String[] lines = JumboStringHandler.loadAsString(location).split(String.format("%n"));
		for (String s : lines) {
			if (!s.equals("")) {
				{// some memory management
					final String[] data = s.split(" ");
					if (data[0].equals("vt")) {
						textures.add(new Vector3f(Float.parseFloat(data[OBJ_SPACE_NUMBER]),
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 1]),
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 2])));
					} else if (data[0].equals("vn")) {
						normals.add(new Vector3f(Float.parseFloat(data[OBJ_SPACE_NUMBER]),
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 1]),
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 2])));
					} else if (data[0].equals("v")) {
						vertices.add(new Vector3f(Float.parseFloat(data[OBJ_SPACE_NUMBER]) * 20f,
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 1]) * 20f,
								Float.parseFloat(data[OBJ_SPACE_NUMBER + 2]) * 20f));
					} else if (data[0].equals("f")) {
						final String[] d1 = data[1].split("/"), d2 = data[1 + OBJ_SPACE_NUMBER].split("/"),
								d3 = data[1 + (OBJ_SPACE_NUMBER * 2)].split("/");// getting
						// the
						// values
						// first
						// as
						// strings
						final Vector3f v1 = new Vector3f(Float.parseFloat(d1[0]), Float.parseFloat(d1[1]),
								Float.parseFloat(d1[2])),
								v2 = new Vector3f(Float.parseFloat(d2[0]), Float.parseFloat(d2[1]),
										Float.parseFloat(d2[2])),
								v3 = new Vector3f(Float.parseFloat(d3[0]), Float.parseFloat(d3[1]),
										Float.parseFloat(d3[2]));// next
																	// converting
																	// the
																	// string
																	// arrays
																	// into
																	// vectors
						// now adding it to the arraylist
						faces.add(new Vector3f[] { v1, v2, v3 });
					}
				}
			}
		}

		JumboConsole.log("Done loading.");
	}

	public static final ArrayList<Vector3f> normals = new ArrayList<>(), vertices = new ArrayList<>(),
			textures = new ArrayList<>();
	public static final ArrayList<Vector3f[]> faces = new ArrayList<>();// 3
																		// sets
																		// of
																		// vectors,
																		// representing
																		// each
																		// triangle
}
