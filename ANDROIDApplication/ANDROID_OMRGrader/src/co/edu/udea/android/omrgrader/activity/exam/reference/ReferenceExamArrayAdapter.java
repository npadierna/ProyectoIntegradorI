package co.edu.udea.android.omrgrader.activity.exam.reference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exam.model.ReferenceExamItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class ReferenceExamArrayAdapter extends ArrayAdapter<ReferenceExamItem> {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss a", Locale.getDefault());

	private int resource;

	private List<ReferenceExamItem> referenceExamsList;

	private Activity activity;

	public ReferenceExamArrayAdapter(Activity activity, int resource,
			List<ReferenceExamItem> referenceExamsList) {
		super(activity.getApplicationContext(), resource, referenceExamsList);

		this.resource = resource;
		this.activity = activity;
		this.referenceExamsList = referenceExamsList;
	}

	@Override()
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ReferenceExamViewHolder referenceExamViewHolder;

		if (view == null) {
			LayoutInflater layoutInflater = this.activity.getLayoutInflater();

			view = layoutInflater.inflate(this.resource, null);

			referenceExamViewHolder = new ReferenceExamViewHolder(
					(TextView) view
							.findViewById(R.id.last_modified_date_text_view),
					(TextView) view.findViewById(R.id.file_name_text_view));

			view.setTag(referenceExamViewHolder);
		} else {
			referenceExamViewHolder = (ReferenceExamViewHolder) view.getTag();
		}

		this.fillListItem(referenceExamViewHolder,
				this.referenceExamsList.get(position));

		return (view);
	}

	private void fillListItem(ReferenceExamViewHolder referenceExamViewHolder,
			ReferenceExamItem referenceExamItem) {
		referenceExamViewHolder.getLastModifiedDateTextView().setText(
				DATE_FORMAT.format(referenceExamItem.getLastModifiedDate()));
		referenceExamViewHolder.getFullNameTextView().setText(
				referenceExamItem.getFullName());
	}
}