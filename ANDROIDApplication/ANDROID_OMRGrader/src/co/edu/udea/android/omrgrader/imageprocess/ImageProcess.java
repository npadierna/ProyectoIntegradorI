package co.edu.udea.android.omrgrader.imageprocess;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class ImageProcess {

	public ImageProcess() {
		super();
	}

	public Mat convertImageToBlackWhite(Mat imageMat, boolean applyGaussBlur) {
		Mat imageCloneMat = imageMat.clone();

		if (applyGaussBlur) {
			Imgproc.GaussianBlur(imageCloneMat, imageCloneMat, new Size(3, 3),
					0, 0);
		}

		double thresh = Imgproc.threshold(imageCloneMat, imageCloneMat, 0, 255,
				Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

		Imgproc.threshold(imageCloneMat, imageCloneMat, thresh, 255,
				Imgproc.THRESH_BINARY_INV);

		return (imageCloneMat);
	}

	public String writePhotoFileToSDCard(String filePhotoName, Mat imageMat,
			File sdCardAbsolutePathFile) {
		File file = new File(sdCardAbsolutePathFile, filePhotoName);

		filePhotoName = file.toString();
		Highgui.imwrite(filePhotoName, imageMat);

		return (filePhotoName);
	}
}