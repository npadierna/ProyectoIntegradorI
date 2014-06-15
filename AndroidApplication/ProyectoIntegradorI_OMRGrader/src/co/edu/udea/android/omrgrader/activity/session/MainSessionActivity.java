package co.edu.udea.android.omrgrader.activity.session;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.exam.ReferenceFileListActivity;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionActivity extends FragmentActivity {

	private static final String TAG = MainSessionActivity.class.getSimpleName();

	private String referencePictureName;

	private DialogFragment inputReferenceNameDialog;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main_session);

		this.createComponents();
	}

	private void createComponents() {
		Bundle bundle = new Bundle();
		bundle.putInt(InputReferenceNameDialogFragment.DIALOG_FRAGMENT_TITLE,
				R.string.main_title_dialog_fragment);

		this.inputReferenceNameDialog = new InputReferenceNameDialogFragment();
		this.inputReferenceNameDialog.setArguments(bundle);
	}

	public void putReferencePictureName(String referencePictureName) {
		this.inputReferenceNameDialog.dismiss();
		if ((referencePictureName == null)
				|| (referencePictureName.trim().equals(""))) {
			this.inputReferenceNameDialog.show(
					super.getSupportFragmentManager(), null);
		} else {
			this.referencePictureName = referencePictureName;
			if (!this.requestIntentForTakingPicture()) {
				// FIXME: Think more about how to handle this error.
			}
		}
	}

	public void onStartGraderSession(View view) {
		super.startActivity(new Intent(super.getApplicationContext(),
				ReferenceFileListActivity.class));
	}

	private boolean requestIntentForTakingPicture() {
		File takenReferencePictureFile = null;
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File directoryFile = BaseStorageDirectory
				.getInstance(super.getApplicationContext())
				.getStorageDirectoriesFilesMap()
				.get(super.getResources().getString(R.string.album_exams_name));
		try {
			takenReferencePictureFile = File.createTempFile(
					this.referencePictureName,
					super.getResources().getString(R.string.picutres_prefix),
					directoryFile);

			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(takenReferencePictureFile));

			super.startActivity(takePictureIntent);
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

	public void onTakeReferenceTemplate(View view) {
		this.inputReferenceNameDialog.show(super.getSupportFragmentManager(),
				null);
	}
}