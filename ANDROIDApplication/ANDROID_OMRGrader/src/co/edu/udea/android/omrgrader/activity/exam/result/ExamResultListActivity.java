package co.edu.udea.android.omrgrader.activity.exam.result;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import co.edu.udea.android.omrgrader.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamResultListActivity extends ListActivity {

	private static final String TAG = ExamResultListActivity.class
			.getSimpleName();

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.list_activity_exam_result);

		this.createWidgetsComponents();
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponets()");
	}
}