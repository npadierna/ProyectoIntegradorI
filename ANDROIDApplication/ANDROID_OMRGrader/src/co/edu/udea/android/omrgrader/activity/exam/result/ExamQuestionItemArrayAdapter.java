package co.edu.udea.android.omrgrader.activity.exam.result;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exam.model.ExamQuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class ExamQuestionItemArrayAdapter extends ArrayAdapter<ExamQuestionItem> {

	private int resource;

	private List<ExamQuestionItem> examQuestionsItemsList;

	private Activity activity;

	public ExamQuestionItemArrayAdapter(Activity activity, int resource,
			List<ExamQuestionItem> examQuestionsItemsList) {
		super(activity.getApplicationContext(), resource,
				examQuestionsItemsList);

		this.resource = resource;
		this.activity = activity;
		this.examQuestionsItemsList = examQuestionsItemsList;
	}

	@Override()
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ExamQuestionItemViewHolder questionItemViewHolder;

		if (view == null) {
			LayoutInflater layoutInflater = this.activity.getLayoutInflater();

			view = layoutInflater.inflate(this.resource, null);

			TextView[] questionsOptionsTextView = new TextView[] {
					(TextView) view
							.findViewById(R.id.first_answer_option_text_view),
					(TextView) view
							.findViewById(R.id.second_answer_option_text_view),
					(TextView) view
							.findViewById(R.id.third_answer_option_text_view),
					(TextView) view
							.findViewById(R.id.fourth_answer_option_text_view),
					(TextView) view
							.findViewById(R.id.fifth_answer_option_text_view) };

			questionItemViewHolder = new ExamQuestionItemViewHolder(
					(TextView) view
							.findViewById(R.id.id_answer_option_text_view),
					questionsOptionsTextView);

			view.setTag(questionItemViewHolder);
		} else {
			questionItemViewHolder = (ExamQuestionItemViewHolder) view.getTag();
		}

		this.fillListItem(questionItemViewHolder,
				this.examQuestionsItemsList.get(position));

		return (view);
	}

	private void fillListItem(
			ExamQuestionItemViewHolder questionItemViewHolder,
			ExamQuestionItem examQuestionItem) {
		questionItemViewHolder.getIdQuestionItemTextView().setText(
				String.valueOf(examQuestionItem.getId()));

		for (int pos = 0; pos < questionItemViewHolder
				.getQuestionsOptionsTextView().length; pos++) {
			questionItemViewHolder.getQuestionsOptionsTextView()[pos]
					.setBackgroundColor(this.activity.getResources().getColor(
							examQuestionItem
									.getQuestionsItemsBackgroundColors()[pos]));
		}
	}
}