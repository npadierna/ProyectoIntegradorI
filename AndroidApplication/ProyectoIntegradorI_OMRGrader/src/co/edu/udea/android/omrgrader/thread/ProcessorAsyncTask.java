package co.edu.udea.android.omrgrader.thread;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import co.edu.udea.android.omrgrader.imageprocess.ImageProcessor;
import co.edu.udea.android.omrgrader.session.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ProcessorAsyncTask extends
		AsyncTask<String, Void, List<QuestionItem>> {

	private ProgressDialog progressDialog;

	public ProcessorAsyncTask(ProgressDialog progressDialog) {
		super();

		this.progressDialog = progressDialog;
	}

	@Override()
	protected List<QuestionItem> doInBackground(String... args) {
		if ((args != null) && (args.length == 4)) {
			for (String s : args) {
				if (!(s instanceof String)) {

					return (null);
				}
			}

			return (new ImageProcessor().executeProcessing((String) args[0],
					(String) args[1], (String) args[2], (String) args[3]));
		}

		return (null);
	}

	@Override()
	protected void onCancelled() {
		super.onCancelled();

		// this.progressDialog.dismiss();
	}

	@Override()
	protected void onPostExecute(List<QuestionItem> result) {
		super.onPostExecute(result);

		// this.progressDialog.dismiss();
	}

	@Override()
	protected void onPreExecute() {
		super.onPreExecute();

		// this.progressDialog.show();
	}
}