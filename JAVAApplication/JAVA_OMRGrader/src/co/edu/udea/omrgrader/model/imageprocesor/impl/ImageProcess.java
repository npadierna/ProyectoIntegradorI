package co.edu.udea.omrgrader.model.imageprocesor.impl;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import co.edu.udea.omrgrader.model.imageprocesor.IImageProcess;

public class ImageProcess implements IImageProcess {

	public ImageProcess() {
		super();
	}

	@Override
	public Mat convertImageToBlackWhite(Mat imageMat, boolean applyGaussBlur) {
		Mat imageInGrayMat = imageMat.clone();

		if (applyGaussBlur) {
			Imgproc.GaussianBlur(imageInGrayMat, imageInGrayMat,
					new Size(3, 3), 0, 0);
		}

		double thresh = Imgproc.threshold(imageInGrayMat, imageInGrayMat, 0,
				255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

		Imgproc.threshold(imageInGrayMat, imageInGrayMat, thresh, 255,
				Imgproc.THRESH_BINARY_INV);

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
		System.out.printf("%s%02d [%b, %b, %b, %b, %b]\n", "Question #", i,
				answer[0], answer[1], answer[2], answer[3], answer[4]);
	}
}
