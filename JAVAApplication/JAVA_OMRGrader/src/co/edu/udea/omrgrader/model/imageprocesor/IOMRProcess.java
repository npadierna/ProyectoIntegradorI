package co.edu.udea.omrgrader.model.imageprocesor;

public interface IOMRProcess {

	public void executeProcessing(String refer_path, String solu_path,
			String processedImageDirectory, String blackWhiteImageDirectory);
}
