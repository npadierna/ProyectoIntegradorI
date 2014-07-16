package co.edu.udea.android.omrgrader.activity.exam.reference;

import android.widget.TextView;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class ReferenceExamViewHolder {

	private TextView lastModifiedDateTextView;
	private TextView fullNameTextView;

	public ReferenceExamViewHolder(TextView lastModifiedDateTextView,
			TextView fullNameTextView) {
		this.setFullNameTextView(fullNameTextView);
		this.setLastModifiedDateTextView(lastModifiedDateTextView);
	}

	public TextView getLastModifiedDateTextView() {

		return (this.lastModifiedDateTextView);
	}

	public void setLastModifiedDateTextView(TextView lastModifiedDateTextView) {
		this.lastModifiedDateTextView = lastModifiedDateTextView;
	}

	public TextView getFullNameTextView() {

		return (this.fullNameTextView);
	}

	public void setFullNameTextView(TextView fullNameTextView) {
		this.fullNameTextView = fullNameTextView;
	}
}