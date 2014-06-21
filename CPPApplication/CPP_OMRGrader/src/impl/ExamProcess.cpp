#include "../header/ExamProcess.hpp"

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
