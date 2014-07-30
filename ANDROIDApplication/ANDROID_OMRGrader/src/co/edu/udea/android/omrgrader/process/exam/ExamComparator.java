package co.edu.udea.android.omrgrader.process.exam;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;
import co.edu.udea.android.omrgrader.process.exam.model.ExamQuestionItem;
import co.edu.udea.android.omrgrader.process.model.ReferenceExam;
import co.edu.udea.android.omrgrader.process.model.StudentExam;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamComparator {

	private ExamGraderSession examGraderSession;

	private Context context;

	public ExamComparator(Context context, ExamGraderSession examGraderSession) {
		this.setContext(context);
		this.setExamGraderSession(examGraderSession);
	}

	public ExamGraderSession getExamGraderSession() {

		return (this.examGraderSession);
	}

	public void setExamGraderSession(ExamGraderSession examGraderSession) {
		this.examGraderSession = examGraderSession;
	}

	public Context getContext() {

		return (this.context);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<ExamQuestionItem> compareAnswers(StudentExam studentExam) {
		List<ExamQuestionItem> examQuestionsItemsList = new ArrayList<ExamQuestionItem>();
		ReferenceExam referenceExam = this.getExamGraderSession()
				.getGraderSession().getReferenceExam();

		for (int counter = 0; counter < referenceExam.getQuestionsItemsList()
				.size(); counter++) {
			QuestionItem referenceQuestionItem = referenceExam
					.getQuestionsItemsList().get(counter);
			QuestionItem studentQuestionItem = studentExam
					.getQuestionsItemsList().get(counter);
			int[] questionsItemsBackgroundColors = new int[this
					.getExamGraderSession().getQuestionsOptionsAmout()];
			boolean correctAnswer = referenceQuestionItem
					.equals(studentQuestionItem);

			boolean[] referenceQuestionsOptionsChosen = referenceQuestionItem
					.getQuestionsOptionsChosen();
			boolean[] studentQuestionsOptionsChosen = studentQuestionItem
					.getQuestionsOptionsChosen();
			for (int pos = 0; pos < referenceQuestionsOptionsChosen.length; pos++) {
				int referenceColor;

				if (referenceQuestionsOptionsChosen[pos]) {
					if (studentQuestionsOptionsChosen[pos]) {
						referenceColor = R.color.correct_answer_background_text_view;
					} else {
						referenceColor = R.color.expected_answer_background_text_view;
					}
				} else {
					if (studentQuestionsOptionsChosen[pos]) {
						referenceColor = R.color.wrong_answer_background_text_view;
					} else {
						referenceColor = R.color.normal_answer_background_text_view;
					}
				}
				questionsItemsBackgroundColors[pos] = referenceColor;
			}

			examQuestionsItemsList.add(new ExamQuestionItem(
					referenceQuestionItem.getId(), correctAnswer,
					questionsItemsBackgroundColors));
		}

		return (examQuestionsItemsList);
	}

	public int countCorrectAnswers(List<ExamQuestionItem> examQuestionsItems) {
		int counter = 0;

		for (ExamQuestionItem examQuestionItem : examQuestionsItems) {
			if (examQuestionItem.isCorrectAnswer()) {
				counter++;
			}
		}

		return (counter);
	}
}