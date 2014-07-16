package co.edu.udea.android.omrgrader.imageprocess.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
abstract class AbstractExam implements Serializable {

	private static final long serialVersionUID = 6141349953004037485L;
	private List<QuestionItem> questionsItemsList;
	private String picturePath;

	public AbstractExam(String picturePath,
			List<QuestionItem> questionsItemsList) {
		this.setPicturePath(picturePath);
		this.setQuestionsItemsList(questionsItemsList);
	}

	public List<QuestionItem> getQuestionsItemsList() {

		return (this.questionsItemsList);
	}

	public void setQuestionsItemsList(List<QuestionItem> questionsItemsList) {
		this.questionsItemsList = questionsItemsList;
	}

	public String getPicturePath() {

		return (this.picturePath);
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

}