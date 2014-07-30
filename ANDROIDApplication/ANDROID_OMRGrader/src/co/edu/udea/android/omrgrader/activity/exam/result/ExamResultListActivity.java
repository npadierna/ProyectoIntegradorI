package co.edu.udea.android.omrgrader.activity.exam.result;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.TextView;
import co.edu.udea.android.omrgrader.R;
import co.edu.udea.android.omrgrader.process.exam.ExamComparator;
import co.edu.udea.android.omrgrader.process.exam.ExamGraderSession;
import co.edu.udea.android.omrgrader.process.exam.model.ExamQuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamResultListActivity extends ListActivity {

	private static final String TAG = ExamResultListActivity.class
			.getSimpleName();

	public static final String EXAM_GRADER_SESSION_KEY = "Key for ExamGraderSession Object";

	private ExamComparator examComparator;
	private ExamGraderSession examGraderSession;
	private List<ExamQuestionItem> examQuestionsItemsList;

	private AlertDialog.Builder errorAlertDialogBuilder;
	private TextView correctAnswersAmoutTextView;
	private TextView finalGradeValueTextView;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.list_activity_exam_result);

		this.extractValuesFromBundle(super.getIntent().getExtras());
		this.createComponents();
		this.createWidgetsComponents();
		this.showExamResults();
	}

	private void createComponents() {
		Log.v(TAG, "createComponents():void");

		this.examComparator = new ExamComparator(super.getApplicationContext(),
				this.examGraderSession);

		this.examQuestionsItemsList = this.examComparator
				.compareAnswers(this.examGraderSession.getGraderSession()
						.getStudentsExamsList().get(0));
	}

	private void createWidgetsComponents() {
		Log.v(TAG, "createWidgetsComponents():void");

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder.setPositiveButton(R.string.accept_button,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						ExamResultListActivity.super.finish();
					}
				});

		this.correctAnswersAmoutTextView = (TextView) super
				.findViewById(R.id.correct_answers_amount_text_view);

		this.finalGradeValueTextView = (TextView) super
				.findViewById(R.id.final_grade_value_text_view);

		ListAdapter referenceExamListAdapter = new ExamQuestionItemArrayAdapter(
				this, R.layout.item_list_question_item,
				this.examQuestionsItemsList);

		super.setListAdapter(referenceExamListAdapter);
	}

	private void extractValuesFromBundle(Bundle bundle) {
		Log.v(TAG, "extractValuesFromBundle(Bundle):void");

		if ((bundle != null) && (bundle.containsKey(EXAM_GRADER_SESSION_KEY))) {
			this.examGraderSession = bundle
					.getParcelable(EXAM_GRADER_SESSION_KEY);
		}

		if (this.examGraderSession == null) {
			this.errorAlertDialogBuilder
					.setMessage(R.string.no_exam_grader_session_found_message_alert_dialog);
			this.errorAlertDialogBuilder
					.setTitle(R.string.no_exam_grader_session_found_message_title_dialog);
			(this.errorAlertDialogBuilder.create()).show();

			return;
		}
	}

	private void showExamResults() {
		Log.v(TAG, "showExamResults(Bundle):void");

		int correctQuestionsAmout = this.examComparator
				.countCorrectAnswers(this.examQuestionsItemsList);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getApplicationContext());
		float maximumGrade = Float
				.valueOf(sharedPreferences.getString(
						super.getString(R.string.grader_settings_preference_maximum_grade_key),
						String.valueOf(super
								.getString(R.string.grader_settings_preference_default_maximum_grade))));

		Log.i(TAG, String.format("Maximum Grade: %f", maximumGrade));

		int totalQuestionsAmout = super.getResources().getInteger(
				R.integer.questions_items_columns_amount)
				* (super.getResources()
						.getIntArray(R.array.bubbles_y_coordinates)).length;
		float finalGrade = (float) ((maximumGrade / totalQuestionsAmout) * correctQuestionsAmout);

		this.correctAnswersAmoutTextView.setText(String.format("%d / %d",
				correctQuestionsAmout, totalQuestionsAmout));
		this.finalGradeValueTextView.setText(String.format("%.2f", finalGrade));
	}
}