package co.edu.udea.android.omrgrader.process.exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader.process.exam.thread.ExamHelperAsyncTask;
import co.edu.udea.android.omrgrader.process.exception.OMRGraderProcessException;
import co.edu.udea.android.omrgrader.process.model.AbstractExam;
import co.edu.udea.android.omrgrader.process.model.GraderSession;
import co.edu.udea.android.omrgrader.process.model.ReferenceExam;
import co.edu.udea.android.omrgrader.process.model.StudentExam;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class ExamGraderSession implements Parcelable, Serializable {

	private static final long serialVersionUID = -8339627345414088591L;

	private int questionsOptionsAmout;
	private int bubbleRadius;
	private int thresh;

	private BaseStorageDirectory baseStorageDirectory;
	private GraderSession graderSession;

	private List<Point> bubblesCentersPointsList;

	public ExamGraderSession(Parcel parcel) {
		this.setQuestionsOptionsAmout(parcel.readInt());
		this.setBubbleRadius(parcel.readInt());
		this.setThresh(parcel.readInt());
		this.setGraderSession((GraderSession) parcel
				.readParcelable(GraderSession.class.getClassLoader()));
	}

	public ExamGraderSession(Context context, String referenceExamAbsolutePath,
			int questionsOptionsAmout, int bubbleRadius, int thresh) {
		super();

		this.baseStorageDirectory = BaseStorageDirectory.getInstance(context);

		this.setQuestionsOptionsAmout(questionsOptionsAmout);
		this.setBubbleRadius(bubbleRadius);
		this.setThresh(thresh);
		this.setGraderSession(new GraderSession(referenceExamAbsolutePath));
	}

	public int getQuestionsOptionsAmout() {

		return (this.questionsOptionsAmout);
	}

	public void setQuestionsOptionsAmout(int questionsOptionsAmout) {
		this.questionsOptionsAmout = questionsOptionsAmout;
	}

	public int getBubbleRadius() {

		return (this.bubbleRadius);
	}

	public void setBubbleRadius(int bubbleRadius) {
		this.bubbleRadius = bubbleRadius;
	}

	public int getThresh() {

		return (this.thresh);
	}

	public void setThresh(int thresh) {
		this.thresh = thresh;
	}

	public GraderSession getGraderSession() {

		return (this.graderSession);
	}

	public void setGraderSession(GraderSession graderSession) {
		this.graderSession = graderSession;
	}

	public List<Point> getBubblesCentersPointsList() {

		return (this.bubblesCentersPointsList);
	}

	public void setBubblesCentersPointsList(List<Point> bubblesCentersPointsList) {
		this.bubblesCentersPointsList = bubblesCentersPointsList;
	}

	public ReferenceExam computeReferenceExam(ProgressDialog progressDialog)
			throws OMRGraderProcessException {
		ReferenceExam referenceExam = (ReferenceExam) this.computeExam(
				progressDialog, this.getGraderSession().getReferenceExam()
						.getPictureAbsolutePath(), Boolean.TRUE);

		this.getGraderSession().setReferenceExam(referenceExam);

		return (referenceExam);
	}

	private AbstractExam computeExam(ProgressDialog progressDialog,
			String examAbsolutePath, Boolean isReferenceExam)
			throws OMRGraderProcessException {
		AsyncTask<Object, Void, AbstractExam> examHelperAsyncTask = new ExamHelperAsyncTask(
				progressDialog);
		String onlyLogosTemplatePictureAsolutePath = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap()
				.get(BaseStorageDirectory.ONLY_LOGOS_PATH).getAbsolutePath();
		String blackAndWithPictureAsolutePath = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap().get("black_white")
				.getAbsolutePath();
		String processedPictureAsolutePath = this.baseStorageDirectory
				.getStorageDirectoriesFilesMap().get("processed")
				.getAbsolutePath();

		try {
			examHelperAsyncTask
					.execute(new Object[] {
							onlyLogosTemplatePictureAsolutePath,
							examAbsolutePath,
							this.getBubblesCentersPointsList(),
							Integer.valueOf(this.getQuestionsOptionsAmout()),
							Integer.valueOf(this.getBubbleRadius()),
							Integer.valueOf(this.getThresh()), isReferenceExam,
							blackAndWithPictureAsolutePath,
							processedPictureAsolutePath });
			AbstractExam abstractExam = examHelperAsyncTask.get();

			return (abstractExam);
		} catch (Exception e) {
			throw new OMRGraderProcessException(
					"A exception has ocurred while the applications was trying to process an exam.",
					e);
		}
	}

	public StudentExam computeStudentExam(ProgressDialog progressDialog,
			String studentExamAbsolutePath) throws OMRGraderProcessException {
		if ((TextUtils.isEmpty(studentExamAbsolutePath))
				|| (TextUtils.isEmpty(studentExamAbsolutePath.trim()))) {
			throw new OMRGraderProcessException(
					"The Absolute Route Path for the new Student Exam is no valid.");
		}

		StudentExam studentExam = (StudentExam) this.computeExam(
				progressDialog, studentExamAbsolutePath, Boolean.FALSE);

		this.getGraderSession().getStudentsExamsList().add(studentExam);

		return (studentExam);
	}

	public List<Point> buildBubblesCenterCoordinates(int[] bubblesXCoordinates,
			int[] bubblesYCoordinates, int questionsItemsColumnsAmout) {
		List<Point> pointsList = new ArrayList<Point>();

		if ((bubblesXCoordinates != null) && (bubblesYCoordinates != null)
				&& (questionsItemsColumnsAmout > 0)) {
			int length = (int) (bubblesXCoordinates.length / questionsItemsColumnsAmout);

			for (int columnCounter = 1; columnCounter <= questionsItemsColumnsAmout; columnCounter++) {
				for (int yCoordinate : bubblesYCoordinates) {
					for (int pos = ((columnCounter - 1) * length); pos < (columnCounter * length); pos++) {
						pointsList.add(new Point(bubblesXCoordinates[pos],
								yCoordinate));
					}
				}
			}
		}

		this.setBubblesCentersPointsList(pointsList);

		return (pointsList);
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getQuestionsOptionsAmout());
		dest.writeInt(this.getBubbleRadius());
		dest.writeInt(this.getThresh());
		dest.writeParcelable(this.getGraderSession(), 0);
	}

	public static final Parcelable.Creator<ExamGraderSession> CREATOR = new Parcelable.Creator<ExamGraderSession>() {

		@Override()
		public ExamGraderSession createFromParcel(Parcel source) {

			return (new ExamGraderSession(source));
		}

		@Override()
		public ExamGraderSession[] newArray(int size) {

			return (new ExamGraderSession[size]);
		}
	};
}