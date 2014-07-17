package co.edu.udea.android.omrgrader.activity.exam.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exam.ExamGraderSession;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

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
	private String newStudentExamAbsolutePath;

	private ExamGraderSession examGraderSession;

	private AlertDialog.Builder alertDialogBuilder;
	private ProgressDialog progressDialog;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_student_exam_catcher);

		this.createWidgetsComponents();
		this.extractValuesFromBundle(super.getIntent().getExtras());
		this.createComponents();
	}

	@Override()
	protected void onStart() {
		super.onStart();

		this.examGraderSession
				.getGraderSession()
				.getReferenceExam()
				.setPictureAbsolutePath(
						"/storage/sdcard0/DCIM/OMRGrader/resources/Full_Sample_5.PNG");
		try {
			this.progressDialog.setMessage(super
					.getString(R.string.application_name));
			this.progressDialog.setTitle(super
					.getString(R.string.application_name));

			this.examGraderSession.computeReferenceExam(this.progressDialog);
		} catch (OMRGraderProcessException e) {
			e.printStackTrace();
		}
	}

	private void createComponents() {
		Log.v(TAG, "createComponents():void");

		this.examGraderSession = new ExamGraderSession(
				super.getApplicationContext(), this.referenceExamAbsolutePath,
				super.getResources().getInteger(
						R.integer.questions_options_amout), super
						.getResources().getInteger(
								R.integer.question_item_bubble_radius_length),
				super.getResources().getInteger(
						R.integer.question_item_bubble_thresh));
		this.examGraderSession
				.buildBubblesCenterCoordinates(
						super.getResources().getIntArray(
								R.array.bubbles_x_coordinates),
						super.getResources().getIntArray(
								R.array.bubbles_y_coordinates),
						super.getResources().getInteger(
								R.integer.questions_items_columns_amount));
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

		this.progressDialog = new ProgressDialog(this.getApplicationContext());
		this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.progressDialog.setCancelable(false);
	}

	private void extractValuesFromBundle(Bundle bundle) {
		Log.v(TAG, "extractValuesFromBundle(Bundle):void");

		if (bundle.containsKey(REFERENCE_EXAM_PATH_KEY)) {
			this.referenceExamAbsolutePath = bundle
					.getString(REFERENCE_EXAM_PATH_KEY);
		}

		if ((TextUtils.isEmpty(this.referenceExamAbsolutePath))
				|| (TextUtils.isEmpty(this.referenceExamAbsolutePath.trim()))) {
			// FIXME: Add the message and title.
			(this.alertDialogBuilder.create()).show();
		}
	}
}