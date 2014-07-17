package co.edu.udea.android.omrgrader.process.model;

import java.util.List;

import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StudentExam extends AbstractExam {

	private static final long serialVersionUID = 5473039490111890206L;

	private float grade;

	private String idNumber;

	public StudentExam(String idNumber, String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList) {
		this(idNumber, pictureAbsolutePath, questionsItemsList, -1.0F);
	}

	public StudentExam(String idNumber, String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList, float grade) {
		super(pictureAbsolutePath, questionsItemsList);

		this.setIdNumber(idNumber);
		this.setGrade(grade);
	}

	public float getGrade() {

		return (this.grade);
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public String getIdNumber() {

		return (this.idNumber);
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
}