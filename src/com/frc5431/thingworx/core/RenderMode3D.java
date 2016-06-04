package com.frc5431.thingworx.core;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.io.File;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.jumbo.core.JumboGraphicsObject;
import com.jumbo.core.JumboRenderMode;
import com.jumbo.tools.calculations.JumboMathHandler;
import com.jumbo.tools.input.JumboInputHandler;
import com.jumbo.tools.input.JumboKey;
import com.jumbo.tools.loaders.JumboStringHandler;

public class RenderMode3D extends JumboRenderMode {
	float depth = -10.0f;

	float[] vertices = new float[] { 0, 0, depth, 0, 0.5f, depth, 0.5f, 0, depth };
	int[] indices = new int[] { 0, 1, 2, 0 };

	int vboId, vaoId, idxVboId;

	private static final float FOV = 70;

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.0f;

	private Vector3f offset = new Vector3f(0, 0, 0), rotation = new Vector3f(0, 0, 0);
	RawModel frame, flywheels, rdrive, ldrive, intake, ball;

	public RenderMode3D() throws Exception {

		prog = new ShaderProgram();
		prog.createVertexShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator + "res"
				+ File.separator + "shaders" + File.separator + "vertex.vs"));
		prog.createFragmentShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator
				+ "res" + File.separator + "shaders" + File.separator + "fragment.fs"));
		prog.link();

		prog.bind();

		final org.lwjgl.util.vector.Matrix4f projectionMatrix = JumboMathHandler.createProjectionMatrix(FOV, Z_NEAR,
				Z_FAR);
		prog.createUniform("projectionMatrix");
		prog.setUniform("projectionMatrix", projectionMatrix);

		prog.createUniform("worldMatrix");
		prog.setUniform("worldMatrix", new Matrix4f().translate(new Vector3f(0.6f, -1.8f, -44.5f)));

		prog.createUniform("color");

		prog.createUniform("lightPosition");
		prog.createUniform("lightColor");

		prog.setUniform("lightPosition", new Vector3f(0.6f, 43f, -6f));
		prog.setUniform("lightColor", new Vector3f(1, 1, 1));
		prog.createUniform("viewMatrix");

		prog.unbind();

		// RobotModelData.init();

		// IntBuffer indicesBuffer =
		// BufferUtils.createIntBuffer(indices.length);
		// FloatBuffer verticesBuffer =
		// BufferUtils.createFloatBuffer(vertices.length);
		// verticesBuffer.put(vertices);
		//
		// indicesBuffer.put(indices);

		// IntBuffer indicesBuffer =
		// BufferUtils.createIntBuffer(RobotModelData.faces.size() * 3);
		// FloatBuffer verticesBuffer =
		// BufferUtils.createFloatBuffer(RobotModelData.vertices.size() * 3);
		//
		// for (Vector3f v : RobotModelData.vertices) {
		// verticesBuffer.put(new float[] { v.x, v.y, v.z });
		// }
		// for (Vector3f[] varr : RobotModelData.faces) {
		// for (Vector3f v : varr) {
		// indicesBuffer.put((int) v.x);
		// }
		// }
		//
		// verticesBuffer.flip();
		// indicesBuffer.flip();

		frame = Loader.loadToVAO(OBJFileLoader.loadOBJ("robot-frame.obj"));
		flywheels = Loader.loadToVAO(OBJFileLoader.loadOBJ("robot-flywheels.obj"));
		rdrive = Loader.loadToVAO(OBJFileLoader.loadOBJ("robot-right-drive.obj"));
		ldrive = Loader.loadToVAO(OBJFileLoader.loadOBJ("robot-left-drive.obj"));
		intake = Loader.loadToVAO(OBJFileLoader.loadOBJ("robot-intake.obj"));
		ball = Loader.loadToVAO(OBJFileLoader.loadOBJ("ball.obj"));

		Util.checkGLError();

		// float aspectRatio = (float) Jumbo.getFrameWidth() /
		// Jumbo.getFrameHeight();
		// projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
		// Z_NEAR, Z_FAR);
		//

	}

	private final ShaderProgram prog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jumbo.core.JumboRenderMode#render(com.jumbo.core.JumboGraphicsObject,
	 * int, int)
	 */
	@Override
	public void render(JumboGraphicsObject e, int renderwidth, int renderheight) {

		Util.checkGLError();

		prog.setUniform("color", Properties.WHITE);
		glBindVertexArray(frame.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL_TRIANGLES, frame.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		prog.setUniform("color", Properties.intakeColor);
		glBindVertexArray(intake.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL_TRIANGLES, intake.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		prog.setUniform("color", Properties.lDriveColor);
		glBindVertexArray(ldrive.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL_TRIANGLES, ldrive.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		prog.setUniform("color", Properties.rDriveColor);
		glBindVertexArray(rdrive.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL_TRIANGLES, rdrive.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		prog.setUniform("color", Properties.flywheelColor);
		glBindVertexArray(flywheels.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL_TRIANGLES, flywheels.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		if ((boolean) Properties.properties.get("ballIn").getValue()) {
			prog.setUniform("color", new Vector4f(1, 0.5f, 0, 1));
			glBindVertexArray(ball.getVaoID());
			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(2);
			GL11.glDrawElements(GL_TRIANGLES, ball.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		glBindVertexArray(0);

		prog.unbind();

		// JumboTexture.unbind();

		// super.render(e, renderwidth, renderheight);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.jumbo.core.JumboRenderMode#prepare()
	 */
	@Override
	public void prepare() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		prog.bind();

		final float MOVE_SPEED = 0.5f;

		if (JumboInputHandler.isKeyDown(JumboKey.S)) {
			rotation.setX(rotation.getX() - MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.W)) {
			rotation.setX(rotation.getX() + MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.A)) {
			rotation.setY(rotation.getY() - MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.D)) {
			rotation.setY(rotation.getY() + MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.Q)) {
			rotation.setZ(rotation.getZ() + MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.E)) {
			rotation.setZ(rotation.getZ() - MOVE_SPEED);
		}
		if (JumboInputHandler.wheel > 0) {
			offset.setZ(offset.getZ() + MOVE_SPEED * 5);
		} else if (JumboInputHandler.wheel < 0) {
			offset.setZ(offset.getZ() - MOVE_SPEED * 5);
		}

		final Matrix4f worldMatrix = (Matrix4f) new Matrix4f().setIdentity();
		worldMatrix.translate(offset);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), worldMatrix, worldMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), worldMatrix, worldMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), worldMatrix, worldMatrix);

		prog.setUniform("viewMatrix", worldMatrix);
		prog.setUniform("color", new Vector4f(1, 1, 1, 1));

		System.out.println(worldMatrix + " ");

		Properties.update();
	}

	@Override
	public void init() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		GL11.glDisable(GL11.GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}