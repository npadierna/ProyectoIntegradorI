package co.edu.udea.omrgrader.model.imageprocesor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import co.edu.udea.omrgrader.model.session.QuestionItem;

public class ImageProcesor {

	private static ImageProcesor instance = null;

	private ImageProcesor() {
		super();
	}

	public static ImageProcesor getInstance() {
		if (instance == null) {
			instance = new ImageProcesor();
		}
		return instance;
	}

	public void executeProcessing(String refer_path, String solu_path,
			String processedImageDirectory, String blackWhiteImageDirectory) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat image_refer = Highgui.imread(refer_path,
				Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat image_solu = Highgui.imread(solu_path,
				Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		/* 1 Detectando los puntos */
		FeatureDetector surfDetector = FeatureDetector
				.create(FeatureDetector.SURF);

		MatOfKeyPoint keyPoints_ref = new MatOfKeyPoint();
		MatOfKeyPoint keyPoints_solu = new MatOfKeyPoint();

		surfDetector.detect(image_refer, keyPoints_ref);
		surfDetector.detect(image_solu, keyPoints_solu);

		/* 2 Creando los descriptores */
		DescriptorExtractor surfExtractor = DescriptorExtractor
				.create(DescriptorExtractor.SURF);

		Mat descriptors_ref = new Mat();
		Mat descriptors_solu = new Mat();

		surfExtractor.compute(image_refer, keyPoints_ref, descriptors_ref);
		surfExtractor.compute(image_solu, keyPoints_solu, descriptors_solu);

		/* 3 Haciendo el Maching */
		DescriptorMatcher matcher = DescriptorMatcher
				.create(DescriptorMatcher.FLANNBASED);

		MatOfDMatch matOfDMatch = new MatOfDMatch();

		matcher.match(descriptors_ref, descriptors_solu, matOfDMatch);

		double dist;
		double max_dist = 0.0;
		double min_dist = 100.0;
		List<DMatch> matches = matOfDMatch.toList();
		for (int row = 0; row < descriptors_ref.rows(); row++) {
			dist = matches.get(row).distance;

			if (dist < min_dist) {
				min_dist = dist;
			}

			if (dist > max_dist) {
				max_dist = dist;
			}

		}

		List<DMatch> good_matches = new ArrayList<DMatch>();
		for (int row = 0; row < descriptors_ref.rows(); row++) {
			if (matches.get(row).distance < 2.0 * (min_dist + 0.001)) {
				good_matches.add(matches.get(row));
			}
		}

		MatOfDMatch matOfDMatches = new MatOfDMatch(
				good_matches.toArray(new DMatch[good_matches.size()]));
		Mat img_matches = new Mat();

		Features2d.drawMatches(image_refer, keyPoints_ref, image_solu,
				keyPoints_solu, matOfDMatches, img_matches, Scalar.all(-1.0),
				Scalar.all(-1.0), new MatOfByte(),
				Features2d.NOT_DRAW_SINGLE_POINTS);

		List<Point> refer = new ArrayList<Point>();
		List<KeyPoint> keyPointsList_ref = keyPoints_ref.toList();

		List<Point> solu = new ArrayList<Point>();
		List<KeyPoint> keyPointList_solu = keyPoints_solu.toList();

		for (int i = 0; i < good_matches.size(); i++) {
			refer.add(keyPointsList_ref.get(good_matches.get(i).queryIdx).pt);
			solu.add(keyPointList_solu.get(good_matches.get(i).trainIdx).pt);
		}

		MatOfPoint2f matOfPoint2f_refer = new MatOfPoint2f(
				refer.toArray(new Point[refer.size()]));
		MatOfPoint2f matOfPoint2f_solu = new MatOfPoint2f(
				solu.toArray(new Point[solu.size()]));
		Mat H = Calib3d.findHomography(matOfPoint2f_refer, matOfPoint2f_solu,
				Calib3d.RANSAC, 3.0);

		List<Point> corners_template = new ArrayList<Point>();
		List<Point> corners_solu = new ArrayList<Point>();

		corners_template.add(new Point(0.0, 0.0));
		corners_template.add(new Point(image_refer.cols(), 0.0));
		corners_template.add(new Point(image_refer.cols(), image_refer.rows()));
		corners_template.add(new Point(0.0, image_refer.rows()));

		Mat corners_tem_mat = Converters
				.vector_Point2d_to_Mat(corners_template);
		Mat corners_sol_mat = new Mat();
		Core.perspectiveTransform(corners_tem_mat, corners_sol_mat, H);
		Converters.Mat_to_vector_Point2d(corners_sol_mat, corners_solu);

		Core.line(img_matches, new Point(corners_solu.get(0).x
				+ corners_template.get(1).x, corners_solu.get(0).y),
				new Point(corners_solu.get(1).x + corners_template.get(1).x,
						corners_solu.get(1).y), new Scalar(0, 255, 0), 4);
		Core.line(img_matches, new Point(corners_solu.get(1).x
				+ corners_template.get(1).x, corners_solu.get(1).y),
				new Point(corners_solu.get(2).x + corners_template.get(1).x,
						corners_solu.get(2).y), new Scalar(0, 255, 0), 4);
		Core.line(img_matches, new Point(corners_solu.get(2).x
				+ corners_template.get(1).x, corners_solu.get(2).y),
				new Point(corners_solu.get(3).x + corners_template.get(1).x,
						corners_solu.get(3).y), new Scalar(0, 255, 0), 4);
		Core.line(img_matches, new Point(corners_solu.get(3).x
				+ corners_template.get(1).x, corners_solu.get(3).y),
				new Point(corners_solu.get(0).x + corners_template.get(1).x,
						corners_solu.get(0).y), new Scalar(0, 255, 0), 4);

		List<Point> center_locations = new ArrayList<Point>();
		// List<Integer> yCoordinatesList = new
		// ArrayList<Integer>(Arrays.asList(
		// 168, 196, 223, 251, 278, 305, 333, 360, 388, 416, 443, 471,
		// 498, 526, 554));
		List<Integer> y_cor = new ArrayList<Integer>(
				Arrays.asList(294, 333, 372, 411, 450, 489, 528, 567, 606, 645,
						684, 723, 762, 801, 840));

		for (int row = 0; row < 15; row++) {
			center_locations.add(new Point(168, y_cor.get(row)));
			center_locations.add(new Point(210, y_cor.get(row)));
			center_locations.add(new Point(252, y_cor.get(row)));
			center_locations.add(new Point(294, y_cor.get(row)));
		}

		for (int row = 0; row < 15; row++) {
			center_locations.add(new Point(521, y_cor.get(row)));
			center_locations.add(new Point(563, y_cor.get(row)));
			center_locations.add(new Point(605, y_cor.get(row)));
			center_locations.add(new Point(647, y_cor.get(row)));
		}

		Mat center_locations_mat = Converters
				.vector_Point2d_to_Mat(center_locations);
		Mat center_locations_transfered = new Mat();
		Core.perspectiveTransform(center_locations_mat,
				center_locations_transfered, H);

		List<Point> center_locations_t = new ArrayList<Point>();
		Converters.Mat_to_vector_Point2d(center_locations_transfered,
				center_locations_t);

		for (int counter = 0; counter < center_locations_t.size(); counter++) {
			Core.circle(
					img_matches,
					new Point(center_locations_t.get(counter).x
							+ corners_template.get(1).x, center_locations_t
							.get(counter).y), 3, new Scalar(0, 0, 255), -1);
		}

		// 439 - Auto_grader.java
		// FIXME: This String must be fixed.
		String computedAnsweredPhotodPath = this.writePhotoFile(
				"examForProcessing-Processed.png", img_matches, new File(
						processedImageDirectory));

		Mat imageSolutionMat = this.convertImageToBlackWhite(image_solu,
				"examForProcessing-BlackAndWhite.png", new File(
						blackWhiteImageDirectory), false);

		List<QuestionItem> questionItemsList = this.getAnswers(
				imageSolutionMat, center_locations_t, 10);

	}

	private List<QuestionItem> getAnswers(Mat imageToProcessMat,
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
			writeForConsole(answers, (i + 1));
		}

		return (answersList);
	}

	private int getWhitePixelsInCircle(Mat imageToProcessMat,
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

	private Mat convertImageToBlackWhite(Mat imageMat, String filePhotoName,
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

	private String writePhotoFile(String filePhotoName, Mat imageMat,
			File directoryFile) {
		File file = new File(directoryFile, filePhotoName);

		filePhotoName = file.toString();
		Highgui.imwrite(filePhotoName, imageMat);

		return (filePhotoName);
	}

	private void writeForConsole(boolean answer[], int i) {
		System.out.printf("%s%d [%b, %b, %b, %b]\n", "Question #", i, answer[0],
				answer[1], answer[2], answer[3]);

	}
}
