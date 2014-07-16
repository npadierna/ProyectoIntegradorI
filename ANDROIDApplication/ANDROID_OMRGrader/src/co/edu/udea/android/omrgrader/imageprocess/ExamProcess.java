package co.edu.udea.android.omrgrader.imageprocess;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class ExamProcess {

	private int questionsOptionsAmout;

	private List<Point> bubblesCentersPointsList;

	public ExamProcess(List<Point> bubblesCentersPointsList,
			int questionsOptionsAmout) {
		super();

		this.questionsOptionsAmout = questionsOptionsAmout;
		this.bubblesCentersPointsList = bubblesCentersPointsList;
	}

	public List<QuestionItem> findAnswers(Mat examPictureMat, int bubbleRadius,
			int thresh) {
		// int thresh = 150;
		List<QuestionItem> questionsItemsList = new ArrayList<QuestionItem>();

		for (int i = 0; i < (this.bubblesCentersPointsList.size() / this.questionsOptionsAmout); i++) {
			boolean[] answers = new boolean[this.questionsOptionsAmout];
			int[] pixelCounter = new int[this.questionsOptionsAmout];
			StringBuilder stringBuilder = new StringBuilder();

			for (int j = 0; j < this.questionsOptionsAmout; j++) {
				int position = i * this.questionsOptionsAmout + j;
				Point point = this.bubblesCentersPointsList.get(position);

				pixelCounter[j] = this.countWhitePixelsInBubble(examPictureMat,
						point, bubbleRadius);
				stringBuilder.append(pixelCounter[j]).append(" ");

				answers[j] = pixelCounter[j] > thresh;
			}

			questionsItemsList.add(new QuestionItem((short) (i + 1), answers));

			// FIXME: TAG.
			// Log.i(OMRGraderProcess.TAG, "White pixeles count for question #"
			// + i + ": " + stringBuilder.toString());
		}

		return (questionsItemsList);
	}

	private int countWhitePixelsInBubble(Mat imageMat, Point bubbleCenterPoint,
			int bubbleRadius) {
		int centerAtX = (int) bubbleCenterPoint.x;
		int centerAtY = (int) bubbleCenterPoint.y;
		int countedWhitePixeles = 0;

		for (int column = (centerAtX - bubbleRadius); column < (centerAtX + bubbleRadius); column++) {
			for (int row = (centerAtY - bubbleRadius); row < (centerAtY + bubbleRadius); row++) {
				if ((row < imageMat.height()) && (column <= imageMat.width())) {
					double valueOfPixel = imageMat.get(row, column)[0];

					if (valueOfPixel == 255.0) {
						countedWhitePixeles++;
					}
				}
			}
		}

		return (countedWhitePixeles);
	}
}