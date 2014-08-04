#include "../header/ANNInformation.hpp"

using namespace std;
using namespace cv;

void scaleDownImage(Mat &originalImg, Mat &scaledDownImage) {
	for (int x = 0; x < 16; x++) {
		for (int y = 0; y < 16; y++) {
			int ydim = ceil((float) (y * originalImg.cols / 16));
			int xdim = ceil((float) (x * originalImg.rows / 16));
			scaledDownImage.at<uchar> (x, y) = originalImg.at<uchar> (xdim,
					ydim);
		}
	}
}

void cropImage(Mat &originalImage, Mat &croppedImage) {
	int row = originalImage.rows;
	int col = originalImage.cols;
	// t = top, r = right, b = bottom, l = left
	int tlx = 0;
	int tly = 0;
	int bry = 0;
	int brx = 0;
	int flag = 0;

	// Top Edge
	for (int x = 1; x < row; x++) {
		for (int y = 0; y < col; y++) {
			if (originalImage.at<uchar> (x, y) == 0) {
				flag = 1;
				tly = x;
				break;
			}
		}

		if (flag == 1) {
			flag = 0;
			break;
		}
	}

	// Bottom Edge
	for (int x = row - 1; x > 0; x--) {
		for (int y = 0; y < col; y++) {
			if (originalImage.at<uchar> (x, y) == 0) {
				flag = 1;
				bry = x;
				break;
			}
		}

		if (flag == 1) {
			flag = 0;
			break;
		}
	}

	// Left Edge
	for (int y = 0; y < col; y++) {
		for (int x = 0; x < row; x++) {
			if (originalImage.at<uchar> (x, y) == 0) {
				flag = 1;
				tlx = y;
				break;
			}
		}

		if (flag == 1) {
			flag = 0;
			break;
		}
	}

	// Right Edge
	for (int y = col - 1; y > 0; y--) {
		for (int x = 0; x < row; x++) {
			if (originalImage.at<uchar> (x, y) == 0) {
				flag = 1;
				brx = y;
				break;
			}
		}

		if (flag == 1) {
			flag = 0;
			break;
		}
	}

	int width = brx - tlx;
	int height = bry - tly;

	Mat crop(originalImage, cv::Rect(tlx, tly, width, height));
	croppedImage = crop.clone();
}

void convertToPixelValueArray(Mat &img, int pixelarray[]) {
	int i = 0;
	for (int x = 0; x < ROWCOLUMN; x++) {
		for (int y = 0; y < ROWCOLUMN; y++) {
			pixelarray[i] = (img.at<uchar> (x, y) == 255) ? 1 : 0;
			i++;
		}
	}
}

string convertIntToString(int number) {
	stringstream ss;
	ss << number;
	return ss.str();
}

void readFile(string datasetPath, int samplesPerClass, char *outputfile,
		int num) {

	FILE *file;
	file = fopen(outputfile, "w");
	for (int sample = 1; sample <= samplesPerClass; sample++) {
		for (int digit = 0; digit < 10; digit++) {
			// Creating the file path string
			string imagePath = datasetPath + "/" + convertIntToString(digit)
					+ "/img(" + convertIntToString(sample) + ").png";

			// Reading the image
			Mat img = cv::imread(imagePath, 0);

			Mat output;
			// Applying Gaussian Blur to remove any noise
			GaussianBlur(img, output, cv::Size(5, 5), 0);
			// Thresholding to get a binary image
			threshold(output, output, 50, 255, 0);

			// Declaring mat to hold the scaled down image
			Mat scaledDownImage(ROWCOLUMN, ROWCOLUMN, CV_8U, Scalar(0));
			// Declaring array to hold the pixel values in the memory before it written into file
			int pixelValueArray[ATTRIBUTES];

			// Cropping the image.
			if (num == 1) {
				cropImage(output, output);
			}

			// Reducing the image dimension to 16X16.
			scaleDownImage(output, scaledDownImage);

			// Reading the pixel values.
			convertToPixelValueArray(scaledDownImage, pixelValueArray);
			// Writing pixel data to file
			for (int d = 0; d < ATTRIBUTES; d++) {
				fprintf(file, "%d,", pixelValueArray[d]);
			}

			// Writing the label to file
			fprintf(file, "%d\n", digit);
			fflush(file);
		}
	}

	// Closing File
	fclose(file);
}
