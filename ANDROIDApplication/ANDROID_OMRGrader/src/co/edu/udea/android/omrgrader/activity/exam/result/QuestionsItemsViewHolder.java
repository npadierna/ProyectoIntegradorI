package co.edu.udea.android.omrgrader.activity.exam.result;

import android.widget.TextView;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class QuestionsItemsViewHolder {

	private TextView[] questionsItemsTextView;

	public QuestionsItemsViewHolder(TextView[] questionsItemsTextView) {
		this.setQuestionsItemsTextView(questionsItemsTextView);
	}

	public TextView[] getQuestionsItemsTextView() {

		return (this.questionsItemsTextView);
	}

	public void setQuestionsItemsTextView(TextView[] questionsItemsTextView) {
		this.questionsItemsTextView = questionsItemsTextView;
	}
}