package co.edu.udea.android.omrgrader.activity.initial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.session.MainSessionActivity;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StarterConfigurationActivity extends Activity {

	private static final String TAG = StarterConfigurationActivity.class
			.getSimpleName();

	static {
		try {
			System.loadLibrary("opencv_java");
			System.loadLibrary("nonfree");
		} catch (Exception e) {
			Log.e(TAG,
					"Error while the application was trying to load the OpenCV libraries",
					e);
		}
	}

	private AlertDialog alertDialog;

	private boolean cameraOk;
	private boolean directoriesOk;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_starter_configuration);

		this.checkCameraApplication();
		this.createBaseStorageDirectory();
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if ((this.cameraOk) && (this.directoriesOk)) {
			// FIXME: And what happen with the coping-error?.
			File onlyLogosTemplateFile = this.copyBaseTemplateToDirectory();
			BaseStorageDirectory
					.getInstance(super.getApplicationContext())
					.getStorageDirectoriesFilesMap()
					.put(BaseStorageDirectory.ONLY_LOGOS_PATH,
							onlyLogosTemplateFile);

			Log.v(TAG, "Starting Activity for Grading Sessions for Exams");

			super.startActivity(new Intent(super.getApplicationContext(),
					MainSessionActivity.class));
			super.finish();
		}
	}

	private File copyBaseTemplateToDirectory() {
		AssetManager assetManager = super.getAssets();
		File outputFile = null;
		InputStream inputStream = null;
		String[] assetsFilesNames = null;

		try {
			assetsFilesNames = assetManager.list("images/template");
			inputStream = assetManager.open("images/template/"
					+ assetsFilesNames[0]);

			File directoryFile = BaseStorageDirectory
					.getInstance(this.getApplicationContext())
					.getStorageDirectoriesFilesMap()
					.get(super.getResources().getString(
							R.string.album_templates_name));
			outputFile = new File(directoryFile, assetsFilesNames[0]);

			if (!outputFile.exists()) {
				OutputStream outputStream = new FileOutputStream(outputFile);
				int read = 0;
				byte[] bytes = new byte[2048];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				outputStream.flush();
				outputStream.close();
				inputStream.close();
			}

			return (outputFile);
		} catch (IOException e) {
			Log.e(TAG,
					"A grave error has occurred while the Only Logos Template was being copied to Storage Directory.",
					e);
		}

		return (null);
	}

	@Override()
	protected void onPause() {
		super.onPause();

		if ((this.alertDialog != null) && (this.alertDialog.isShowing())) {
			this.alertDialog.dismiss();
		}
	}

	private void checkCameraApplication() {
		this.cameraOk = this.isCameraApplicationAvailable();
		if (!this.cameraOk) {
			Log.d(TAG, "There is not any available camera application.");

			if (this.alertDialog == null) {
				this.alertDialog = this.createAlertDialog(
						R.string.no_camera_title_alert_dialog,
						R.string.no_camera_text_alert_dialog, false);
				this.alertDialog.show();
			}
		}
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
						finish();
					}
				});

		return (alertDialogBuilder.create());
	}

	private void createBaseStorageDirectory() {
		BaseStorageDirectory baseStorageDirectory = BaseStorageDirectory
				.getInstance(this.getApplicationContext());

		this.directoriesOk = !baseStorageDirectory
				.getStorageDirectoriesFilesMap().isEmpty();
		if ((!this.directoriesOk) && (this.alertDialog == null)) {
			this.alertDialog = this.createAlertDialog(
					R.string.no_directory_created_title_alert_dialog,
					R.string.no_directory_created_text_alert_dialog, false);
			this.alertDialog.show();
		}
	}

	private boolean isCameraApplicationAvailable() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> returnedResolveInformation = super
				.getPackageManager().queryIntentActivities(cameraIntent,
						PackageManager.MATCH_DEFAULT_ONLY);

		return (!returnedResolveInformation.isEmpty());
	}
}