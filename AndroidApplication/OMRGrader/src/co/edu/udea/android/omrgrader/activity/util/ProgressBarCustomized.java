package co.edu.udea.android.omrgrader.activity.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ProgressBarCustomized {

	private Activity activity;

	public ProgressBarCustomized(Activity activity) {
		super();
		this.setActivity(activity);
	}

	public Activity getActivity() {

		return (this.activity);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ProgressDialog createProgressDialog(String title, String message,
			boolean isCancelable) {
		ProgressDialog progressDialog = new ProgressDialog(this.getActivity());

		progressDialog.setCancelable(isCancelable);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);

		return (progressDialog);
	}
}