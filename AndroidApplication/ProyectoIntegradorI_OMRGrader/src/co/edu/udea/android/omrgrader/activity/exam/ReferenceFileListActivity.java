package co.edu.udea.android.omrgrader.activity.exam;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;

public class ReferenceFileListActivity extends Activity {

	private static final String TAG = ReferenceFileListActivity.class
			.getSimpleName();

	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss a");

	private BaseStorageDirectory baseStorageDirectory;
	private File examsReferencesFile;
	private List<FileListItem> fileListItemsList;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_references_file_list);

		this.createComponents();
	}

	private void createComponents() {
		this.baseStorageDirectory = BaseStorageDirectory.getInstance(super
				.getApplicationContext());

		this.examsReferencesFile = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap().get(
						super.getResources().getString(
								R.string.album_exams_name));

		this.fileListItemsList = this
				.findFilesIntoDirectory(examsReferencesFile);

		if (!this.fileListItemsList.isEmpty()) {
			ArrayAdapter<FileListItem> fileListItemArrayAdapter = new FileListItemArrayAdapter(
					this, R.layout.list_item_file_list, this.fileListItemsList);

			ListView fileListItemListView = (ListView) super
					.findViewById(R.id.examsFileListView);
			fileListItemListView.setAdapter(fileListItemArrayAdapter);
			fileListItemListView
					.setOnItemClickListener(this.fileListItemOnClickListener);
			fileListItemListView.setVisibility(View.VISIBLE);
		} else {
			((TextView) super.findViewById(R.id.emptyFileListTextView))
					.setVisibility(View.VISIBLE);
		}
	}

	private List<FileListItem> findFilesIntoDirectory(File directoryFile) {
		List<FileListItem> fileListItemList = new ArrayList<FileListItem>();

		for (File file : directoryFile.listFiles()) {
			fileListItemList.add(new FileListItem(DATE_FORMAT.format(new Date(
					file.lastModified())), file.getName()));
		}

		return (fileListItemList);
	}

	private OnItemClickListener fileListItemOnClickListener = new OnItemClickListener() {

		@Override()
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.v(TAG,
					"FileListItem Selected: "
							+ fileListItemsList.get(position).getFullName());

			startStudentTakingPictureActivity(fileListItemsList.get(position)
					.getFullName());
		}
	};

	private void startStudentTakingPictureActivity(String referenceExamName) {
		StringBuilder pathStringBuilder = new StringBuilder(
				baseStorageDirectory
						.getStorageDirectoriesFilesMap()
						.get(super.getResources().getString(
								R.string.album_exams_name)).getAbsolutePath());

		pathStringBuilder.append("/").append(referenceExamName);

		Log.v(TAG, "Full Absolute Path for Reference Exam: "
				+ pathStringBuilder.toString());

		Intent intent = new Intent(super.getApplicationContext(),
				StudentExamTakerActivity.class);
		intent.putExtra(StudentExamTakerActivity.REFERENCE_EXAM_KEY,
				pathStringBuilder.toString());

		super.startActivity(intent);
	}
}