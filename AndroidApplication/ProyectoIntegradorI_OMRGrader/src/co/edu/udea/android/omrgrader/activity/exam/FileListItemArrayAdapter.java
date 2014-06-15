package co.edu.udea.android.omrgrader.activity.exam;

import java.util.List;

import co.edu.udea.android.omrgrader.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

final class FileListItemArrayAdapter extends ArrayAdapter<FileListItem> {

	private int xmlResourceId;

	private Activity belongingActivity;
	private List<FileListItem> fileListItemsList;

	public FileListItemArrayAdapter(Activity activity, int resource,
			List<FileListItem> fileListItemsList) {
		super(activity, resource, fileListItemsList);

		this.xmlResourceId = resource;
		this.belongingActivity = activity;
		this.fileListItemsList = fileListItemsList;
	}

	@Override()
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		FileItemViewHolder fileItemViewHolder;

		if (view == null) {
			LayoutInflater layoutInflater = this.belongingActivity
					.getLayoutInflater();

			view = layoutInflater.inflate(this.xmlResourceId, null);

			fileItemViewHolder = new FileItemViewHolder();
			fileItemViewHolder.creationDateTextView = (TextView) view
					.findViewById(R.id.creationDateTextView);
			fileItemViewHolder.fullNameTextView = (TextView) view
					.findViewById(R.id.fileNameTextView);

			view.setTag(fileItemViewHolder);
		} else {
			fileItemViewHolder = (FileItemViewHolder) view.getTag();
		}

		fileItemViewHolder.creationDateTextView.setText(this.fileListItemsList
				.get(position).getCreationDate());
		fileItemViewHolder.fullNameTextView.setText(this.fileListItemsList
				.get(position).getFullName());

		return (view);
	}

	static final class FileItemViewHolder {

		TextView creationDateTextView;
		TextView fullNameTextView;
	}
}