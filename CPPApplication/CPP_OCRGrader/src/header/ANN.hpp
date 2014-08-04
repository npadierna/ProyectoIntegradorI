#ifndef ANN_HPP_
#define ANN_HPP_

#include <opencv2/opencv.hpp>

void readDataset(char *fileName, cv::Mat &data, cv::Mat &classes,
		int totalSamples);

CvANN_MLP trainANN();

void testANN();

#endif
