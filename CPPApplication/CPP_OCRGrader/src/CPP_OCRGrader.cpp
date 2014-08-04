#include "header/ANN.hpp"
#include "header/ANNInformation.hpp"
#include "header/Prediction.hpp"

int main() {
//	cout << "Reading the training set......\n";
//	readFile(
//			"/home/andersson/Escritorio/Temporales/images/numbers/trainingset",
//			1004, "/home/andersson/Escritorio/Temporales/training_set.txt", 1);
//
//	cout << "\nReading the training set......\n";
//	readFile(
//			"/home/andersson/Escritorio/Temporales/images/numbers/trainingset",
//			100, "/home/andersson/Escritorio/Temporales/test_set.txt", 1);
//	cout << "\nReading operation completed.\n\n";
//
//	cout << "Training network.\n";
//	trainANN();
//	cout << "\nTraining operation completed.";
//
//	cout << "Testing ANN";
//	testANN();

	string imagePath =
			"/home/andersson/Escritorio/Temporales/images-numbers/num6.PNG";
	processImage(imagePath);
}
