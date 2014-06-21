#ifndef EXAMPPROCESS_HPP_
#define EXAMPPROCESS_HPP_

#include <opencv2/opencv.hpp>

using namespace cv;

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

void getAnswers(Mat, vector<Point2f> , struct list_item_struct *,
		int, int);

int getWhitePixelsInBlob(Mat, Point2f, int);

#endif /* EXAMPPROCESS_HPP_ */
