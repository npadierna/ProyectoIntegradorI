package co.edu.udea.android.omrgrader.activity.introduction;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.session.GradeSessionActivity;
import co.edu.udea.android.omrgrader.activity.util.AlertDialogCustomized;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class IntroductionActivity extends Activity {

	private static final String TAG = IntroductionActivity.class
			.getSimpleName();

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_introduction);

		this.createComponents();
	}

	public void onStartOMRGrader(View view) {
		Log.i(IntroductionActivity.TAG, "onStartOMRGrader(View view) method.");

		if (this.checkAvailabilityForCamera()) {
			Log.d(IntroductionActivity.TAG,
					"One or more Camera Applications are installed.");

			super.startActivity(new Intent(super.getApplicationContext(),
					GradeSessionActivity.class));
			super.finish();
		} else {
			Log.d(IntroductionActivity.TAG, "No one Camera Application found.");

			AlertDialog.Builder alertDiaBuilder = (new AlertDialogCustomized(
					this)).createAlertDialog(
					super.getString(R.string.no_camera_alert_dialog_title),
					super.getString(R.string.no_camera_alert_dialog_text),
					false);
			alertDiaBuilder.setPositiveButton(
					super.getString(R.string.accept_text),
					new DialogInterface.OnClickListener() {

						@Override()
						public void onClick(DialogInterface dialog, int which) {
							IntroductionActivity.super.finish();
						}
					});
			(alertDiaBuilder.create()).show();
		}
	}

	private boolean checkAvailabilityForCamera() {
		Log.i(IntroductionActivity.TAG,
				"Checking if there is a Camera Application available.");

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> returnedResolveInformation = super
				.getPackageManager().queryIntentActivities(cameraIntent,
						PackageManager.MATCH_DEFAULT_ONLY);

		return (!returnedResolveInformation.isEmpty());
	}

	private void createComponents() {
		Log.i(IntroductionActivity.TAG, "createComponents() method.");
	}
}