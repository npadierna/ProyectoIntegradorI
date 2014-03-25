package co.edu.udea.android.omrgrader.asynctask;

import java.util.List;

import co.edu.udea.android.omrgrader.imageprocess.ImageProcessor;
import co.edu.udea.android.omrgrader.model.QuestionItem;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ProcessorAsyncTask extends
		AsyncTask<String, Void, List<QuestionItem>> {

	private ProgressDialog progressDialog;

	public ProcessorAsyncTask(ProgressDialog progressDialog) {
		super();

		this.progressDialog = progressDialog;
	}

	@Override()
	protected List<QuestionItem> doInBackground(String... args) {
		if ((args != null) && (args.length == 2) && (args[0] instanceof String)
				&& (args[1] instanceof String)) {

			return (ImageProcessor.getInstance().executeProcessing(
					(String) args[0], (String) args[1]));
		}

		return (null);
	}

	@Override()
	protected void onCancelled() {
		super.onCancelled();

		this.progressDialog.dismiss();
	}

	@Override()
	protected void onPostExecute(List<QuestionItem> result) {
		super.onPostExecute(result);

		this.progressDialog.dismiss();
	}

	@Override()
	protected void onPreExecute() {
		super.onPreExecute();

		this.progressDialog.show();
	}
}