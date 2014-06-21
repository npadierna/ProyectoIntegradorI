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
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.utils.Converters;

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

	public void executeProcessing(String referenceImage, String studentImage) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat referenceMat = Highgui.imread(referenceImage,
				Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat studentMat = Highgui.imread(studentImage,
				Highgui.CV_LOAD_IMAGE_GRAYSCALE);

		/* 1 Detectando los puntos */
		FeatureDetector featureDetector = FeatureDetector
				.create(FeatureDetector.SURF);

		MatOfKeyPoint referenceOfKeyPoint = new MatOfKeyPoint();
		MatOfKeyPoint studentOfKeyPoint = new MatOfKeyPoint();

		featureDetector.detect(referenceMat, referenceOfKeyPoint);
		featureDetector.detect(studentMat, studentOfKeyPoint);

		/* 2 Creando los descriptores */
		DescriptorExtractor descriptorExtractor = DescriptorExtractor
				.create(DescriptorExtractor.SURF);

		Mat referenceDescriptorMat = new Mat();
		Mat studentDescriptorMat = new Mat();

		descriptorExtractor.compute(referenceMat, referenceOfKeyPoint,
				referenceDescriptorMat);
		descriptorExtractor.compute(studentMat, studentOfKeyPoint,
				studentDescriptorMat);

		/* 3 Haciendo el Maching */
		DescriptorMatcher descriptorMatcher = DescriptorMatcher
				.create(DescriptorMatcher.FLANNBASED);

		MatOfDMatch matOfDMatch = new MatOfDMatch();

		descriptorMatcher.match(referenceDescriptorMat, studentDescriptorMat,
				matOfDMatch);

		double distance;
		double maxDistance = 0.0;
		double minDistance = 100.0;
		List<DMatch> dMatchList = matOfDMatch.toList();
		for (int row = 0; row < referenceDescriptorMat.rows(); row++) {
			distance = dMatchList.get(row).distance;

			if (distance < minDistance) {
				minDistance = distance;
			}

			if (distance > maxDistance) {
				maxDistance = distance;
			}

		}

		List<DMatch> goodDMatchList = new ArrayList<DMatch>();
		for (int row = 0; row < referenceDescriptorMat.rows(); row++) {
			if (dMatchList.get(row).distance < 2.0 * (minDistance + 0.001)) {
				goodDMatchList.add(dMatchList.get(row));
			}
		}

		MatOfDMatch matOfDMatches = new MatOfDMatch(
				goodDMatchList.toArray(new DMatch[goodDMatchList.size()]));
		Mat img_matches = new Mat();

		Features2d.drawMatches(referenceMat, referenceOfKeyPoint, studentMat,
				studentOfKeyPoint, matOfDMatches, img_matches,
				Scalar.all(-1.0), Scalar.all(-1.0), new MatOfByte(),
				Features2d.NOT_DRAW_SINGLE_POINTS);

		List<Point> refer = new ArrayList<Point>();
		List<KeyPoint> keyPointsList_ref = referenceOfKeyPoint.toList();

		List<Point> solu = new ArrayList<Point>();
		List<KeyPoint> keyPointList_solu = studentOfKeyPoint.toList();

		for (int i = 0; i < goodDMatchList.size(); i++) {
			refer.add(keyPointsList_ref.get(goodDMatchList.get(i).queryIdx).pt);
			solu.add(keyPointList_solu.get(goodDMatchList.get(i).trainIdx).pt);
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
		corners_template.add(new Point(referenceMat.cols(), 0.0));
		corners_template
				.add(new Point(referenceMat.cols(), referenceMat.rows()));
		corners_template.add(new Point(0.0, referenceMat.rows()));

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

		/* Guardar las imagenes */
		File file = new File(
				"C:\\Users\\Andersson\\Desktop\\Temporales\\ImageOpenCV\\Output\\OMRJavaImage.png");
		Highgui.imwrite(file.toString(), img_matches);

	}

}
