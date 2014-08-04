package co.edu.udea.android.omrgrader.activity.exam.student;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.exam.result.ExamResultListActivity;
import co.edu.udea.android.omrgrader.process.exam.StudentExamHelper;
import co.edu.udea.android.omrgrader.process.exam.session.ExamSessionGrader;
import co.edu.udea.android.omrgrader.process.exam.thread.GalleryHelperAsyncTask;

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
	private static final int TAKE_STUDENT_EXAM_PICTURE_REQUEST = 1;

	private long endingTime;
	private long startingTime;

	private String referenceExamAbsolutePath;
	private String newStudentExamAbsolutePath;

	private ExamSessionGrader examGraderSession;
	private StudentExamHelper studentExamHelper;

	private AlertDialog.Builder errorAlertDialogBuilder;
	private ProgressDialog progressDialog;

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_STUDENT_EXAM_PICTURE_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				try {
					this.progressDialog
							.setMessage(super
									.getString(R.string.student_exam_processing_message_progress_dialog));
					this.progressDialog
							.setTitle(R.string.student_exam_processing_title_progress_dialog);

					// FIXME: This is not the correct absolute path.
					this.newStudentExamAbsolutePath = "/storage/sdcard0/DCIM/OMRGrader/resources/Full_Sample_5.PNG";

					// FIXME: Do this in a separated Thread.
					this.handleBigCameraImage();

					// *** Starting Performance Mode *** //
					this.startingTime = SystemClock.elapsedRealtime();

					this.examGraderSession.computeStudentExam(
							this.progressDialog,
							this.newStudentExamAbsolutePath);

					this.endingTime = SystemClock.elapsedRealtime();
					Log.i(TAG, String.format(
							"%s: Elapse milliseconds time: %d", "STUDENT EXAM",
							(this.endingTime - this.startingTime)));
					// *** Ending Performance Mode *** //
				} catch (Exception e) {
					this.errorAlertDialogBuilder
							.setMessage(R.string.error_processing_student_exam_message_alert_dialog);
					this.errorAlertDialogBuilder
							.setTitle(R.string.error_processing_student_exam_title_alert_dialog);
					(this.errorAlertDialogBuilder.create()).show();

					return;
				}
			}
			break;
		}
	}

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_student_exam_catcher);

		this.createWidgetsComponents();
		this.extractValuesFromBundle(super.getIntent().getExtras());
		this.createComponents();
	}

	@Override()
	protected void onResume() {
		super.onResume();

		if (this.newStudentExamAbsolutePath == null) {
			try {
				this.newStudentExamAbsolutePath = this
						.createIntentForTakingStudentExamPicture(
								super.getString(R.string.default_base_student_exam_name),
								this.studentExamHelper
										.obtainDirectoryFileForExams())
						.getAbsolutePath();
			} catch (Exception e) {
				this.errorAlertDialogBuilder
						.setMessage(R.string.error_taking_student_exam_message_alert_dialog);
				this.errorAlertDialogBuilder
						.setTitle(R.string.error_taking_student_exam_title_alert_dialog);
				(this.errorAlertDialogBuilder.create()).show();

				return;
			}
		} else {
			Bundle bundle = new Bundle();
			bundle.putParcelable(
					ExamResultListActivity.EXAM_GRADER_SESSION_KEY,
					this.examGraderSession);

			Intent intent = new Intent(super.getApplicationContext(),
					ExamResultListActivity.class);
			intent.putExtras(bundle);

			super.startActivity(intent);
			super.finish();
		}
	}

	private File createIntentForTakingStudentExamPicture(
			String examPictureName, File destinationDirectoryFile)
			throws NotFoundException, IOException {
		File takenReferencePictureFile = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		takenReferencePictureFile = File.createTempFile(
				examPictureName,
				super.getApplicationContext().getResources()
						.getString(R.string.pictures_prefix),
				destinationDirectoryFile);

		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(takenReferencePictureFile));

		super.startActivityForResult(takePictureIntent,
				TAKE_STUDENT_EXAM_PICTURE_REQUEST);

		return (takenReferencePictureFile);
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if (this.newStudentExamAbsolutePath == null) {
			// FIXME: This is not the correct absolute path.
			this.examGraderSession
					.getGraderSession()
					.getReferenceExam()
					.setPictureAbsolutePath(
							"/storage/sdcard0/DCIM/OMRGrader/resources/Full_Sample_5.PNG");
			try {
				this.progressDialog
						.setMessage(super
								.getString(R.string.reference_exam_processing_message_progress_dialog));
				this.progressDialog
						.setTitle(super
								.getString(R.string.reference_exam_processing_title_progress_dialog));

				// *** Starting Performance Mode *** //
				this.startingTime = SystemClock.elapsedRealtime();

				this.examGraderSession
						.computeReferenceExam(this.progressDialog);

				this.endingTime = SystemClock.elapsedRealtime();
				Log.i(TAG, String
						.format("%s: Elapse milliseconds time: %d",
								"REFERENCE EXAM",
								(this.endingTime - this.startingTime)));
				// *** Ending Performance Mode *** //
			} catch (Exception e) {
				this.errorAlertDialogBuilder
						.setMessage(R.string.error_processing_reference_exam_message_alert_dialog);
				this.errorAlertDialogBuilder
						.setTitle(R.string.error_processing_reference_exam_title_alert_dialog);
				(this.errorAlertDialogBuilder.create()).show();

				return;
			}
		}
	}

	private void createComponents() {
		Log.v(TAG, "createComponents():void");

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getApplicationContext());
		int radiusLenght = Integer
				.valueOf(sharedPreferences.getString(
						super.getString(R.string.grader_settings_preference_radius_lenght_key),
						String.valueOf(super.getResources().getInteger(
								R.integer.question_item_bubble_thresh))));
		int minimumThresh = Integer
				.valueOf(sharedPreferences.getString(
						super.getString(R.string.grader_settings_preference_minimum_thresh_key),
						String.valueOf(super.getResources().getInteger(
								R.integer.question_item_bubble_thresh))));

		this.examGraderSession = new ExamSessionGrader(
				super.getApplicationContext(), this.referenceExamAbsolutePath,
				super.getResources().getInteger(
						R.integer.questions_options_amout), radiusLenght,
				minimumThresh);
		this.examGraderSession
				.buildBubblesCenterCoordinates(
						super.getResources().getIntArray(
								R.array.bubbles_x_coordinates),
						super.getResources().getIntArray(
								R.array.bubbles_y_coordinates),
						super.getResources().getInteger(
								R.integer.questions_items_columns_amount));

		this.studentExamHelper = new StudentExamHelper(
				super.getApplicationContext());

		this.newStudentExamAbsolutePath = null;
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						StudentExamCatcherActivity.super.finish();
					}
				});

		this.progressDialog = new ProgressDialog(this);
		this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.progressDialog.setCancelable(false);
	}

	private void extractValuesFromBundle(Bundle bundle) {
		Log.v(TAG, "extractValuesFromBundle(Bundle):void");

		if ((bundle != null) && (bundle.containsKey(REFERENCE_EXAM_PATH_KEY))) {
			this.referenceExamAbsolutePath = bundle
					.getString(REFERENCE_EXAM_PATH_KEY);
		}

		if ((TextUtils.isEmpty(this.referenceExamAbsolutePath))
				|| (TextUtils.isEmpty(this.referenceExamAbsolutePath.trim()))) {
			this.errorAlertDialogBuilder
					.setMessage(R.string.no_reference_exam_name_found_message_alert_dialog);
			this.errorAlertDialogBuilder
					.setTitle(R.string.no_reference_exam_name_found_title_alert_dialog);
			(this.errorAlertDialogBuilder.create()).show();

			return;
		}
	}

	private void handleBigCameraImage() {
		// if (this.newStudentExamAbsolutePath != null) {
		// Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		//
		// Uri uri = Uri.fromFile(new File(this.newStudentExamAbsolutePath));
		// intent.setData(uri);
		//
		// super.sendBroadcast(intent);
		// }
		AsyncTask<String, Void, File> galleryHelperAsyncTask = new GalleryHelperAsyncTask(
				super.getApplicationContext());
		galleryHelperAsyncTask.execute(this.newStudentExamAbsolutePath);

		try {
			galleryHelperAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}