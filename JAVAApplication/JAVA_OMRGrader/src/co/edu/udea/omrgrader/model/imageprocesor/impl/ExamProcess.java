package co.edu.udea.omrgrader.model.imageprocesor.impl;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import co.edu.udea.omrgrader.model.imageprocesor.IExamProcess;
import co.edu.udea.omrgrader.model.session.QuestionItem;

public class ExamProcess implements IExamProcess {
	
	public ExamProcess() {
		super();
	}

	@Override
	public List<QuestionItem> getAnswers(Mat imageToProcessMat,
			List<Point> pointsList, int radius) {
		int thresh = 150;
		List<QuestionItem> answersList = new ArrayList<QuestionItem>();

		for (int i = 0; i < (pointsList.size() / 4); i++) {
			boolean[] answers = new boolean[4];
			int[] pixelCounter = new int[4];
			StringBuilder stringBuilder = new StringBuilder();

			for (int j = 0; j < 4; j++) {
				int position = i * 4 + j;
				Point point = pointsList.get(position);

				pixelCounter[j] = this.getWhitePixelsInCircle(
						imageToProcessMat, point, radius);
				stringBuilder.append(pixelCounter[j]).append(" ");

				answers[j] = pixelCounter[j] > thresh;
			}

			answersList.add(new QuestionItem((short) (i + 1), answers));
			ImageProcess imageProcess = new ImageProcess();
			imageProcess.writeForConsole(answers, (i + 1));
		}
		return (answersList);
	}

	@Override
	public int getWhitePixelsInCircle(Mat imageToProcessMat,
			Point currentPoint, int radius) {
		int centerAtX = (int) currentPoint.x;
		int centerAtY = (int) currentPoint.y;
		int amoutOfWhitePixeles = 0;

		for (int column = (centerAtX - radius); column < (centerAtX + radius); column++) {
			for (int row = (centerAtY - radius); row < (centerAtY + radius); row++) {
				if ((row < imageToProcessMat.height())
						&& (column <= imageToProcessMat.width())) {
					double valueOfPixel = imageToProcessMat.get(row, column)[0];

					if (valueOfPixel == 255.0) {
						amoutOfWhitePixeles++;
					}
				}
			}
		}
		return (amoutOfWhitePixeles);
	}

}
