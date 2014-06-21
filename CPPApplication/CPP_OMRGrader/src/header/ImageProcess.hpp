#ifndef IMAGEPROCESS_HPP_
#define IMAGEPROCESS_HPP_

#include <opencv2/opencv.hpp>

using namespace cv;

Mat convertImageToBlackAndWhite(Mat, bool);

Mat drawKeyPointsIntoImage(Mat, vector<KeyPoint> , Scalar);

Mat drawMatchesKeyPointsIntoImages(Mat, vector<KeyPoint> , Mat,
		vector<KeyPoint> , vector<DMatch> , Scalar, Scalar);

void showImageWindow(const char *, Mat, bool);

#endif /* IMAGEPROCESS_HPP_ */
