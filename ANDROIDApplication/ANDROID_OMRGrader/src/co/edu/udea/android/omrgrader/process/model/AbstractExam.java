package co.edu.udea.android.omrgrader.process.model;

import java.io.Serializable;
import java.util.List;

import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
abstract class AbstractExam implements Serializable {

	private static final long serialVersionUID = 6141349953004037485L;

	private List<QuestionItem> questionsItemsList;
	private String pictureAbsolutePath;

	public AbstractExam(String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList) {
		this.setPictureAbsolutePath(pictureAbsolutePath);
		this.setQuestionsItemsList(questionsItemsList);
	}

	public List<QuestionItem> getQuestionsItemsList() {

		return (this.questionsItemsList);
	}

	public void setQuestionsItemsList(List<QuestionItem> questionsItemsList) {
		this.questionsItemsList = questionsItemsList;
	}

	public String getPictureAbsolutePath() {

		return (this.pictureAbsolutePath);
	}

	public void setPictureAbsolutePath(String picturePath) {
		this.pictureAbsolutePath = picturePath;
	}
}