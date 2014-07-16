package co.edu.udea.android.omrgrader.process.config;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class CameraValidator {

	private static final String TAG = CameraValidator.class.getSimpleName();

	private Context context;

	public CameraValidator(Context context) {
		super();

		this.setContext(context);
	}

	public Context getContext() {

		return (this.context);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isCameraApplicationInstalled() {
		Log.v(TAG, "isCameraApplicationInstalled():boolean");

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> resolveInfoList = this
				.getContext()
				.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);

		return (!resolveInfoList.isEmpty());
	}
}