package co.edu.udea.android.omrgrader.imageprocess;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

import android.util.Log;

import co.edu.udea.android.omrgrader.model.QuestionItem;

public final class ImageProcessor {

	private static final String TAG = ImageProcessor.class.getSimpleName();

	private static ImageProcessor instance = null;

	private ImageProcessor() {
		super();
	}

	public synchronized static ImageProcessor getInstance() {
		if (instance == null) {
			instance = new ImageProcessor();
		}

		return (instance);
	}

	public List<QuestionItem> executeProcessing(String pictureReferencePath,
			String studentPicturePath) {
		Log.d(ImageProcessor.TAG, "Reference Picture: " + pictureReferencePath);
		Log.d(ImageProcessor.TAG, "Student Picture: " + studentPicturePath);

		// Mat.data == null, if the image can not be read.
		Mat pictureReferenceDetectorMat = Highgui.imread(pictureReferencePath);
		Mat studentPictureDetectorMat = Highgui.imread(studentPicturePath);

		/*
		 * 1. Detecting Key Points using some detector. (SURF Detector).
		 */
		FeatureDetector featureDetector = FeatureDetector
				.create(FeatureDetector.SURF);

		MatOfKeyPoint pictureReferenceMatOfKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint studentPictureMatOfKeyPoints = new MatOfKeyPoint();

		featureDetector.detect(pictureReferenceDetectorMat,
				pictureReferenceMatOfKeyPoints);
		featureDetector.detect(studentPictureDetectorMat,
				studentPictureMatOfKeyPoints);

		/*
		 * 2. Calculating Descriptors. (SURF Descriptor)
		 */
		DescriptorExtractor descriptorExtractor = DescriptorExtractor
				.create(DescriptorExtractor.SURF);

		Mat pictureReferenceDescriptorMat = new Mat();
		Mat studentPictureDescriptorMat = new Mat();

		descriptorExtractor.compute(pictureReferenceDetectorMat,
				pictureReferenceMatOfKeyPoints, pictureReferenceDescriptorMat);
		descriptorExtractor.compute(studentPictureDetectorMat,
				pictureReferenceMatOfKeyPoints, studentPictureDescriptorMat);

		/*
		 * 3. Matching Descriptor vectors using some matcher. (FLANNBASED
		 * Matcher).
		 */
		DescriptorMatcher descriptorMatcher = DescriptorMatcher
				.create(DescriptorMatcher.FLANNBASED);

		MatOfDMatch matOfDMatch = new MatOfDMatch();
		descriptorMatcher.match(pictureReferenceDescriptorMat,
				studentPictureDescriptorMat, matOfDMatch);

		double distance;
		double maxDistance = 0.0;
		double minDistance = 100.0;
		List<DMatch> dMatchList = matOfDMatch.toList();
		for (int row = 0; row < pictureReferenceDescriptorMat.rows(); row++) {
			distance = dMatchList.get(row).distance;

			if (distance < minDistance) {
				minDistance = distance;
			}

			if (distance > maxDistance) {
				maxDistance = distance;
			}
		}

		List<DMatch> goodDMatchList = new ArrayList<DMatch>();
		for (int row = 0; row < pictureReferenceDescriptorMat.rows(); row++) {
			if (dMatchList.get(row).distance < 3 * (minDistance + 0.01)) {
				goodDMatchList.add(dMatchList.get(row));
			}
		}

		// 363 - Auto_grader.java
		MatOfDMatch matOfDMatches = new MatOfDMatch(
				goodDMatchList.toArray(new DMatch[goodDMatchList.size()]));

		return (null);
	}
}