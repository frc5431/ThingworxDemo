package com.frc5431.thingworx.core;

import org.joml.Vector3f;

public class DirectionalLight {

	private Vector3f color;

	private Vector3f direction;

	private float intensity;

	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}

	public DirectionalLight(DirectionalLight light) {
		this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
	}

	/**
	 * @return the color
	 */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}

	/**
	 * @return the direction
	 */
	public org.joml.Vector3f getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	/**
	 * @return the intensity
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * @param intensity
	 *            the intensity to set
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
