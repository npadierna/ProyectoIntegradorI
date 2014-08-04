#include "../header/Prediction.hpp"
#include "../header/ANNInformation.hpp"

#include "opencv2/opencv.hpp"    // OpenCV general include file
#include "opencv2/ml/ml.hpp"     // OpenCV Machine Learning include file
#include <stdio.h>

string predictDigits(Mat &originalImage) {
	string numbers = "";
	Mat clon = originalImage.clone();

	// Read the model from the XML file and create the neural network.
	CvANN_MLP nnetwork;
	CvFileStorage* storage = cvOpenFileStorage(
			"/home/andersson/Escritorio/Temporales/neural_network.xml", 0,
			CV_STORAGE_READ);
	CvFileNode *n = cvGetFileNodeByName(storage, 0, "DigitOCR");
	nnetwork.read(storage, n);
	cvReleaseFileStorage(&storage);

	int rows = originalImage.rows;
	int cols = originalImage.cols;

	int lx = 0;
	int ty = 0;
	int by = 0;
	int rx = 0;
	int flag = 0;
	int currentColumn = 1;
	bool temp = false;

	while (!temp) {
		/* Left X */
		for (int i = currentColumn; i < cols; i++) {
			for (int j = 1; j < rows; j++) {
				if (i != (cols - 1)) {
					if (originalImage.at<uchar> (j, i) == 0) {
						lx = i;
						flag = 1;
						break;
					}
				} else {
					temp = true;
					break;
				}
			}

			if (!temp) {
				if (flag == 1) {
					flag = 0;
					break;
				}
			} else {
				break;
			}
		}

		if (temp) {
			continue;
		}

		/* Right X */
		int tempNum;
		for (int i = lx; i < cols; i++) {
			tempNum = 0;
			for (int j = 1; j < rows; j++) {
				if (originalImage.at<uchar> (j, i) == 0) {
					tempNum += 1;
				}
			}

			if (tempNum == 0) {
				rx = (i - 1);
				break;
			}
		}

		currentColumn = rx + 1;

		/* Top Y */
		for (int i = 1; i < rows; i++) {
			for (int j = lx; j <= rx; j++) {
				if (originalImage.at<uchar> (i, j) == 0) {
					ty = i;
					flag = 1;
					break;
				}
			}

			if (flag == 1) {
				flag = 0;
				break;
			}
		}

		/* Bottom Y */
		for (int i = (rows - 1); i >= 1; i--) {
			for (int j = lx; j <= rx; j++) {
				if (originalImage.at<uchar> (i, j) == 0) {
					by = i;
					flag = 1;
					break;
				}
			}

			if (flag == 1) {
				flag = 0;
				break;
			}
		}

		int width = rx - lx;
		int height = by - ty;

		// Cropping image
		Mat crop(originalImage, Rect(lx, ty, width, height));

		// Cloning image
		Mat splittedImage;
		splittedImage = crop.clone();

		//		imwrite("/home/andersson/Escritorio/Temporales/splitted.png",
		//				splittedImage);

		// Processing image
		Mat output;
		cv::GaussianBlur(splittedImage, output, cv::Size(5, 5), 0);
		cv::threshold(output, output, 50, ATTRIBUTES - 1, 0);
		cv::Mat scaledDownImage(ROWCOLUMN, ROWCOLUMN, CV_8U, cv::Scalar(0));
		scaleDownImage(output, scaledDownImage);

		int pixelValueArray[ATTRIBUTES];
		cv::Mat testSet(1, ATTRIBUTES, CV_32F);
		// Mat to Pixel Value Array
		convertToPixelValueArray(scaledDownImage, pixelValueArray);

		// Pixel Value Array to Mat CV_32F
		cv::Mat classificationResult(1, CLASSES, CV_32F);
		for (int i = 0; i <= ATTRIBUTES; i++) {
			testSet.at<float> (0, i) = pixelValueArray[i];
		}

		// Predicting the number
		nnetwork.predict(testSet, classificationResult);

		// Selecting the correct response
		int maxIndex = 0;
		float value = 0.0f;
		float maxValue = classificationResult.at<float> (0, 0);
		for (int index = 1; index < CLASSES; index++) {
			value = classificationResult.at<float> (0, index);
			if (value > maxValue) {
				maxValue = value;
				maxIndex = index;
			}
		}

		printf("Class result: %d\n", maxIndex);
		numbers = numbers + convertIntToString(maxIndex);

		Scalar colorRect = Scalar(0.0, 0.0, 255.0);
		rectangle(clon, Point(lx, ty), Point(rx, by), colorRect, 1, 8, 0);
		namedWindow("Clon", CV_WINDOW_NORMAL);
		imshow("Clon", clon);
		waitKey(0);

		namedWindow("Test", CV_WINDOW_NORMAL);
		imshow("Test", splittedImage);
		waitKey(0);
	}

	imwrite("/home/andersson/Escritorio/Temporales/clon.png", clon);

	return numbers;
}

void processImage(string imagePath) {
	Mat image = imread(imagePath, CV_LOAD_IMAGE_GRAYSCALE);

	Mat output;
	output = imread(imagePath, CV_LOAD_IMAGE_GRAYSCALE);

	namedWindow("Test", CV_WINDOW_NORMAL);
	imshow("Test", image);
	waitKey(0);

	// Applying Gaussian Blur to remove any noise
	GaussianBlur(image, output, Size(3, 3), 0.0, 0.0);

	namedWindow("Test", CV_WINDOW_NORMAL);
	imshow("Test", output);
	waitKey(0);

	// Thresholding to get a binary image
	double thresh = threshold(output, output, 0.0, 255.0,
			THRESH_BINARY | THRESH_OTSU);
	threshold(output, output, thresh, 255.0, THRESH_BINARY);

	imwrite("/home/andersson/Escritorio/Temporales/processed.png", output);

	namedWindow("Test", CV_WINDOW_NORMAL);
	imshow("Test", output);
	waitKey(0);

	string numbers = predictDigits(output);
	printf("\nThe id number is: %s\n", numbers.data());
}
