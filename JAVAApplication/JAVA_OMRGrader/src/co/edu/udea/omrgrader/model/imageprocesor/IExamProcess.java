package co.edu.udea.omrgrader.model.imageprocesor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import co.edu.udea.omrgrader.model.session.QuestionItem;

public interface IExamProcess {

	List<Integer> y_cor = new ArrayList<Integer>(Arrays.asList(340, 367, 394,
			421, 448, 475, 502, 529, 556, 583, 610, 637, 664, 691, 718, 745,
			772, 799, 826, 853));

	List<Integer> x_cor = new ArrayList<Integer>(Arrays.asList(116, 145, 174,
			203, 232, 355, 384, 413, 442, 471, 593, 622, 651, 680, 709));

	int TOTAL_QUESTIONS_ITEMS = 60;
	
	int QUESTION_ITEMS_COLUMNS_AMOUNT = 3;
	
	int OPTION_AMOUNT = 5;

	public List<QuestionItem> getAnswers(Mat imageToProcessMat,
			List<Point> pointsList, int radius);

	public int getWhitePixelsInCircle(Mat imageToProcessMat,
			Point currentPoint, int radius);

	public List<Point> buildBubblesCenterLocations();

	public void drawTransferredSquare(Mat imgMatches, Scalar lineColor,
			List<Point> cornersSolu, List<Point> cornersTemplate);

	public void drawTransferredBubbles(Mat image,
			List<Point> centerLocationsTransfered, List<Point> cornersTemplate,
			Scalar bubbleColor, int innerCircleRadius, int outerCircleRadius);
}