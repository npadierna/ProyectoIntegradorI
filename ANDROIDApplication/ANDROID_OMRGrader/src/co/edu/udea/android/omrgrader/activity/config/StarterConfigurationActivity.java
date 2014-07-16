package co.edu.udea.android.omrgrader.activity.config;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.session.MainSessionActivity;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.process.config.CameraValidator;
import co.edu.udea.android.omrgrader.process.config.LibrariesLoader;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StarterConfigurationActivity extends Activity {

	private static final String TAG = StarterConfigurationActivity.class
			.getSimpleName();

	private static boolean isLibrariesLoaded = false;

	static {
		try {
			isLibrariesLoaded = LibrariesLoader.loadLibraries();
		} catch (OMRGraderProcessException e) {
			Log.e(TAG,
					"Error while the application was trying to load the required libraries",
					e);
		}
	}

	private BaseStorageDirectory baseStorageDirectory;

	private AlertDialog.Builder alertDialogBuilder;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.getActionBar().hide();
		super.setContentView(R.layout.activity_starter_configuration);

		this.createWidgetsComponents();
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if (this.isAllComponentsAvailables() == true) {
			File onlyLogosTemplateFile = null;

			try {
				onlyLogosTemplateFile = this.baseStorageDirectory
						.copyBaseTemplateToDirectory();
			} catch (OMRGraderProcessException e) {
				this.alertDialogBuilder
						.setMessage(R.string.no_copied_resources_message_alert_dialog);
				this.alertDialogBuilder
						.setTitle(R.string.no_copied_resources_title_alert_dialog);
				(this.alertDialogBuilder.create()).show();

				return;
			}

			this.baseStorageDirectory.getStorageDirectoriesFilesMap()
					.put(BaseStorageDirectory.ONLY_LOGOS_PATH,
							onlyLogosTemplateFile);

			Log.i(TAG, "Starting Activity for Grading Sessions for Exams");

			super.startActivity(new Intent(super.getApplicationContext(),
					MainSessionActivity.class));
			super.finish();
		}
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.alertDialogBuilder = new AlertDialog.Builder(this);
		this.alertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						StarterConfigurationActivity.super.finish();
					}
				});
	}

	private boolean isAllComponentsAvailables() {
		Log.v(TAG, "isAllComponentsAvailables():boolean");

		if (isLibrariesLoaded == false) {
			Log.w(TAG, "All libraries required have not been loaded.");

			this.alertDialogBuilder
					.setMessage(R.string.no_found_libraries_message_alert_dialog);
			this.alertDialogBuilder
					.setTitle(R.string.no_found_libraries_title_alert_dialog);
			(this.alertDialogBuilder.create()).show();

			return (false);
		}

		boolean isCameraOk = (new CameraValidator(super.getApplicationContext()))
				.isCameraApplicationInstalled();
		if (isCameraOk == false) {
			Log.w(TAG, "There is not any available Camera Application.");

			this.alertDialogBuilder
					.setMessage(R.string.no_cameras_message_alert_dialog);
			this.alertDialogBuilder
					.setTitle(R.string.no_cameras_title_alert_dialog);
			(this.alertDialogBuilder.create()).show();

			return (false);
		}

		this.baseStorageDirectory = BaseStorageDirectory.getInstance(this
				.getApplicationContext());
		boolean isDirectoriesOk = !baseStorageDirectory
				.getStorageDirectoriesFilesMap().isEmpty();
		if (isDirectoriesOk == false) {
			Log.w(TAG,
					"All needed directories and files were not created successfully.");

			this.alertDialogBuilder
					.setMessage(R.string.no_created_directories_message_alert_dialog);
			this.alertDialogBuilder
					.setTitle(R.string.no_created_directories_title_alert_dialog);
			(this.alertDialogBuilder.create()).show();

			return (false);
		}

		return (true);
	}
}