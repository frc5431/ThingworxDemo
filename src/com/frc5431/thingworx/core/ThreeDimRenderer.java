package com.frc5431.thingworx.core;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.jumbo.core.JumboGraphicsObject;
import com.jumbo.core.JumboRenderMode;
import com.jumbo.tools.calculations.JumboMathHandler;
import com.jumbo.tools.input.JumboInputHandler;
import com.jumbo.tools.input.JumboKey;
import com.jumbo.tools.loaders.JumboStringHandler;

public class ThreeDimRenderer extends JumboRenderMode {
	float depth = -10.0f;

	float[] vertices = new float[] { 0, 0, depth, 0, 0.5f, depth, 0.5f, 0, depth };
	int[] indices = new int[] { 0, 1, 2, 0 };

	int vboId, vaoId, idxVboId;

	private static final float FOV = 70;

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.0f;

	private Vector3f offset = new Vector3f(0, 0, 1000), rotation = new Vector3f(0, 0, 0);

	public ThreeDimRenderer() throws Exception {

		prog = new ShaderProgram();
		prog.createVertexShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator + "res"
				+ File.separator + "shaders" + File.separator + "vertex.vs"));
		prog.createFragmentShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator
				+ "res" + File.separator + "shaders" + File.separator + "fragment.fs"));
		prog.link();

		prog.bind();

		prog.createUniform("projectionMatrix");
		prog.setUniform("projectionMatrix", JumboMathHandler.createProjectionMatrix(FOV, Z_NEAR, Z_FAR));

		prog.createUniform("worldMatrix");

		prog.unbind();

		RobotModelData.init();

		// IntBuffer indicesBuffer =
		// BufferUtils.createIntBuffer(indices.length);
		// FloatBuffer verticesBuffer =
		// BufferUtils.createFloatBuffer(vertices.length);
		// verticesBuffer.put(vertices);
		//
		// indicesBuffer.put(indices);

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(RobotModelData.faces.size() * 3);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(RobotModelData.vertices.size() * 3);

		for (Vector3f v : RobotModelData.vertices) {
			verticesBuffer.put(new float[] { v.x, v.y, v.z });
		}
		for (Vector3f[] varr : RobotModelData.faces) {
			for (Vector3f v : varr) {
				indicesBuffer.put((int) v.x);
			}
		}

		verticesBuffer.flip();
		indicesBuffer.flip();

		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		idxVboId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Unbind the VAO
		glBindVertexArray(0);

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

		GL11.glDrawElements(GL_TRIANGLES, RobotModelData.vertices.size() * 3, GL11.GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		prog.unbind();

		// super.render(e, renderwidth, renderheight);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.jumbo.core.JumboRenderMode#prepare()
	 */
	@Override
	public void prepare() {
		prog.bind();

		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);

		final float MOVE_SPEED = 10.0f;

		if (JumboInputHandler.isKeyDown(JumboKey.D)) {
			offset.setX(offset.getX() - MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.A)) {
			offset.setX(offset.getX() + MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.W)) {
			offset.setY(offset.getY() - MOVE_SPEED);
		}
		if (JumboInputHandler.isKeyDown(JumboKey.S)) {
			offset.setY(offset.getY() + MOVE_SPEED);
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
		worldMatrix.translate(new Vector3f(offset));
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), worldMatrix, worldMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), worldMatrix, worldMatrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), worldMatrix, worldMatrix);

		prog.setUniform("worldMatrix", worldMatrix);
	}

	@Override
	public void init() {
		// GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}
