package com.frc5431.thingworx.core;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ShaderProgram {

	private final int programId;

	private int vertexShaderId;

	private int fragmentShaderId;

	private final Map<String, Integer> uniforms;

	public ShaderProgram() throws Exception {
		uniforms = new HashMap<>();

		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception("Could not create Shader");
		}
	}

	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Code: " + shaderId);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetShaderInfoLog(programId, 1024));
		}

	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (programId != 0) {
			if (vertexShaderId != 0) {
				glDetachShader(programId, vertexShaderId);
			}
			if (fragmentShaderId != 0) {
				glDetachShader(programId, fragmentShaderId);
			}
			glDeleteProgram(programId);
		}
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = GL20.glGetUniformLocation(programId, uniformName);
		Util.checkGLError();
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform:" + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f worldMatrix) {
		final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		worldMatrix.store(fb);
		fb.flip();
		GL20.glUniformMatrix4(uniforms.get(uniformName), false, fb);
		Util.checkGLError();
	}

	public void setUniform(String uniformName, org.joml.Matrix4f worldMatrix) {
		final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		worldMatrix.get(fb);
		fb.flip();
		GL20.glUniformMatrix4(uniforms.get(uniformName), false, fb);
		Util.checkGLError();
	}

	public void setUniform(String uniformName, int value) {
		GL20.glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		GL20.glUniform1f(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, org.joml.Vector3f value) {
		GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, Vector4f value) {
		GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	public void bindAttributeLocation(int id, String name) {
		GL20.glBindAttribLocation(programId, id, name);
	}

	public void createDirectionalLightUniform(String uniformName) throws Exception {
		createUniform(uniformName + ".colour");
		createUniform(uniformName + ".direction");
		createUniform(uniformName + ".intensity");
	}

	// ...
	public void setUniform(String uniformName, DirectionalLight dirLight) {
		setUniform(uniformName + ".colour", dirLight.getColor());
		setUniform(uniformName + ".direction", dirLight.getDirection());
		setUniform(uniformName + ".intensity", dirLight.getIntensity());
	}

}