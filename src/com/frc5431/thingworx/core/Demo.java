package com.frc5431.thingworx.core;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.jumbo.core.Jumbo;
import com.jumbo.core.JumboLaunchConfig;
import com.jumbo.core.JumboLayer;
import com.jumbo.core.JumboRenderer;
import com.jumbo.core.JumboScene;
import com.jumbo.entities.graphics.text.JumboText;
import com.jumbo.entities.graphics.text.JumboTextBox;
import com.jumbo.tools.JumboErrorHandler;
import com.jumbo.tools.JumboSettings;

public class Demo {
	static int MODE_ID;

	public static void main(String... args) {
		JumboSettings.fps = 60;
		JumboSettings.logerrors = false;
		Jumbo.setLaunchAction(() -> {
			try {
				MODE_ID = JumboRenderer.addRenderMode(new ThreeDimRenderer());
				// JumboRenderer.setCurrentRenderMode(MODE_ID);
				// JumboRenderer.removeRenderMode(JumboRenderer.locationOfMode(JumboRenderer.getMode(0)));
			} catch (Exception e) {
				JumboErrorHandler.handle(e);
			}
			final JumboLayer l = new JumboLayer();

			l.addEntity(new RobotObject());
			l.addEntity(new JumboTextBox(new Rectangle(0, 0, 720, 100), new JumboText("<#FFFFFF>HI")));

			final JumboScene s = new JumboScene(l);
			Jumbo.setScene(s);
		});
		Jumbo.start(new JumboLaunchConfig("FRC 5431", new Dimension(720, 480), "res/fonts/liavishis"));
	}
}
