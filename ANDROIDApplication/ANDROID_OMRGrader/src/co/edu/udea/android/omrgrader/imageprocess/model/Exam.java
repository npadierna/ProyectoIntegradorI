package co.edu.udea.android.omrgrader.imageprocess.model;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class Exam {

	private List<QuestionItem> questionsItemsList;
	private String imageAbsolutePath;

	private Mat grayScaledImageMat;
	private Mat imageDescriptorsMat;
	private MatOfKeyPoint imageMatOfKeyPoints;

	public Exam(String imageAbsolutePath, Mat grayScaledImageMat,
			Mat imageDescriptorsMat, MatOfKeyPoint imageMatOfKeyPoints,
			List<QuestionItem> questionsItemsList) {
		super();

		this.setGrayScaledImageMat(grayScaledImageMat);
		this.setImageAbsolutePath(imageAbsolutePath);
		this.setImageDescriptorsMat(imageDescriptorsMat);
		this.setImageMatOfKeyPoints(imageMatOfKeyPoints);
		this.setQuestionsItemsList(questionsItemsList);
	}

	public List<QuestionItem> getQuestionsItemsList() {

		return (this.questionsItemsList);
	}

	public void setQuestionsItemsList(List<QuestionItem> questionsItemsList) {
		this.questionsItemsList = questionsItemsList;
	}

	public String getImageAbsolutePath() {

		return (this.imageAbsolutePath);
	}

	public void setImageAbsolutePath(String imageAbsolutePath) {
		this.imageAbsolutePath = imageAbsolutePath;
	}

	public Mat getGrayScaledImageMat() {

		return (this.grayScaledImageMat);
	}

	public void setGrayScaledImageMat(Mat grayScaledImageMat) {
		this.grayScaledImageMat = grayScaledImageMat;
	}

	public Mat getImageDescriptorsMat() {

		return (this.imageDescriptorsMat);
	}

	public void setImageDescriptorsMat(Mat imageDescriptorsMat) {
		this.imageDescriptorsMat = imageDescriptorsMat;
	}

	public MatOfKeyPoint getImageMatOfKeyPoints() {

		return (this.imageMatOfKeyPoints);
	}

	public void setImageMatOfKeyPoints(MatOfKeyPoint imageMatOfKeyPoints) {
		this.imageMatOfKeyPoints = imageMatOfKeyPoints;
	}
}