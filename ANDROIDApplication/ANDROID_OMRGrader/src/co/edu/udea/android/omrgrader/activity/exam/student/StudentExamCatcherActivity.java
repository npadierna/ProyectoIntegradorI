package co.edu.udea.android.omrgrader.activity.exam.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StudentExamCatcherActivity extends Activity {

	private static final String TAG = StudentExamCatcherActivity.class
			.getSimpleName();

	public static final String REFERENCE_EXAM_PATH_KEY = "Key for Reference Exam Path";
	private static final int TAKE_STUDENT_PICTURE_REQUEST = 1;

	private String referenceExamAbsolutePath;

	private AlertDialog.Builder alertDialogBuilder;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_student_exam_catcher);

		this.createWidgetsComponents();
		this.extractValuesFromBundle(super.getIntent().getExtras());
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.alertDialogBuilder = new AlertDialog.Builder(this);
		this.alertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						StudentExamCatcherActivity.super.finish();
					}
				});
	}

	private void extractValuesFromBundle(Bundle bundle) {
		Log.v(TAG, "extractValuesFromBundle(Bundle):void");

		if (bundle.containsKey(REFERENCE_EXAM_PATH_KEY)) {
			this.referenceExamAbsolutePath = bundle
					.getString(REFERENCE_EXAM_PATH_KEY);
		}

		if ((TextUtils.isEmpty(this.referenceExamAbsolutePath))
				|| (TextUtils.isEmpty(this.referenceExamAbsolutePath.trim()))) {
			// FIXME: Show a Dialog for informating this issue.
		}
	}
}