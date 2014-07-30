package co.edu.udea.android.omrgrader.imageprocess;

import java.io.File;
import java.util.ArrayList;
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
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.utils.Converters;

import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader.imageprocess.model.Exam;
import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class OMRGraderProcess {

	private static final String TAG = OMRGraderProcess.class.getSimpleName();

	public static final DescriptorExtractor DESCRIPTOR_EXTRACTOR = DescriptorExtractor
			.create(DescriptorExtractor.SURF);
	public static final DescriptorMatcher DESCRIPTOR_MATCHER = DescriptorMatcher
			.create(DescriptorMatcher.FLANNBASED);
	public static final FeatureDetector FEATURE_DETECTOR = FeatureDetector
			.create(FeatureDetector.SURF);

	private int bubbleRadius;
	private int thresh;

	private ExamProcess examProcess;
	private ImageProcess imageProcess;

	public OMRGraderProcess(List<Point> bubblesCentersPointsList,
			int questionsOptionsAmout, int bubbleRadius, int thresh) {
		super();

		Log.i(TAG, String.format("Radius Lenght: %d", bubbleRadius));
		Log.i(TAG, String.format("Minimum Thresh: %d", thresh));

		this.setBubbleRadius(bubbleRadius);
		this.setThresh(thresh);

		this.examProcess = new ExamProcess(bubblesCentersPointsList,
				questionsOptionsAmout);
		this.imageProcess = new ImageProcess();
	}

	public int getBubbleRadius() {

		return (this.bubbleRadius);
	}

	public void setBubbleRadius(int bubbleRadius) {
		this.bubbleRadius = bubbleRadius;
	}

	public int getThresh() {

		return (this.thresh);
	}

	public void setThresh(int thresh) {
		this.thresh = thresh;
	}

	public Exam computeDescriptorsKeyPoints(String imageAbsolutePath) {
		Log.v(TAG,
				"computeDescriptorsKeyPoints(String, MatOfKeyPoints, Mat):void");

		Mat imageDescriptorsMat = new Mat();
		Mat grayScaledImageMat = Highgui.imread(imageAbsolutePath,
				Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		MatOfKeyPoint imageMatOfKeyPoints = new MatOfKeyPoint();

		FEATURE_DETECTOR.detect(grayScaledImageMat, imageMatOfKeyPoints);
		DESCRIPTOR_EXTRACTOR.compute(grayScaledImageMat, imageMatOfKeyPoints,
				imageDescriptorsMat);

		return (new Exam(imageAbsolutePath, grayScaledImageMat,
				imageDescriptorsMat, imageMatOfKeyPoints, null));
	}

	public List<QuestionItem> executeProcessing(Exam onlyLogosTemplateExam,
			Exam exam, String blackAndWhiteImageDestinationDirectory,
			String processedImageDestinationDirectory) {
		Log.v(TAG,
				"executeProcessing(Exam, Exam, String, String):List<QuestionItem>");

		MatOfDMatch matOfDMatch = new MatOfDMatch();

		DESCRIPTOR_MATCHER.match(
				onlyLogosTemplateExam.getImageDescriptorsMat(),
				exam.getImageDescriptorsMat(), matOfDMatch);

		double dist;
		double max_dist = 0.0;
		double min_dist = 100.0;
		List<DMatch> matches = matOfDMatch.toList();
		for (int i = 0; i < onlyLogosTemplateExam.getImageDescriptorsMat()
				.rows(); i++) {
			dist = matches.get(i).distance;

			if (dist < min_dist) {
				min_dist = dist;
			}

			if (dist > max_dist) {
				max_dist = dist;
			}
		}

		List<DMatch> good_matches = new ArrayList<DMatch>();
		for (int i = 0; i < onlyLogosTemplateExam.getImageDescriptorsMat()
				.rows(); i++) {
			// 1.2 * (min_dist + 0.00000000001)
			// if (matches.get(i).distance < 1.2 * (min_dist + 0.000000001)) {
			if (matches.get(i).distance < (2.0 * (min_dist + 0.001))) {
				good_matches.add(matches.get(i));
			}
		}

		MatOfDMatch matOfDMatches = new MatOfDMatch(
				good_matches.toArray(new DMatch[good_matches.size()]));
		Mat img_matches = new Mat();
		Features2d.drawMatches(onlyLogosTemplateExam.getGrayScaledImageMat(),
				onlyLogosTemplateExam.getImageMatOfKeyPoints(),
				exam.getGrayScaledImageMat(), exam.getImageMatOfKeyPoints(),
				matOfDMatches, img_matches, Scalar.all(-1.0), Scalar.all(-1.0),
				new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);

		List<Point> refer = new ArrayList<Point>();
		List<KeyPoint> keyPointsList_ref = onlyLogosTemplateExam
				.getImageMatOfKeyPoints().toList();

		List<Point> solu = new ArrayList<Point>();
		List<KeyPoint> keyPointList_solu = exam.getImageMatOfKeyPoints()
				.toList();

		for (int i = 0; i < good_matches.size(); i++) {
			refer.add(keyPointsList_ref.get(good_matches.get(i).queryIdx).pt);
			solu.add(keyPointList_solu.get(good_matches.get(i).trainIdx).pt);
		}

		MatOfPoint2f matOfPoint2f_refer = new MatOfPoint2f(
				refer.toArray(new Point[refer.size()]));
		MatOfPoint2f matOfPoint2f_solu = new MatOfPoint2f(
				solu.toArray(new Point[solu.size()]));
		Mat H = Calib3d.findHomography(matOfPoint2f_refer, matOfPoint2f_solu,
				Calib3d.LMEDS, 3.0);

		List<Point> corners_template = new ArrayList<Point>();
		List<Point> corners_solu = new ArrayList<Point>();

		corners_template.add(new Point(0.0, 0.0));
		corners_template.add(new Point(onlyLogosTemplateExam
				.getGrayScaledImageMat().cols(), 0.0));
		corners_template.add(new Point(onlyLogosTemplateExam
				.getGrayScaledImageMat().cols(), onlyLogosTemplateExam
				.getGrayScaledImageMat().rows()));
		corners_template.add(new Point(0.0, onlyLogosTemplateExam
				.getGrayScaledImageMat().rows()));

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

		Mat center_locations_mat = Converters
				.vector_Point2d_to_Mat(this.examProcess
						.getBubblesCentersPointsList());
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

		if (!TextUtils.isEmpty(processedImageDestinationDirectory)) {
			this.imageProcess.writePhotoFileToSDCard(
					"examForProcessing-Processed.png", img_matches, new File(
							processedImageDestinationDirectory));
		}

		Mat imageSolutionMat = this.imageProcess.convertImageToBlackWhite(
				exam.getGrayScaledImageMat(), false);
		if (!TextUtils.isEmpty(blackAndWhiteImageDestinationDirectory)) {
			this.imageProcess.writePhotoFileToSDCard(
					"examForProcessing-BlackAndWhite.png", imageSolutionMat,
					new File(blackAndWhiteImageDestinationDirectory));
		}

		exam.setQuestionsItemsList(this.examProcess.findAnswers(
				imageSolutionMat, center_locations_t, this.getBubbleRadius(),
				this.getThresh()));

		return (exam.getQuestionsItemsList());
	}
}