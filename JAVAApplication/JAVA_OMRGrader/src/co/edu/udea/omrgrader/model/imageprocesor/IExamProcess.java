package co.edu.udea.omrgrader.model.imageprocesor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import co.edu.udea.omrgrader.model.session.QuestionItem;

public interface IExamProcess {
	
	List<Integer> y_cor = new ArrayList<Integer>(
			Arrays.asList(294, 333, 372, 411, 450, 489, 528, 567, 606, 645,
					684, 723, 762, 801, 840));
	
	List<Integer> x_cor = new ArrayList<Integer>(
			Arrays.asList(168, 210, 252, 294, 521, 563, 605, 647));
	
	public List<QuestionItem> getAnswers(Mat imageToProcessMat,
			List<Point> pointsList, int radius);
	
	public int getWhitePixelsInCircle(Mat imageToProcessMat,
			Point currentPoint, int radius);

}


