package co.edu.udea.omrgrader.presentation;

import co.edu.udea.omrgrader.model.imageprocesor.ImageProcesor;

public class Main {
	private static String referenceImage = "C:\\Users\\Andersson\\Desktop\\Temporales\\ImageOpenCV\\only_logos_template.png";
	private static String studentImage = "C:\\Users\\Andersson\\Desktop\\Temporales\\ImageOpenCV\\Owner_Solved_Exam-Photo.jpg";

	public Main() {
		super();
	}

	public static void main(String[] args) {

		ImageProcesor.getInstance().executeProcessing(referenceImage,
				studentImage);
	}

}
