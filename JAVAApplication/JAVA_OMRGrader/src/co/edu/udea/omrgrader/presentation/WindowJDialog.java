package co.edu.udea.omrgrader.presentation;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class WindowJDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -377752605547819806L;
	private String windowName = "Image Processed";
	private String imageNameAndLocation;
	private JLabel dataJLabels;
	private final int width = 700;
	private final int heigh = 800;
	private Point pointPosition;

	public WindowJDialog(Frame owner, String imageNameAndLocation,
			String windowName, boolean left) {
		super(owner, false);

		this.setImageNameAndLocation(imageNameAndLocation);
		this.windowName += windowName;

		this.setTitle(this.windowName);
		this.setResizable(false);
		this.setSize(width, heigh);

		if (left) {
			pointPosition = WindowLocation.calculateLeftLocation(
					this.getWidth(), this.getHeight());
		} else {
			pointPosition = WindowLocation.calculateRightLocation(
					this.getWidth(), this.getHeight());
		}

		this.setLocation(pointPosition);
		this.setVisible(false);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				WindowJDialog.this.closeWindow();
			}
		});

		this.createComponents();
	}

	private void createComponents() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		createScrollPane();
	}

	private void createScrollPane() {
		ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.setSize(this.getWidth(), (int) (this.getHeight() * 0.95));

		this.dataJLabels = new JLabel("");
		this.dataJLabels.setIcon(new ImageIcon(this.getImageNameAndLocation()));

		scrollPane.add(this.dataJLabels);
		this.getContentPane().add(scrollPane);
	}

	public void closeWindow() {
		this.removeAll();
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public void startWindow() {
		this.setVisible(true);
	}

	public String getImageNameAndLocation() {
		return imageNameAndLocation;
	}

	public void setImageNameAndLocation(String imageNameAndLocation) {
		this.imageNameAndLocation = imageNameAndLocation;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
}
