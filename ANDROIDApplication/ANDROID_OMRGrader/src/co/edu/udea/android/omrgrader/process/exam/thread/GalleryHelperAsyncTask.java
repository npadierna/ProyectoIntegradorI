package co.edu.udea.android.omrgrader.process.exam.thread;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

public class GalleryHelperAsyncTask extends AsyncTask<String, Void, File> {

	private static final long TIME_FOR_SLEEPING = 2000L;

	private Context context;

	public GalleryHelperAsyncTask(Context context) {
		this.context = context;
	}

	@Override()
	protected File doInBackground(String... params) {
		if (this.checkValidityParameters(params) == false) {

			return (null);
		}

		try {
			Thread.sleep(TIME_FOR_SLEEPING);
		} catch (InterruptedException e) {
		}

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File((String) params[0]);

		Uri uri = Uri.fromFile(file);
		intent.setData(uri);

		this.context.sendBroadcast(intent);

		return (file);
	}

	private boolean checkValidityParameters(String... args) {
		if ((args == null) || (args.length != 1)) {

			return (false);
		}

		if ((args[0] == null) || (args[0] instanceof String)
				|| (TextUtils.isEmpty((String) args[0]))
				|| (TextUtils.isEmpty(((String) args[0]).trim()))) {

			return (false);
		}

		return (true);
	}
}