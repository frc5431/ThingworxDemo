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
				MODE_ID = JumboRenderer.addRenderMode(new RenderMode3D());
				// JumboRenderer.setCurrentRenderMode(MODE_ID);
				// JumboRenderer.removeRenderMode(JumboRenderer.locationOfMode(JumboRenderer.getMode(0)));
			} catch (Exception e) {
				JumboErrorHandler.handle(e);
			}

			JumboRenderer.setRefreshcolor(0, 0.7f, 1f);

			final JumboLayer l = new JumboLayer();

			l.addEntity(new Database());
			l.addEntity(new RobotObject());
			final JumboTextBox title = new JumboTextBox(new Rectangle(0, 0, 720, 100),
					new JumboText("<#FFAAFFs64>DEMIO"));
			title.setMaintainwidth(false);
			;
			title.setMaintainingPosition(true);
			l.addEntity(title);

			final JumboScene s = new JumboScene(l);
			Jumbo.setScene(s);
		});
		final JumboLaunchConfig launch = new JumboLaunchConfig("FRC 5431", new Dimension(720, 480),
				"res/fonts/liavishis");
		launch.resizable = true;
		Jumbo.start(launch);
	}
}
