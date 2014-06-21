#include <stdio.h>
#include <stdlib.h>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/nonfree/features2d.hpp>
#include <opencv2/opencv.hpp>
#include "header/ExamProcess.hpp"
#include "header/ImageProcess.hpp"

using namespace cv;

const char *BLACK_AND_WHITE_IMAGE_VIEWER_NAME = "Black And White Image";
const char *MATCHING_KEY_POINTS_VIEWER_NAME = "Matching Key Points";
const char *ONLY_LOGOS_IMAGE_VIEWER_NAME = "Only Logos Template";
const char *REFERRING_EXAM_IMAGE_VIEWER_NAME = "Referring Exam";
const char *TRANSFERRED_BUBBLES_IMAGE_VIEWER_NAME = "Transferred Bubbles Image";

Mat drawKeyPointsIntoImage(Mat, vector<KeyPoint> , Scalar);

void showImageWindow(const char *, Mat, bool);

int main(int argc, char *argv[]) {
	if (argc != 3) {
		fprintf(stderr, "\n%s\n", "The route of the images has not been setted");

		return (EXIT_FAILURE);
	}

	Mat image_refer = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
	Mat image_solu = imread(argv[2], CV_LOAD_IMAGE_GRAYSCALE);
	if ((image_refer.empty()) || (image_solu.empty())) {
		fprintf(stderr, "\n%s\n",
				"There is not image data into some of the images passed as parameter");

		return (EXIT_FAILURE);
	}

	///// ----- ///// ----- ///// ----- /////
	showImageWindow(ONLY_LOGOS_IMAGE_VIEWER_NAME, image_refer, true);
	showImageWindow(REFERRING_EXAM_IMAGE_VIEWER_NAME, image_solu, true);
	///// ----- ///// ----- ///// ----- /////

	/*
	 * 1. Detecting Key Points using some detector. (Best option: STAR)
	 * [Stanford University's implementation: SURF].
	 */
	SurfFeatureDetector surfDetector(1900, 4, 2, true, false); // 1500

	vector<KeyPoint> keyPoints_ref;
	vector<KeyPoint> keyPoints_solu;

	surfDetector.detect(image_refer, keyPoints_ref);
	surfDetector.detect(image_solu, keyPoints_solu);

	///// ----- ///// ----- ///// ----- /////
	Scalar keyPointsColor = Scalar(0.0, 255.0, 0.0);
	Mat image_refer_output = drawKeyPointsIntoImage(image_refer, keyPoints_ref,
			keyPointsColor);
	Mat image_solu_output = drawKeyPointsIntoImage(image_solu, keyPoints_solu,
			keyPointsColor);

	showImageWindow(ONLY_LOGOS_IMAGE_VIEWER_NAME, image_refer_output, true);
	showImageWindow(REFERRING_EXAM_IMAGE_VIEWER_NAME, image_solu_output, true);

	imwrite("./tmp/Owner_Only_Logos-Digitized-Processed_Key_Points[1].jpg",
			image_refer_output);
	imwrite("./tmp/Owner_Solved_Exam-Processed_Key_Points[1].jpg",
			image_solu_output);
	///// ----- ///// ----- ///// ----- /////

	/*
	 * 2. Calculating Descriptors. (Best option: BRISK) [Stanford
	 * University's implementation: SURF].
	 */
	SurfDescriptorExtractor surfExtractor(1900, 4, 2, true, false); // 1500

	Mat descriptors_ref;
	Mat descriptors_solu;

	surfExtractor.compute(image_refer, keyPoints_ref, descriptors_ref);
	surfExtractor.compute(image_solu, keyPoints_solu, descriptors_solu);

	/*
	 * 3. Matching Descriptor vectors using some matcher. (Best option:
	 * BRUTEFORCE_HAMMING Matcher) [Stanford University's implementation:
	 * FLANNBASED].
	 */
	FlannBasedMatcher matcher;

	vector<DMatch> matOfDMatch;

	matcher.match(descriptors_ref, descriptors_solu, matOfDMatch);

	double dist;
	double max_dist = 0.0;
	double min_dist = 99999.9;
	for (int i = 0; i < descriptors_ref.rows; i++) {
		dist = matOfDMatch[i].distance;

		if (dist < min_dist) {
			min_dist = dist;
		}

		if (dist > max_dist) {
			max_dist = dist;
		}
	}

	vector<DMatch> good_matches;
	for (int i = 0; i < descriptors_ref.rows; i++) {
		if (matOfDMatch[i].distance < (3.0F * min_dist + 0.01F)) {
			good_matches.push_back(matOfDMatch[i]);
		}
	}

	vector<Point2f> refer;
	vector<Point2f> solu;
	for (unsigned int i = 0; i < good_matches.size(); i++) {
		refer.push_back(keyPoints_ref[good_matches[i].queryIdx].pt);
		solu.push_back(keyPoints_solu[good_matches[i].trainIdx].pt);
	}

	///// ----- ///// ----- ///// ----- /////
	Mat matching_keyPoints_image = drawMatchesKeyPointsIntoImages(image_refer,
			keyPoints_ref, image_solu, keyPoints_solu, good_matches,
			Scalar::all(-1.0), Scalar::all(-1.0));

	showImageWindow(MATCHING_KEY_POINTS_VIEWER_NAME, matching_keyPoints_image,
			true);
	///// ----- ///// ----- ///// ----- /////


	Mat H = findHomography(refer, solu, CV_RANSAC);

	vector<Point2f> corners_template(4);
	corners_template[0] = cvPoint(0, 0);
	corners_template[1] = cvPoint(image_refer.cols, 0);
	corners_template[2] = cvPoint(image_refer.cols, image_refer.rows);
	corners_template[3] = cvPoint(0, image_refer.rows);

	vector<Point2f> corners_solu(4);
	perspectiveTransform(corners_template, corners_solu, H);

	///// ----- ///// ----- ///// ----- /////
	Mat img_matches = Mat();
	drawMatches(image_refer, keyPoints_ref, image_solu, keyPoints_solu,
			good_matches, img_matches, Scalar::all(-1.0), Scalar::all(-1.0),
			vector<char> (), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	// FIXME: This clone is because the original image mat can not be used.
	Mat image_solu_clone = image_solu.clone();
	Scalar squareColorScalar = Scalar(0.0, 255.0, 0.0, 0.0);
	line(image_solu_clone, corners_solu[0], corners_solu[1], squareColorScalar,
			4);
	line(image_solu_clone, corners_solu[1], corners_solu[2], squareColorScalar,
			4);
	line(image_solu_clone, corners_solu[2], corners_solu[3], squareColorScalar,
			4);
	line(image_solu_clone, corners_solu[3], corners_solu[0], squareColorScalar,
			4);

	vector<Point2f> center_locations;
	for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[0],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[1],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[2],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[3],
						BUBBLES_CENTER_Y_COORDS[row]));
	}

	for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[4],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[5],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[6],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[7],
						BUBBLES_CENTER_Y_COORDS[row]));
	}

	vector<Point2f> center_locations_transfered(center_locations.size());
	perspectiveTransform(center_locations, center_locations_transfered, H);

	for (unsigned int i = 0; i < center_locations_transfered.size(); i++) {
		circle(image_solu_clone, center_locations_transfered[i], 2,
				Scalar(255.0, 0.0, 0.0), -1);
		circle(image_solu_clone, center_locations_transfered[i], 11,
				Scalar(255.0, 0.0, 0.0));
	}

	showImageWindow(TRANSFERRED_BUBBLES_IMAGE_VIEWER_NAME, image_solu_clone,
			true);

	imwrite("./tmp/Owner_Solved_Exam-Processed_Matching_Logos[1a].jpg",
			image_solu);
	///// ----- ///// ----- ///// ----- /////

	Mat image_solu_bw = convertImageToBlackAndWhite(image_solu_clone, false);

	///// ----- ///// ----- ///// ----- /////
	showImageWindow(BLACK_AND_WHITE_IMAGE_VIEWER_NAME, image_solu_bw, true);

	imwrite("./tmp/Owner_Solved_Exam-Processed_BlackAndWhite[1].jpg",
			image_solu_bw);
	///// ----- ///// ----- ///// ----- /////

	struct list_item_struct *tempAnswerData =
			(struct list_item_struct*) malloc(
					sizeof(struct list_item_struct) * TOTAL_QUESTIONS_ITEMS);
	getAnswers(image_solu_bw, center_locations_transfered, tempAnswerData, 11,
			80);

	struct list_item_struct *temp = tempAnswerData;
	for (int pos = 0; pos < TOTAL_QUESTIONS_ITEMS; pos++) {
		fprintf(stdout, "Question #%d -> [%d %d %d %d]\n", temp->questionNum,
				temp->choiceA, temp->choiceB, temp->choiceC, temp->choiceD);
		temp++;
	}

	free(tempAnswerData);

	return (EXIT_SUCCESS);
}
