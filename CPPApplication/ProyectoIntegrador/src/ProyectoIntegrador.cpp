#include <stdio.h>
#include <stdlib.h>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/nonfree/features2d.hpp>
#include <opencv2/opencv.hpp>

using namespace cv;

const char *ONLY_LOGOS_IMAGE_VIEWER_NAME = "Only Logos Template";
const char *REFERRING_EXAM_IMAGE_VIEWER_NAME = "Referring Exam";
const char *MATCHING_KEY_POINTS_VIEWER_NAME = "Matching Key Points";
const char *BLACK_AND_WHITE_IMAGE_VIEWER_NAME = "Black And White Image";

const int TOTAL_QUESTIONS_ITEMS = 30;
const int BUBBLES_CENTER_Y_COORDS[] = { 294, 333, 372, 411, 450, 489, 528, 567,
		606, 645, 684, 723, 762, 801, 840 };
const int BUBBLES_CENTER_X_COORDS[] =
		{ 168, 210, 252, 294, 521, 563, 605, 647 };

struct list_item_struct {

	short questionNum;
	bool choiceA;
	bool choiceB;
	bool choiceC;
	bool choiceD;
};

Mat convertImageToBlackAndWhite(Mat, bool);

void getAnswers(Mat, vector<Point2f> , struct list_item_struct *,
		int, int);

int getWhitePixelsInBlob(Mat, Point2f, int);

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
	namedWindow(ONLY_LOGOS_IMAGE_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(ONLY_LOGOS_IMAGE_VIEWER_NAME, image_refer);
	waitKey(0);

	namedWindow(REFERRING_EXAM_IMAGE_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(REFERRING_EXAM_IMAGE_VIEWER_NAME, image_solu);
	waitKey(0);
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
	Mat image_refer_output;
	Mat image_solu_output;
	Scalar keyPointsColor = Scalar(0.0, 255.0, 0.0);

	drawKeypoints(image_refer, keyPoints_ref, image_refer_output,
			keyPointsColor, DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	drawKeypoints(image_solu, keyPoints_solu, image_solu_output,
			keyPointsColor, DrawMatchesFlags::DRAW_RICH_KEYPOINTS);

	namedWindow(ONLY_LOGOS_IMAGE_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(ONLY_LOGOS_IMAGE_VIEWER_NAME, image_refer_output);
	waitKey(0);

	namedWindow(REFERRING_EXAM_IMAGE_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(REFERRING_EXAM_IMAGE_VIEWER_NAME, image_solu_output);
	waitKey(0);

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

	vector<DMatch> matOfDMatch1;
	vector<vector<DMatch> > matOfDMatch2;

	matcher.match(descriptors_ref, descriptors_solu, matOfDMatch1);
	matcher.knnMatch(descriptors_ref, descriptors_solu, matOfDMatch2, 2);

	double dist;
	double max_dist = 0.0;
	double min_dist = 99999.9;
	for (int i = 0; i < descriptors_ref.rows; i++) {
		dist = matOfDMatch1[i].distance;

		if (dist < min_dist) {
			min_dist = dist;
		}

		if (dist > max_dist) {
			max_dist = dist;
		}
	}

	vector<DMatch> good_matches1;
	for (int i = 0; i < descriptors_ref.rows; i++) {
		// Criteria
		if (matOfDMatch1[i].distance < (3.0F * min_dist + 0.01F)) {
			good_matches1.push_back(matOfDMatch1[i]);
		}
	}

	vector<DMatch> good_matches2;
	good_matches2.reserve(matOfDMatch2.size());
	for (size_t i = 0; i < matOfDMatch2.size(); ++i) {
		if (matOfDMatch2[i].size() < 2) {
			continue;
		}

		const DMatch &dMatch1 = matOfDMatch2[i][0];
		const DMatch &dMatch2 = matOfDMatch2[i][1];

		if (dMatch1.distance <= (3.0F * dMatch2.distance + 0.01F)) {
			good_matches2.push_back(dMatch1);
		}
	}

	vector<Point2f> refer1;
	vector<Point2f> solu1;
	for (unsigned int i = 0; i < good_matches1.size(); i++) {
		refer1.push_back(keyPoints_ref[good_matches1[i].queryIdx].pt);
		solu1.push_back(keyPoints_solu[good_matches1[i].trainIdx].pt);
	}

	vector<Point2f> refer2;
	vector<Point2f> solu2;
	for (unsigned int i = 0; i < good_matches2.size(); i++) {
		refer2.push_back(keyPoints_ref[good_matches2[i].queryIdx].pt);
		solu2.push_back(keyPoints_solu[good_matches2[i].trainIdx].pt);
	}

	Mat H1 = findHomography(refer1, solu1, CV_RANSAC);
	Mat H2 = findHomography(refer2, solu2, CV_RANSAC);

	vector<Point2f> corners_template1(4);
	corners_template1[0] = cvPoint(0, 0);
	corners_template1[1] = cvPoint(image_refer.cols, 0);
	corners_template1[2] = cvPoint(image_refer.cols, image_refer.rows);
	corners_template1[3] = cvPoint(0, image_refer.rows);

	vector<Point2f> corners_template2(4);
	corners_template2[0] = cvPoint(0, 0);
	corners_template2[1] = cvPoint(image_refer.cols, 0);
	corners_template2[2] = cvPoint(image_refer.cols, image_refer.rows);
	corners_template2[3] = cvPoint(0, image_refer.rows);

	vector<Point2f> corners_solu1(4);
	perspectiveTransform(corners_template1, corners_solu1, H1);

	vector<Point2f> corners_solu2(4);
	perspectiveTransform(corners_template2, corners_solu2, H2);

	///// ----- ///// ----- ///// ----- /////
	Mat img_matches1 = Mat();
	drawMatches(image_refer, keyPoints_ref, image_solu, keyPoints_solu,
			good_matches1, img_matches1, Scalar::all(-1.0), Scalar::all(-1.0),
			vector<char> (), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	Mat image_solu_clone = image_solu.clone();
	line(image_solu_clone, corners_solu1[0], corners_solu1[1],
			Scalar(0.0, 255.0, 0.0), 4);
	line(image_solu_clone, corners_solu1[1], corners_solu1[2],
			Scalar(0.0, 255.0, 0.0), 4);
	line(image_solu_clone, corners_solu1[2], corners_solu1[3],
			Scalar(0.0, 255.0, 0.0), 4);
	line(image_solu_clone, corners_solu1[3], corners_solu1[0],
			Scalar(0.0, 255.0, 0.0), 4);

	vector<Point2f> center_locations1;
	for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[0],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[1],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[2],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[3],
						BUBBLES_CENTER_Y_COORDS[row]));
	}

	for (int row = 0; row < (TOTAL_QUESTIONS_ITEMS / 2); row++) {
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[4],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[5],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[6],
						BUBBLES_CENTER_Y_COORDS[row]));
		center_locations1.push_back(
				Point2f(BUBBLES_CENTER_X_COORDS[7],
						BUBBLES_CENTER_Y_COORDS[row]));
	}

	vector<Point2f> center_locations_transfered(center_locations1.size());
	perspectiveTransform(center_locations1, center_locations_transfered, H1);

	for (unsigned int i = 0; i < center_locations_transfered.size(); i++) {
		circle(image_solu_clone, center_locations_transfered[i], 2,
				Scalar(255.0, 0.0, 0.0), -1);
		circle(image_solu_clone, center_locations_transfered[i], 11,
				Scalar(255.0, 0.0, 0.0));
	}

	namedWindow(MATCHING_KEY_POINTS_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(MATCHING_KEY_POINTS_VIEWER_NAME, image_solu_clone);
	waitKey(0);

	imwrite("./tmp/Owner_Solved_Exam-Processed_Matching_Logos[1a].jpg",
			image_solu);
	///// ----- ///// ----- ///// ----- /////

	///// ----- ///// ----- ///// ----- /////
	Mat img_matches2;
	drawMatches(image_refer, keyPoints_ref, image_solu, keyPoints_solu,
			good_matches2, img_matches2, Scalar::all(-1.0), Scalar::all(-1.0),
			vector<char> (), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	line(img_matches2, corners_solu2[0], corners_solu2[1],
			Scalar(0.0, 255.0, 0.0), 4);
	line(img_matches2, corners_solu2[1], corners_solu2[2],
			Scalar(0.0, 255.0, 0.0), 4);
	line(img_matches2, corners_solu2[2], corners_solu2[3],
			Scalar(0.0, 255.0, 0.0), 4);
	line(img_matches2, corners_solu2[3], corners_solu2[0],
			Scalar(0.0, 255.0, 0.0), 4);

	namedWindow("Temp 1", CV_WINDOW_NORMAL);
	imshow("Temp 1", img_matches2);
	waitKey(0);

	imwrite("./tmp/Owner_Solved_Exam-Processed_Matching_Logos[1b].jpg",
			img_matches2);
	///// ----- ///// ----- ///// ----- /////

	Mat image_solu_bw = convertImageToBlackAndWhite(image_solu_clone, false);

	///// ----- ///// ----- ///// ----- /////
	namedWindow(BLACK_AND_WHITE_IMAGE_VIEWER_NAME, CV_WINDOW_NORMAL);
	imshow(BLACK_AND_WHITE_IMAGE_VIEWER_NAME, image_solu_bw);
	waitKey(0);

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

void getAnswers(Mat imgToProcess, vector<Point2f> pList,
		struct list_item_struct *tempAnswerData, int radius, int thresh) {
	for (int i = 0; i < TOTAL_QUESTIONS_ITEMS; i++) {
		int pixelCnt[] = { 0, 0, 0, 0 };

		for (int j = 0; j < 4; j++) {
			int position = i * 4 + j;
			Point2f pt = pList[position];

			pixelCnt[j] = getWhitePixelsInBlob(imgToProcess, pt, radius);
		}

		tempAnswerData->questionNum = (short) (i + 1);
		tempAnswerData->choiceA = (pixelCnt[0] > thresh);
		tempAnswerData->choiceB = (pixelCnt[1] > thresh);
		tempAnswerData->choiceC = (pixelCnt[2] > thresh);
		tempAnswerData->choiceD = (pixelCnt[3] > thresh);
		tempAnswerData++;
	}
}

Mat convertImageToBlackAndWhite(Mat img, bool applyGaussBlurr) {
	Mat img_gray = img.clone();

	if (applyGaussBlurr) {
		GaussianBlur(img_gray, img_gray, Size(3, 3), 0.0, 0.0);
	}

	double thresh = threshold(img_gray, img_gray, 0.0, 255.0,
			THRESH_BINARY | THRESH_OTSU);
	threshold(img_gray, img_gray, thresh, 255.0, THRESH_BINARY_INV);

	return (img_gray);
}

int getWhitePixelsInBlob(Mat imgToProcess, Point2f pt, int radius) {
	int centerX = (int) pt.x;
	int centerY = (int) pt.y;
	int cntOfWhite = 0;

	for (int i = (centerX - radius); i < (centerX + radius); i++) {
		for (int j = (centerY - radius); j < (centerY + radius); j++) {
			if ((j < imgToProcess.rows) && (i < imgToProcess.cols)) {
				uchar val = imgToProcess.at<uchar>(j, i);

				if (val == 255) {
					cntOfWhite++;
				}
			}
		}
	}

	return (cntOfWhite);
}
