#include "../header/ImageProcess.hpp"

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

Mat drawKeyPointsIntoImage(Mat image, vector<KeyPoint> keyPoints,
		Scalar keyPointsColor) {
	Mat imageOutput;

	drawKeypoints(image, keyPoints, imageOutput, keyPointsColor,
			DrawMatchesFlags::DRAW_RICH_KEYPOINTS);

	return (imageOutput);
}

Mat drawMatchesKeyPointsIntoImages(Mat image_refer,
		vector<KeyPoint> keyPoints_ref, Mat image_solu,
		vector<KeyPoint> keyPoints_solu, vector<DMatch> good_matches,
		Scalar matchColor, Scalar singlePointColor) {
	Mat matching_keyPoints_image;

	drawMatches(image_refer, keyPoints_ref, image_solu, keyPoints_solu,
			good_matches, matching_keyPoints_image, singlePointColor,
			matchColor, vector<char> (),
			DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	return (matching_keyPoints_image);
}

void showImageWindow(const char *windowTitleId, Mat image, bool waitForKey) {
	namedWindow(windowTitleId, CV_WINDOW_NORMAL);
	imshow(windowTitleId, image);

	if (waitForKey) {
		waitKey();
	}
}
