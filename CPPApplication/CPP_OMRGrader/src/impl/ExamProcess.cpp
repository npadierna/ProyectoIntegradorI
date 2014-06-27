#include "../header/ExamProcess.hpp"

vector<Point2f> buildBubblesCenterLocations() {
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

	return (center_locations);
}

Mat drawTransferredBubbles(Mat image,
		vector<Point2f> center_locations_transfered, Scalar bubbleColor,
		int innerCircleRadius, int outerCircleRadius) {
	for (unsigned int i = 0; i < center_locations_transfered.size(); i++) {
		circle(image, center_locations_transfered[i], innerCircleRadius,
				bubbleColor, -1);
		circle(image, center_locations_transfered[i], outerCircleRadius,
				bubbleColor);
	}

	return (image);
}

Mat drawTransferredSquare(Mat image_refer, vector<KeyPoint> keyPoints_ref,
		Mat image_solu, vector<KeyPoint> keyPoints_solu,
		vector<DMatch> good_matches, Scalar matchColor,
		Scalar singlePointColor, vector<Point2f> corners_solu) {
	Mat mat = Mat();
	drawMatches(image_refer, keyPoints_ref, image_solu, keyPoints_solu,
			good_matches, mat, matchColor, singlePointColor, vector<char> (),
			DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

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

	return (image_solu_clone);
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

int getWhitePixelsInBlob(Mat imgToProcess, Point2f pt, int radius) {
	int centerX = (int) pt.x;
	int centerY = (int) pt.y;
	int cntOfWhite = 0;

	for (int i = (centerX - radius); i < (centerX + radius); i++) {
		for (int j = (centerY - radius); j < (centerY + radius); j++) {
			if ((j < imgToProcess.rows) && (i < imgToProcess.cols)) {
				uchar val = imgToProcess.at<uchar> (j, i);

				if (val == 255) {
					cntOfWhite++;
				}
			}
		}
	}

	return (cntOfWhite);
}
