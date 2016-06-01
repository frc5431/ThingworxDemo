package com.frc5431.thingworx.core;

import java.awt.Rectangle;

import com.jumbo.core.JumboGraphicsObject;
import com.jumbo.core.JumboRenderer;

public class RobotObject extends JumboGraphicsObject {

	public RobotObject() {
		super(new Rectangle(0, 0, 720, 480), null);
		// RobotModelData.init();
	}

	@Override
	protected void customRender() {
		JumboRenderer.render(this, Demo.MODE_ID);
	}

	@Override
	public void customTick() {

	}

}
