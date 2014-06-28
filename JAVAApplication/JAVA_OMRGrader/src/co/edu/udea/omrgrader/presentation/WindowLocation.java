package co.edu.udea.omrgrader.presentation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

class WindowLocation {

	private static final int HORIZONTAL_HALF;
	private static final Dimension SCREEN_SIZE;

	static {
		SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		HORIZONTAL_HALF = (int) WindowLocation.SCREEN_SIZE.width / 2;
	}

	public static Point calculateLeftLocation(int wWidth, int wHeight) {
		Point location = new Point();

		location.x = (int) ((WindowLocation.HORIZONTAL_HALF - wWidth) / 2);
		location.y = (int) ((WindowLocation.SCREEN_SIZE.height - wHeight) / 2);

		return (location);
	}

	public static Point calculateRightLocation(int wWidth, int wHeight) {
		Point location = WindowLocation.calculateLeftLocation(wWidth, wHeight);
		location.x += WindowLocation.HORIZONTAL_HALF;

		return (location);
	}
}