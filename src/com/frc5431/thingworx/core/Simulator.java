package com.frc5431.thingworx.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.jumbo.components.FloatRectangle;
import com.jumbo.components.JumboColor;
import com.jumbo.components.LambdaObject;
import com.jumbo.core.Jumbo;
import com.jumbo.core.JumboLaunchConfig;
import com.jumbo.core.JumboLayer;
import com.jumbo.core.JumboRenderer;
import com.jumbo.core.JumboScene;
import com.jumbo.core.JumboTexture;
import com.jumbo.entities.JumboGraphicsGroup;
import com.jumbo.entities.graphics.JumboButton;
import com.jumbo.entities.graphics.JumboImage;
import com.jumbo.entities.graphics.text.JumboText;
import com.jumbo.entities.graphics.text.JumboTextBox;
import com.jumbo.tools.JumboErrorHandler;
import com.jumbo.tools.JumboSettings;

public class Simulator {
	static int MODE_ID;

	public static void main(String... args) throws IOException {
		JumboSettings.fps = 60;
		JumboSettings.logerrors = false;
		JumboSettings.tickdelay = 100;
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

			final JumboTexture arrows = new JumboTexture("res/assets/arrows.png");
			arrows.setTextureCoords(new FloatRectangle(0, 0.5f, 1, 0.5f));
			final JumboTexture arrowsHover = new JumboTexture(arrows);
			arrowsHover.setColor(JumboColor.LIGHT_BLUE);

			final JumboGraphicsGroup info = new JumboGraphicsGroup();
			info.setBounds(new Rectangle(-200, 0, 242, 480));
			info.setMaintainheight(false);
			info.setMaintainingPosition(true);

			final JumboImage cameraBackground = new JumboImage(new JumboTexture(JumboColor.LIGHT_GREY),
					new Rectangle(200, 300, 42, 84));
			info.array.add(cameraBackground);

			info.array.add(new JumboImage(new JumboTexture(JumboColor.LIGHT_GREY), new Rectangle(0, 0, 200, 480)));
			info.array.add(createPropertiesOverlay());

			info.setAllChildrenMaintainingPosition(true);
			info.setAllChildrenMaintainingHeight(false);

			cameraBackground.setMaintainheight(true);
			cameraBackground.setMaintainy(false);

			final LambdaObject<Boolean> extended = new LambdaObject<>(false);

			final JumboButton extendo = new JumboButton(arrows, arrowsHover, new Rectangle(200, 0, 42, 42));
			extendo.setClickAction(() -> {
				if (!extended.get()) {
					extendo.getTexture().setTextureCoords(new FloatRectangle(0, 0, 1, 0.5f));
					extendo.getHoverIcon().getTexture().setTextureCoords(new FloatRectangle(0, 0, 1, 0.5f));
					info.setCustomRenderAction(() -> {
						if (info.getBounds().x < 0) {
							info.getBounds().x += 10;
						}
					});
				} else {
					extendo.getTexture().setTextureCoords(new FloatRectangle(0, 0.5f, 1, 0.5f));
					extendo.getHoverIcon().getTexture().setTextureCoords(new FloatRectangle(0, 0.5f, 1, 0.5f));

					info.setCustomRenderAction(() -> {
						if (info.getBounds().x > -200) {
							info.getBounds().x -= 10;
						}
					});
				}
				extended.set(!extended.get());
			});
			extendo.setMaintainingPosition(true);
			info.array.add(extendo);

			final JumboTexture cameras = new JumboTexture("res/assets/cameras.png");
			final JumboTexture manualCamera = new JumboTexture(cameras), autoCamera = new JumboTexture(cameras);
			manualCamera.setTextureCoords(new FloatRectangle(0, 0.5f, 1, 0.5f));
			autoCamera.setTextureCoords(new FloatRectangle(0, 0, 1, 0.5f));

			final JumboButton autoCameraButton = new JumboButton(autoCamera,
					new JumboTexture(autoCamera, JumboColor.DARK_BLUE), new Rectangle(0, 5, 32, 32));
			autoCameraButton.setMaintainingPosition(true);
			autoCameraButton.addParent(cameraBackground);
			autoCameraButton.setDisabledIcon(new JumboTexture(autoCamera, JumboColor.LIGHT_BLUE));

			final JumboButton manualCameraButton = new JumboButton(manualCamera,
					new JumboTexture(manualCamera, JumboColor.DARK_BLUE), new Rectangle(0, 47, 32, 32));
			manualCameraButton.setMaintainingPosition(true);
			manualCameraButton.addParent(cameraBackground);
			manualCameraButton.setDisabledIcon(new JumboTexture(manualCamera, JumboColor.LIGHT_BLUE));
			manualCameraButton.setClickAction(() -> {
				autoCameraButton.setActive(true);
				manualCameraButton.setActive(false);
				RenderMode3D.mode = RenderMode3D.CAMERA_MODE.MANUAL;
			});
			l.addEntity(manualCameraButton);

			autoCameraButton.setClickAction(() -> {
				autoCameraButton.setActive(false);
				manualCameraButton.setActive(true);
				RenderMode3D.mode = RenderMode3D.CAMERA_MODE.AUTO;
			});
			autoCameraButton.trigger();

			l.addEntity(info);
			l.addEntity(autoCameraButton);
			l.addEntity(manualCameraButton);

			final JumboScene s = new JumboScene(l);
			Jumbo.setScene(s);
		});
		final JumboLaunchConfig launch = new JumboLaunchConfig("FRC 5431", new Dimension(720, 480),
				new BufferedImage[] { ImageIO.read(new File("res/assets/logo.png")) }, "res/fonts/verdana");
		launch.resizable = true;
		Jumbo.start(launch);
	}

	private static JumboGraphicsGroup createPropertiesOverlay() {
		final JumboGraphicsGroup g = new JumboGraphicsGroup(new Rectangle(0, 0, 200, 480));
		final Iterator<String> i = Properties.properties.keySet().iterator();
		int yOffset = 50;

		for (Property p : Properties.properties.values()) {
			final String key = i.next();
			final int offset = yOffset;// it has to be a copy
			final JumboGraphicsGroup value = new JumboGraphicsGroup(new Rectangle(0, 0, 0, 0)) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.jumbo.core.JumboEntity#additionalCalculations(java.awt.
				 * Rectangle)
				 */
				@Override
				public Rectangle additionalCalculations(Rectangle bounds) {
					bounds.y = Jumbo.getFrameHeight() - offset;
					return super.additionalCalculations(bounds);
				}

			};
			final JumboButton background = new JumboButton(new JumboTexture(JumboColor.DARK_GREY),
					new Rectangle(20, 0, 180, 50));
			background.setTexture(new JumboTexture(JumboColor.DARK_GREY));// the
																			// constructor
																			// doesnt
																			// work
																			// for
																			// some
																			// reason
			value.array.add(background);

			final JumboImage bevel = new JumboImage(new JumboTexture(new JumboColor(0.5f, 0.5f, 0.5f)),
					new Rectangle(20, 5, 175, 40));
			value.array.add(bevel);

			final JumboTextBox title = new JumboTextBox(new Rectangle(20, 15, 180, 30),
					new JumboText("<s30#FFFFFFi1>" + key));
			value.array.add(title);

			final JumboTextBox valueText = new JumboTextBox(new Rectangle(20, 0, 180, 15), new JumboText("Loading..."));
			valueText.setCustomRenderAction(() -> {
				valueText.setText("<s22#FFFFFFi0>" + p.getValue());
			});
			value.array.add(valueText);

			value.setMaintainingPosition(true);
			value.setAllChildrenMaintainingPosition(true);
			g.array.add(value);
			yOffset += 50;
		}

		return g;
	}
}
