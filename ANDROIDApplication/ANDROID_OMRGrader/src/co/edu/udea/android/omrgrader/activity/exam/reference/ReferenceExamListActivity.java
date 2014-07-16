package co.edu.udea.android.omrgrader.activity.exam.reference;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exam.ReferenceExamHelper;
import co.edu.udea.android.omrgrader.process.exam.ReferenceExamItem;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ReferenceExamListActivity extends ListActivity {

	private static final String TAG = ReferenceExamListActivity.class
			.getSimpleName();

	private ReferenceExamHelper referenceExamHelper;

	private AlertDialog.Builder alertDialogBuilder;

	private List<ReferenceExamItem> referenceExamsItemsList;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.list_activity_reference_exam);

		this.createComponents();
		this.createWidgetsComponents();
	}

	private void createComponents() {
		Log.v(TAG, "createComponents():void");

		this.referenceExamHelper = new ReferenceExamHelper(
				super.getApplicationContext());
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.alertDialogBuilder = new AlertDialog.Builder(this);
		this.alertDialogBuilder
				.setMessage(R.string.error_founding_reference_exams_message_alert_dialog);
		this.alertDialogBuilder
				.setTitle(R.string.error_founding_reference_exams_title_alert_dialog);
		this.alertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						ReferenceExamListActivity.super.finish();
					}
				});

		try {
			this.referenceExamsItemsList = this.referenceExamHelper
					.findAllReferenceExamsItems();
		} catch (OMRGraderProcessException e) {
			(this.alertDialogBuilder.create()).show();

			return;
		}

		if (!this.referenceExamsItemsList.isEmpty()) {
			ListAdapter referenceExamListAdapter = new ReferenceExamArrayAdapter(
					this, R.layout.item_reference_exam_list,
					this.referenceExamsItemsList);

			ListView referenceExamsListView = (ListView) super
					.findViewById(android.R.id.list);
			referenceExamsListView
					.setOnItemClickListener(this.referenceExamListItemOnClickListener);
			referenceExamsListView.setVisibility(View.VISIBLE);

			super.setListAdapter(referenceExamListAdapter);
		} else {
			super.findViewById(R.id.empty_reference_exams_list_text_view)
					.setVisibility(View.VISIBLE);
		}
	}

	private OnItemClickListener referenceExamListItemOnClickListener = new OnItemClickListener() {

		@Override()
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.v(TAG, String.format("Reference Examn Item Selected: %s",
					referenceExamsItemsList.get(position).getFullName()));

			startStudentTakingPictureActivity(referenceExamsItemsList.get(
					position).getFullName());
		}
	};

	private void startStudentTakingPictureActivity(String referenceExamName) {
		// StringBuilder pathStringBuilder = new StringBuilder(
		// baseStorageDirectory
		// .getStorageDirectoriesFilesMap()
		// .get(super.getResources().getString(
		// R.string.album_exams_name)).getAbsolutePath());
		//
		// pathStringBuilder.append("/").append(referenceExamName);
		//
		// Log.v(TAG, "Full Absolute Path for Reference Exam: "
		// + pathStringBuilder.toString());
		//
		// Intent intent = new Intent(super.getApplicationContext(),
		// StudentExamTakerActivity.class);
		// intent.putExtra(StudentExamTakerActivity.REFERENCE_EXAM_KEY,
		// pathStringBuilder.toString());
		//
		// super.startActivity(intent);
	}
}