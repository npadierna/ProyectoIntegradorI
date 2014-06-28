package co.edu.udea.omrgrader.presentation;

import java.io.IOException;
import java.util.Properties;
import co.edu.udea.omrgrader.model.imageprocesor.impl.OMRProcess;

public class Main {

	public Main() {
		super();
	}

	public static void main(String[] args) {
		Properties constants = new Properties();
		try {
			constants.load(Main.class.getClassLoader().getResourceAsStream(
					"co/edu/udea/omrgrader/model/util/constants.properties"));
		} catch (IOException e) {
			System.out.println("Error al leer archivo de propiedades");
			e.printStackTrace();
		}

		if (!constants.isEmpty()) {
			OMRProcess.getInstance().executeProcessing(
					constants.getProperty("refer_path"),
					constants.getProperty("solu_path"),
					constants.getProperty("processedImageDirectory"),
					constants.getProperty("blackWhiteImageDirectory"));

		} else {
			System.out.println("Propiedades vacias");
		}
	}
}
