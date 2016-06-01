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

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.jumbo.core.Jumbo;
import com.jumbo.core.JumboGraphicsObject;
import com.jumbo.core.JumboRenderMode;
import com.jumbo.tools.loaders.JumboStringHandler;

public class ThreeDimRenderer extends JumboRenderMode {
	float depth = -0.5f;

	float[] vertices = new float[] { 0, 0, depth, 0, 0.5f, depth, 0.5f, 0.0f, depth };
	int[] indices = new int[] { 0, 1, 2, 0 };

	int vboId, vaoId, idxVboId;

	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private Matrix4f projectionMatrix;

	public ThreeDimRenderer() throws Exception {

		prog = new ShaderProgram();
		prog.createVertexShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator + "res"
				+ File.separator + "shaders" + File.separator + "vertex.vs"));
		prog.createFragmentShader(JumboStringHandler.loadAsString(System.getProperty("user.dir") + File.separator
				+ "res" + File.separator + "shaders" + File.separator + "fragment.fs"));
		prog.link();

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices).flip();

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();

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

		float aspectRatio = (float) Jumbo.getFrameWidth() / Jumbo.getFrameHeight();
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

		prog.createUniform("projectionMatrix");
		prog.setUniform("projectionMatrix", projectionMatrix);

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
		prog.bind();

		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);

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

	}

	@Override
	public void init() {
		// GL11.glEnable(GL11.GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}
