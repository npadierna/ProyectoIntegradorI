package co.edu.udea.android.omrgrader.activity.exam.result;

import android.widget.TextView;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class QuestionItemViewHolder {

	private TextView idQuestionItemTextView;
	private TextView[] questionsOptionsTextView;

	public QuestionItemViewHolder(TextView idQuestionItemTextView,
			TextView[] questionsOptionsTextView) {
		this.setIdQuestionItemTextView(idQuestionItemTextView);
		this.setQuestionsOptionsTextView(questionsOptionsTextView);
	}

	public TextView getIdQuestionItemTextView() {

		return (this.idQuestionItemTextView);
	}

	public void setIdQuestionItemTextView(TextView idQuestionItemTextView) {
		this.idQuestionItemTextView = idQuestionItemTextView;
	}

	public TextView[] getQuestionsOptionsTextView() {

		return (this.questionsOptionsTextView);
	}

	public void setQuestionsOptionsTextView(TextView[] questionsOptionsTextView) {
		this.questionsOptionsTextView = questionsOptionsTextView;
	}
}