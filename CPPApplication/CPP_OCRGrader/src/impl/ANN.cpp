#include "../header/ANN.hpp"
#include "../header/ANNInformation.hpp"

#include "opencv2/opencv.hpp"    // OpenCV general include file
#include "opencv2/ml/ml.hpp"     // OpenCV Machine Learning include file
#include <stdio.h>
#include <fstream>

using namespace std;
using namespace cv;

#define TRAINING_SAMPLES 10040  // Number of samples in training dataset.
#define TEST_SAMPLES 1000		// Number of samples in test dataset.
void readDataset(char *fileName, Mat &data, Mat &classes, int totalSamples) {

	int label;
	float pixelvalue;
	FILE* inputfile = fopen(fileName, "r");

	// Reading each row of the binary file
	for (int row = 0; row < totalSamples; row++) {
		// For each attribute in the row
		for (int col = 0; col <= ATTRIBUTES; col++) {
			// Is it a pixel value?
			if (col < ATTRIBUTES) {
				fscanf(inputfile, "%f,", &pixelvalue);
				data.at<float> (row, col) = pixelvalue;
			}
			// Is it the label?
			else if (col == ATTRIBUTES) {
				// Make the value of label column in that row as 1.
				fscanf(inputfile, "%i", &label);
				classes.at<float> (row, label) = 1.0;
			}
		}
	}

	fclose(inputfile);
}

CvANN_MLP trainANN() {
	// Matrix to hold the training sample
	Mat trainingSet(TRAINING_SAMPLES, ATTRIBUTES, CV_32F);
	// Matrix to hold the labels of each training sample
	Mat trainingSetClassifications(TRAINING_SAMPLES, CLASSES, CV_32F);

	// Loading the training set
	readDataset("/home/andersson/Escritorio/Temporales/training_set.txt",
			trainingSet, trainingSetClassifications, TRAINING_SAMPLES);

	/* Defining the structure for the Neural Network (MLP)
	 The neural network has 3 layers.
	 - One input node per attribute in a sample, so 256 input nodes
	 - 16 hidden nodes
	 - 10 output node, one for each class.*/

	Mat layers(3, 1, CV_32S);
	layers.at<int> (0, 0) = ATTRIBUTES; // Input layer
	layers.at<int> (1, 0) = 16; // Hidden layer
	layers.at<int> (2, 0) = CLASSES; // Output layer

	// Creating the neural network
	CvANN_MLP nnetwork(layers, CvANN_MLP::SIGMOID_SYM, 0.6, 1);

	CvANN_MLP_TrainParams ANNParams(
	/* Finishing the training after either 1000 iterations or a very small
	 * change in the network weights below the specified value.*/
	cvTermCriteria(CV_TERMCRIT_ITER + CV_TERMCRIT_EPS, 1000, 0.000001),
	// Using Backpropogation for neural network training
			CvANN_MLP_TrainParams::BACKPROP,
			// Co-efficents for Backpropogation training
			0.1, 0.1);

	// Training the Artificial Neural Network using training data
	printf("\nUsing training dataset\n");
	int iterations = nnetwork.train(trainingSet, trainingSetClassifications,
			Mat(), Mat(), ANNParams);
	printf("Training iterations: %i\n\n", iterations);

	// Saving the model generated into an XML file.
	CvFileStorage* storage = cvOpenFileStorage(
			"/home/andersson/Escritorio/Temporales/neural_network.xml", 0,
			CV_STORAGE_WRITE);
	nnetwork.write(storage, "DigitOCR");
	cvReleaseFileStorage(&storage);

	return nnetwork;
}

void testANN() {
	// Matrix to hold the test samples
	Mat testSet(TEST_SAMPLES, ATTRIBUTES, CV_32F);
	// Matrix to hold the test labels
	Mat testSetClassifications(TEST_SAMPLES, CLASSES, CV_32F);
	// Matrix to hold the results
	Mat classificationResult(1, CLASSES, CV_32F);

	// Reading the model from the XML file and create the neural network
	CvANN_MLP nnetwork;
	CvFileStorage* storage = cvOpenFileStorage(
			"/home/andersson/Escritorio/Temporales/neural_network.xml", 0,
			CV_STORAGE_READ);
	CvFileNode *n = cvGetFileNodeByName(storage, 0, "DigitOCR");
	nnetwork.read(storage, n);
	cvReleaseFileStorage(&storage);
	readDataset("/home/andersson/Escritorio/Temporales/test_set.txt", testSet,
			testSetClassifications, TEST_SAMPLES);

	// Test the generated model with the test samples
	Mat testSample;
	// Amount correct classifications
	int correctClasses = 0;
	// Amount of wrong classifications
	int wrongClasses = 0;
	// Classification matrix gives the count of classes to which the samples were classified
	int classificationMatrix[CLASSES][CLASSES] = { { } };

	// For each sample in the test set
	for (int tsample = 0; tsample < TEST_SAMPLES; tsample++) {
		// Extracting the sample
		testSample = testSet.row(tsample);

		// Trying to predict its class
		nnetwork.predict(testSample, classificationResult);
		/*The classification result matrix holds weightage  of each class.
		 we take the class with the highest weightage as the resultant class */

		// Finding the class with maximum weightage.
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

		printf("Testing Sample %i -> class result (digit %d)\n", tsample,
				maxIndex);

		// Comparing the predicted class with the real class.
		// If the prediction is correct then testSetClassifications[tsample][maxIndex] should be 1.
		if (testSetClassifications.at<float> (tsample, maxIndex) != 1.0f) {
			// If they differ more than floating point error => wrong class
			wrongClasses++;

			// Finding the actual label 'classIndex'
			for (int classIndex = 0; classIndex < CLASSES; classIndex++) {
				if (testSetClassifications.at<float> (tsample, classIndex)
						== 1.0f) {

					// A classIndex sample was wrongly classified as maxIndex.
					classificationMatrix[classIndex][maxIndex]++;
					break;
				}
			}
		} else {
			// Otherwise correct
			correctClasses++;
			classificationMatrix[maxIndex][maxIndex]++;
		}
	}

	printf("\nResults on the Testing Dataset\n"
		"\tCorrect classifications: %d (%g%%)\n"
		"\tWrong classifications: %d (%g%%)\n", correctClasses,
			(double) correctClasses * 100 / TEST_SAMPLES, wrongClasses,
			(double) wrongClasses * 100 / TEST_SAMPLES);

	cout << "   ";
	for (int i = 0; i < CLASSES; i++) {
		cout << i << "\t";
	}

	cout << "\n";
	for (int row = 0; row < CLASSES; row++) {
		cout << row << "  ";
		for (int col = 0; col < CLASSES; col++) {
			cout << classificationMatrix[row][col] << "\t";
		}
		cout << "\n";
	}
}
