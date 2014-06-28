package co.edu.udea.omrgrader.presentation;

import co.edu.udea.omrgrader.model.imageprocesor.ImageProcesor;

public class Main {
	private static String refer_path = "C:\\Users\\lis\\Desktop\\Miguel\\ImagesOMR\\only_logos_template.png";
	private static String solu_path = "C:\\Users\\lis\\Desktop\\Miguel\\ImagesOMR\\real.jpg";
	private static String processedImageDirectory = "C:\\Users\\lis\\Desktop\\Miguel\\ImagesOMR\\";
	private static String blackWhiteImageDirectory = "C:\\Users\\lis\\Desktop\\Miguel\\ImagesOMR\\";
	
	public Main() {
		super();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageProcesor.getInstance()
				.executeProcessing(refer_path, solu_path,
						processedImageDirectory, blackWhiteImageDirectory);
	}

}
