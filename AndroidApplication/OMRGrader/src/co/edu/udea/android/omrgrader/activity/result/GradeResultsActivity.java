package co.edu.udea.android.omrgrader.activity.result;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.activity.session.GradeSessionActivity;
import co.edu.udea.android.omrgrader.asynctask.ProcessorAsyncTask;
import co.edu.udea.android.omrgrader.model.GraderSession;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GradeResultsActivity extends Activity {

	private static final String TAG = GradeResultsActivity.class
			.getSimpleName();

	private GraderSession currentSession;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_grade_results);

		this.extractBundleExtra(super.getIntent());
		this.createComponents();
	}

	private void createComponents() {
		Log.i(GradeResultsActivity.TAG, "createComponents() method.");

	}

	private void extractBundleExtra(Intent intent) {
		Log.i(GradeResultsActivity.TAG,
				"extractBundleExtra(Intent intent) method.");

		this.currentSession = intent.getExtras().getParcelable(
				GradeSessionActivity.CURRENT_GRADER_SESSION);

		// DEBUGME: IMPORTANT!!!!

		ProcessorAsyncTask processorAsyncTask = new ProcessorAsyncTask(
				new ProgressDialog(super.getApplicationContext()));
		processorAsyncTask.execute(
				this.currentSession.getReferencePicturePath(),
				this.currentSession.getStudentsPicturesPath().get(0));
		try {
			processorAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}