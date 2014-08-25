#ifndef EXAMPPROCESS_HPP_
#define EXAMPPROCESS_HPP_

#include <opencv2/opencv.hpp>

using namespace cv;

const int BUBBLES_CENTER_Y_COORDS[] = { 340, 367, 394, 421, 448, 475, 502, 529,
		556, 583, 610, 637, 664, 691, 718, 745, 772, 799, 826, 853 };

const int BUBBLES_CENTER_X_COORDS[] = { 116, 145, 174, 203, 232, 355, 384, 413,
		442, 471, 593, 622, 651, 680, 709 };

const int QUESTIONS_COLUMNS_AMOUT = 3;

const int QUESTIONS_ITEMS_AMOUT = 5;

const int TOTAL_QUESTIONS_ITEMS = 60;

struct list_item_struct {

	short questionNum;
	bool choiceA;
	bool choiceB;
	bool choiceC;
	bool choiceD;
	bool choiceE;
};

vector<Point2f> buildBubblesCenterLocations();

Mat drawTransferredBubbles(Mat, vector<Point2f> , Scalar, int, int);

Mat drawTransferredSquare(Mat, vector<KeyPoint> , Mat, vector<KeyPoint> ,
		vector<DMatch> , Scalar, Scalar, vector<Point2f> );

void getAnswers(Mat, vector<Point2f> , struct list_item_struct *, int, int);

int getWhitePixelsInBlob(Mat, Point2f, int);

#endif /* EXAMPPROCESS_HPP_ */
