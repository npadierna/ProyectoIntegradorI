package co.edu.udea.omrgrader.model.imageprocesor.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import co.edu.udea.omrgrader.model.imageprocesor.IImageProcess;

public class ImageProcess implements IImageProcess {

	public ImageProcess() {
		super();
	}

	@Override
	public Mat convertImageToBlackWhite(Mat imageMat, String filePhotoName,
			File directoryFile, boolean applyGaussBlur) {
		Mat imageInGrayMat = imageMat.clone();

		if (applyGaussBlur) {
			Imgproc.GaussianBlur(imageInGrayMat, imageInGrayMat,
					new Size(3, 3), 0, 0);
		}

		double thresh = Imgproc.threshold(imageInGrayMat, imageInGrayMat, 0,
				255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

		Imgproc.threshold(imageInGrayMat, imageInGrayMat, thresh, 255,
				Imgproc.THRESH_BINARY_INV);

		this.writePhotoFile(filePhotoName, imageInGrayMat, directoryFile);

		return (imageInGrayMat);
	}

	@Override
	public String writePhotoFile(String filePhotoName, Mat imageMat,
			File directoryFile) {
		File file = new File(directoryFile, filePhotoName);

		filePhotoName = file.toString();
		Highgui.imwrite(filePhotoName, imageMat);

		return (filePhotoName);
	}

	@Override
	public void writeForConsole(boolean[] answer, int i) {
		System.out.printf("%s%d [%b, %b, %b, %b]\n", "Question #", i,
				answer[0], answer[1], answer[2], answer[3]);
	}

	public void showImage(Mat image) {
		if (!image.empty()) {
			Image imagenMostrar = converter(image);
			int width = imagenMostrar.getWidth(null);
			int height = imagenMostrar.getHeight(null);
		}
	}

	private Image converter(Mat imagen) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", imagen, matOfByte);

		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;

		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Image) bufImage;
	}
}
