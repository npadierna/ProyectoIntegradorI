package co.edu.udea.omrgrader.model.imageprocesor;

import java.io.File;

import org.opencv.core.Mat;

public interface IImageProcess {

	public Mat convertImageToBlackWhite(Mat imageMat, boolean applyGaussBlur);

	public String writePhotoFile(String filePhotoName, Mat imageMat,
			File directoryFile);

	public void writeForConsole(boolean answer[], int i);

}