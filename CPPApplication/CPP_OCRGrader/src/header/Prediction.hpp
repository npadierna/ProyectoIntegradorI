#ifndef PREDICTION_HPP_
#define PREDICTION_HPP_

#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

string predictDigits(Mat &originalImage);

void processImage(string imagePath);

#endif
