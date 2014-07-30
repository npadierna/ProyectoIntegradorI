package co.edu.udea.android.omrgrader.process.exam.thread;

import java.util.List;

import org.opencv.core.Point;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader.imageprocess.OMRGraderProcess;
import co.edu.udea.android.omrgrader.imageprocess.model.Exam;
import co.edu.udea.android.omrgrader.process.model.AbstractExam;
import co.edu.udea.android.omrgrader.process.model.ReferenceExam;
import co.edu.udea.android.omrgrader.process.model.StudentExam;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamHelperAsyncTask extends AsyncTask<Object, Void, AbstractExam> {

	private static Exam onlyLogosTemplateExam = null;

	private ProgressDialog progressDialog;

	public ExamHelperAsyncTask(ProgressDialog progressDialog) {
		super();

		this.progressDialog = progressDialog;
	}

	@Override()
	@SuppressWarnings(value = { "unchecked" })
	protected AbstractExam doInBackground(Object... args) {
		if (this.checkValidityParameters(args) == false) {

			return (null);
		}

		String onlyLogosTemplatePictureAsolutePath = (String) args[0];
		String examAbsolutePath = (String) args[1];
		List<Point> bubblesCentersPointsList = (List<Point>) args[2];
		Integer questionsOptionsAmout = (Integer) args[3];
		Integer bubbleRadius = (Integer) args[4];
		Integer thresh = (Integer) args[5];
		Boolean isReferenceExam = (Boolean) args[6];

		OMRGraderProcess omrGraderProcess = new OMRGraderProcess(
				bubblesCentersPointsList, questionsOptionsAmout, bubbleRadius,
				thresh);

		if (onlyLogosTemplateExam == null) {
			onlyLogosTemplateExam = omrGraderProcess
					.computeDescriptorsKeyPoints(onlyLogosTemplatePictureAsolutePath);
		}

		Exam exam = omrGraderProcess
				.computeDescriptorsKeyPoints(examAbsolutePath);

		AbstractExam abstractExam = (isReferenceExam.booleanValue()) ? new ReferenceExam(
				examAbsolutePath, null) : new StudentExam(null,
				examAbsolutePath, null);
		abstractExam.setQuestionsItemsList(omrGraderProcess
				.executeProcessing(onlyLogosTemplateExam, exam,
						(String) args[7], (String) args[8]));

		return (abstractExam);
	}

	@Override()
	protected void onCancelled() {
		super.onCancelled();

		if ((this.progressDialog != null) && (this.progressDialog.isShowing())) {
			this.progressDialog.dismiss();
		}
	}

	@Override()
	protected void onPostExecute(AbstractExam result) {
		super.onPostExecute(result);

		if ((this.progressDialog != null) && (this.progressDialog.isShowing())) {
			this.progressDialog.dismiss();
		}
	}

	@Override()
	protected void onPreExecute() {
		super.onPreExecute();

		if ((this.progressDialog != null) && (!this.progressDialog.isShowing())) {
			this.progressDialog.show();
		}
	}

	private boolean checkValidityParameters(Object... args) {
		if ((args == null) || (args.length != 9)) {

			return (false);
		}

		if ((args[0] == null) || !(args[0] instanceof String)
				|| (TextUtils.isEmpty((String) args[0]))
				|| (TextUtils.isEmpty(((String) args[0]).trim()))) {

			return (false);
		}

		if ((args[1] == null) || !(args[1] instanceof String)
				|| (TextUtils.isEmpty((String) args[1]))
				|| (TextUtils.isEmpty(((String) args[1]).trim()))) {

			return (false);
		}

		if ((args[2] == null) || !(args[2] instanceof List)
				|| ((List<?>) args[2]).isEmpty()) {

			return (false);
		}

		for (int pos = 3; pos < 6; pos++) {
			if ((args[pos] == null) || !(args[pos] instanceof Integer)
					|| (((Integer) args[pos]).intValue() < 1)) {

				return (false);
			}
		}

		if ((args[6] == null) || !(args[6] instanceof Boolean)) {

			return (false);
		}

		return (true);
	}
}