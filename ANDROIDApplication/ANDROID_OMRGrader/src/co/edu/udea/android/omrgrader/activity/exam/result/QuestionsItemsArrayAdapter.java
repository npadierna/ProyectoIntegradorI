package co.edu.udea.android.omrgrader.activity.exam.result;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;
import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class QuestionsItemsArrayAdapter extends ArrayAdapter<List<QuestionItem>> {

	private int resource;

	private List<QuestionItem> questionsItemsList;

	private Activity activity;

	public QuestionsItemsArrayAdapter(Activity activity, int resource,
			List<QuestionItem> questionsItemsList) {
		super(activity.getApplicationContext(), resource);

		this.resource = resource;
		this.activity = activity;
		this.questionsItemsList = questionsItemsList;
	}
}