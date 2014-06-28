package co.edu.udea.omrgrader.model.imageprocesor.impl;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

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

	@Override
	public List<Point> buildBubblesCenterLocations() {
		List<Point> centerLocations = new ArrayList<Point>();
		for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
			centerLocations.add(new Point(x_cor.get(0), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(1), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(2), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(3), y_cor.get(row)));
		}

		for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
			centerLocations.add(new Point(x_cor.get(4), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(5), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(6), y_cor.get(row)));
			centerLocations.add(new Point(x_cor.get(7), y_cor.get(row)));
		}

		return centerLocations;
	}

	@Override
	public void drawTransferredSquare(Mat imgMatches, Scalar lineColor,
			List<Point> corners_solu, List<Point> corners_template) {

		Core.line(imgMatches, new Point(corners_solu.get(0).x
				+ corners_template.get(1).x, corners_solu.get(0).y),
				new Point(corners_solu.get(1).x + corners_template.get(1).x,
						corners_solu.get(1).y), lineColor, 4);
		Core.line(imgMatches, new Point(corners_solu.get(1).x
				+ corners_template.get(1).x, corners_solu.get(1).y),
				new Point(corners_solu.get(2).x + corners_template.get(1).x,
						corners_solu.get(2).y), lineColor, 4);
		Core.line(imgMatches, new Point(corners_solu.get(2).x
				+ corners_template.get(1).x, corners_solu.get(2).y),
				new Point(corners_solu.get(3).x + corners_template.get(1).x,
						corners_solu.get(3).y), lineColor, 4);
		Core.line(imgMatches, new Point(corners_solu.get(3).x
				+ corners_template.get(1).x, corners_solu.get(3).y),
				new Point(corners_solu.get(0).x + corners_template.get(1).x,
						corners_solu.get(0).y), lineColor, 4);
	}

	@Override
	public void drawTransferredBubbles(Mat image,
			List<Point> centerLocationsTransfered, List<Point> cornersTemplate,
			Scalar bubbleColor, int innerCircleRadius, int outerCircleRadius) {

		for (int counter = 0; counter < centerLocationsTransfered.size(); counter++) {
			Core.circle(image,
					new Point(centerLocationsTransfered.get(counter).x
							+ cornersTemplate.get(1).x,
							centerLocationsTransfered.get(counter).y),
					innerCircleRadius, bubbleColor, -1);

			Core.circle(image,
					new Point(centerLocationsTransfered.get(counter).x
							+ cornersTemplate.get(1).x,
							centerLocationsTransfered.get(counter).y),
					outerCircleRadius, bubbleColor);
		}
	}
}