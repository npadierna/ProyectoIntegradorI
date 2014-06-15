package co.edu.udea.android.omrgrader.activity.exam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.session.GraderSession;
import co.edu.udea.android.omrgrader.session.OMRExam;
import co.edu.udea.android.omrgrader.thread.ProcessorAsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

public class StudentExamTakerActivity extends Activity {

	private static final String TAG = StudentExamTakerActivity.class
			.getSimpleName();

	public static final String REFERENCE_EXAM_KEY = "Key for Reference Exam";
	private static final int TAKE_STUDENT_PICTURE_REQUEST = 1;

	private AlertDialog alertDialog;
	private BaseStorageDirectory baseStorageDirectory;
	private GraderSession graderSession;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_student_exam_taker);

		if (savedInstanceState == null) {
			this.createComponents();
		}
	}

	@Override()
	protected void onPause() {
		super.onPause();

		if ((this.alertDialog != null) && (this.alertDialog.isShowing())) {
			this.alertDialog.dismiss();
		}
	}

	private void createComponents() {
		this.alertDialog = this.createAlertDialog(
				R.string.picture_taker_title_alert_dialog,
				R.string.picture_taker_text_alert_dialog, false);
		this.alertDialog.show();

		this.baseStorageDirectory = BaseStorageDirectory.getInstance(super
				.getApplicationContext());

		this.graderSession = new GraderSession(new OMRExam(null, super
				.getIntent().getStringExtra(REFERENCE_EXAM_KEY), null),
				new ArrayList<OMRExam>());
	}

	private AlertDialog createAlertDialog(int dialogTitle, int dialogBodyText,
			boolean isCancelable) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setCancelable(isCancelable)
				.setMessage(dialogBodyText).setTitle(dialogTitle);
		alertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						requestIntentForTakingStudentPicture();
					}
				});
		alertDialogBuilder.setNegativeButton(R.string.cancel_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						startReferenceExamProcessing(graderSession
								.getStudentsOMRExamList().get(0)
								.getPictureRoutePath());
					}
				});

		return (alertDialogBuilder.create());
	}

	private boolean requestIntentForTakingStudentPicture() {
		File takenReferencePictureFile = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File directoryFile = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap().get(
						super.getResources().getString(
								R.string.album_tests_name));
		try {
			takenReferencePictureFile = File.createTempFile(
					"TestingForDevices-",
					super.getResources().getString(R.string.picutres_prefix),
					directoryFile);

			this.graderSession.getStudentsOMRExamList().add(
					new OMRExam(null, takenReferencePictureFile
							.getAbsolutePath(), null));

			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(takenReferencePictureFile));

			super.startActivityForResult(takePictureIntent,
					TAKE_STUDENT_PICTURE_REQUEST);
		} catch (NotFoundException e) {
			Log.e(TAG,
					"A exception has ocurred while the Camera Applications was being requested",
					e);

			return (false);
		} catch (IOException e) {
			Log.e(TAG,
					"A exception has ocurred while the Camera Applications was being requested",
					e);

			return (false);
		}

		return (true);
	}

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_STUDENT_PICTURE_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Log.v(TAG, "The Camera Applicaton Activity has finished");

				this.startReferenceExamProcessing(graderSession
						.getStudentsOMRExamList().get(0).getPictureRoutePath());
			}
			break;
		}
	}

	private void startReferenceExamProcessing(String referenceExamRoutePath) {
		referenceExamRoutePath = "/storage/sdcard0/DCIM/OMRGrader/resources/Full_Sample_5.PNG";

		String onlyLogosTemplatePath = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap()
				.get(BaseStorageDirectory.ONLY_LOGOS_PATH).toString();

		ProcessorAsyncTask processorAsyncTask = new ProcessorAsyncTask(null);
		processorAsyncTask.execute(new String[] {
				onlyLogosTemplatePath,
				referenceExamRoutePath,
				this.baseStorageDirectory
						.getStorageDirectoriesFilesMap()
						.get(super.getResources().getString(
								R.string.album_processed_exams_name))
						.getAbsolutePath(),
				this.baseStorageDirectory
						.getStorageDirectoriesFilesMap()
						.get(super.getResources().getString(
								R.string.album_blackwhite_exams_name))
						.getAbsolutePath() });

		try {
			processorAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}