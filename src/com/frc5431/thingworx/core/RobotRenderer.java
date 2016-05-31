package com.frc5431.thingworx.core;

import java.awt.Rectangle;

import com.jumbo.core.JumboGraphicsObject;
import com.jumbo.core.JumboTexture;

public class RobotRenderer extends JumboGraphicsObject {

	public RobotRenderer(Rectangle bounds, JumboTexture texture) {
		super(bounds, texture);
	}

	@Override
	protected void customRender() {
		System.out.println(RobotModel.normals);
	}

	@Override
	public void customTick() {

	}

}
