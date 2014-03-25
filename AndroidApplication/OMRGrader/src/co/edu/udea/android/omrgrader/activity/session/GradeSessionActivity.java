package co.edu.udea.android.omrgrader.activity.session;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.result.GradeResultsActivity;
import co.edu.udea.android.omrgrader.activity.util.AlertDialogCustomized;
import co.edu.udea.android.omrgrader.directory.DefaultDirectoryHelper;
import co.edu.udea.android.omrgrader.model.DateFormatter;
import co.edu.udea.android.omrgrader.model.GraderSession;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GradeSessionActivity extends FragmentActivity {

	private static final String TAG = GradeSessionActivity.class
			.getSimpleName();

	public static final String CURRENT_GRADER_SESSION = "Key for current GraderSession";

	private static final int TAKE_PICTURE_ACTION = 1;

	private static final String ALBUM_BASE_NAME = "OMRGrader/sessions";
	private static final String PICTURE_FORMAT = ".jpg";
	private static final String PICTURE_REFERENCE_BASE_NAME = "ReferencePictureForAnswers";
	private static final String STUDENT_PICTURE_BASE_NAME = "AnswersStudentsPicture_";

	private boolean isPictureReferenceTaken = false;

	private DialogFragment dialogFragment;

	private AlertDialog backButtonPressedAlertDialog;
	private Button computeStudentsScoreButton;
	private Button startTakingStudentsPicturesButton;
	private Button takePictureReferenceButton;

	private DefaultDirectoryHelper directoryHelper;
	private GraderSession currentSession;

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(GradeSessionActivity.TAG,
				"onActivityResult(int requestCode, int resultCode, Intent data) method.");

		switch (requestCode) {
		case (GradeSessionActivity.TAKE_PICTURE_ACTION):
			Log.d(GradeSessionActivity.TAG,
					"The requestCode has been: TAKE_PICTURE_ACTION.");

			if (resultCode == Activity.RESULT_OK) {
				Log.d(GradeSessionActivity.TAG,
						"The Camera Application has returned OKAY.");

				if (this.isPictureReferenceTaken == false) {
					this.isPictureReferenceTaken = true;

					this.takePictureReferenceButton.setEnabled(false);
					this.startTakingStudentsPicturesButton.setEnabled(true);
				} else {
					if (this.currentSession.isEnd()) {
						this.computeStudentsScoreButton.setEnabled(true);
						this.startTakingStudentsPicturesButton
								.setEnabled(false);
					} else {
						this.takePictureOnClickListener
								.onClick(this.startTakingStudentsPicturesButton);
					}
				}
			} else {
				Log.e(GradeSessionActivity.TAG,
						"The Camera Application has returned: " + resultCode);
			}
			break;
		}
	}

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_grade_session);

		this.createComponents();

		// FIXME: This invocation for showing Fragment Dialog should not be
		// here.
		this.showSessionCreatorDialog();
	}

	@Override()
	public void onBackPressed() {
		Log.i(GradeSessionActivity.TAG, "onBackPressed() method.");

		this.backButtonPressedAlertDialog.show();
	}

	private void createComponents() {
		Log.i(GradeSessionActivity.TAG, "createComponents() method.");

		this.dialogFragment = SessionDataDialogFragment
				.newInstance(R.string.session_dialog_title);

		AlertDialog.Builder alertDialogBuilder = (new AlertDialogCustomized(
				this))
				.createAlertDialog(
						super.getString(R.string.back_pressed_alert_dialog_title),
						super.getString(R.string.back_pressed_alert_dialog_text),
						false);
		alertDialogBuilder.setPositiveButton(
				super.getString(R.string.accept_text),
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						GradeSessionActivity.super.finish();
					}
				});
		alertDialogBuilder.setNegativeButton(
				super.getString(R.string.cancel_text), null);
		this.backButtonPressedAlertDialog = alertDialogBuilder.create();

		this.currentSession = new GraderSession((short) 2, "First Session");
		this.directoryHelper = new DefaultDirectoryHelper(
				GradeSessionActivity.ALBUM_BASE_NAME,
				this.currentSession.getSessionName());

		this.computeStudentsScoreButton = (Button) super
				.findViewById(R.id.compute_students_score_button);

		this.startTakingStudentsPicturesButton = (Button) super
				.findViewById(R.id.start_taking_students_pictures_button);
		this.startTakingStudentsPicturesButton
				.setOnClickListener(this.takePictureOnClickListener);

		this.takePictureReferenceButton = (Button) super
				.findViewById(R.id.take_picture_reference_button);
		this.takePictureReferenceButton
				.setOnClickListener(this.takePictureOnClickListener);
	}

	public void onCaptureSessionData(String sessionName,
			String studentsTestsAmount) {
		Log.d(GradeSessionActivity.TAG, "Session Name: " + sessionName);
		Log.d(GradeSessionActivity.TAG, "Test Amount: " + studentsTestsAmount);

		try {
			short s = Short.parseShort(studentsTestsAmount);

			if ((s <= 0) || (sessionName.trim().equals(""))) {
				this.showSessionCreatorDialog();
			}
		} catch (NumberFormatException e) {
			this.showSessionCreatorDialog();
		}

		// FIXME: All data is correcto for the session, now what?
	}

	public void onProcessStudentsTests(View view) {
		Log.i(GradeSessionActivity.TAG,
				"onProcessStudentsTest(View view) method. Starting image processing...");

		// FIXME: Invocation to the Image Processing algorithm.

		this.startResultsActivity();
	}

	private void startResultsActivity() {
		Log.i(GradeSessionActivity.TAG,
				"startResultsActivity() method. Starting GradeResultsActivity...");

		Bundle bundle = new Bundle();
		bundle.putParcelable(GradeSessionActivity.CURRENT_GRADER_SESSION,
				this.currentSession);

		Intent intent = new Intent(super.getApplicationContext(),
				GradeResultsActivity.class);
		intent.putExtras(bundle);

		// FIXME: Will it be necessary kill this Activity?.

		super.startActivity(intent);
	}

	private void showSessionCreatorDialog() {
		if (this.dialogFragment.isAdded()) {
			this.dialogFragment.dismiss();
			this.dialogFragment.onDestroyView();
		}

		this.dialogFragment.show(super.getSupportFragmentManager(),
				"Dialog For Session Creator");
	}

	private final Button.OnClickListener takePictureOnClickListener = new Button.OnClickListener() {

		@Override()
		public void onClick(View view) {
			Log.i(GradeSessionActivity.TAG,
					"onClick(View view) method. Taking picture from Camera.");

			File takenPictureFile = null;
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			StringBuilder newPictureName = null;

			if (!isPictureReferenceTaken) {
				newPictureName = new StringBuilder(
						GradeSessionActivity.PICTURE_REFERENCE_BASE_NAME);
			} else {
				newPictureName = new StringBuilder(
						GradeSessionActivity.STUDENT_PICTURE_BASE_NAME);
				newPictureName.append(currentSession.studentsTestTaken());
			}
			newPictureName.append(
					DateFormatter.getInstance().formatDate(new Date())).append(
					"_");

			try {
				takenPictureFile = File.createTempFile(
						newPictureName.toString(),
						GradeSessionActivity.PICTURE_FORMAT,
						directoryHelper.getDefaultAlbumDirectory());

				if (!isPictureReferenceTaken) {
					currentSession.setReferencePicturePath(takenPictureFile
							.getAbsolutePath());

					Log.d(GradeSessionActivity.TAG,
							"Reference Picture Taken path: "
									+ currentSession.getReferencePicturePath());
				} else {
					currentSession.addNewStudentsPicturePath(takenPictureFile
							.getAbsolutePath());

					Log.d(GradeSessionActivity.TAG,
							"Last Student Picture taken index: "
									+ currentSession.studentsTestTaken());
				}

				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(takenPictureFile));
			} catch (IOException e) {
				takenPictureFile = null;

				// FIXME: Think more about how drive this.

				e.printStackTrace();
			}

			GradeSessionActivity.super
					.startActivityForResult(takePictureIntent,
							GradeSessionActivity.TAKE_PICTURE_ACTION);
		}
	};
}