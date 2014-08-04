#ifndef ANNINFORMATION_HPP_
#define ANNINFORMATION_HPP_

#include <opencv2/opencv.hpp>
#include <string.h>
#include <stdio.h>

#define ATTRIBUTES 256  // Number of pixels per sample. 16X16
#define CLASSES 10		// Number of distinct labels (number of classes).
#define ROWCOLUMN 16	// Number of rows or columns per number.
using namespace cv;
using namespace std;

void scaleDownImage(cv::Mat &originalImg, cv::Mat &scaledDownImage);

void cropImage(cv::Mat &originalImage, cv::Mat &croppedImage);

void convertToPixelValueArray(cv::Mat &img, int pixelarray[]);

string convertIntToString(int number);

void readFile(std::string datasetPath, int samplesPerClass, char *outputFile,
		int num);

#endif
