package co.edu.udea.android.omrgrader.process.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class GraderSession implements Parcelable, Serializable {

	private static final long serialVersionUID = -5418906090820115625L;

	private List<StudentExam> studentsExamsList;
	private ReferenceExam referenceExam;

	public GraderSession(Parcel parcel) {
		this.setReferenceExam((ReferenceExam) parcel
				.readParcelable(ReferenceExam.class.getClassLoader()));

		List<StudentExam> studentsExamsList = new ArrayList<StudentExam>();
		parcel.readTypedList(studentsExamsList, StudentExam.CREATOR);
		this.setStudentsExamsList(studentsExamsList);
	}

	public GraderSession(String referenceExamAsolutePath) {
		this(new ReferenceExam(referenceExamAsolutePath, null),
				new ArrayList<StudentExam>());
	}

	public GraderSession(ReferenceExam referenceExam,
			List<StudentExam> studentsExamsList) {
		super();

		this.setReferenceExam(referenceExam);
		this.setStudentsExamsList(studentsExamsList);
	}

	public List<StudentExam> getStudentsExamsList() {

		return (this.studentsExamsList);
	}

	public void setStudentsExamsList(List<StudentExam> studentsExamsList) {
		this.studentsExamsList = studentsExamsList;
	}

	public ReferenceExam getReferenceExam() {

		return (this.referenceExam);
	}

	public void setReferenceExam(ReferenceExam referenceExam) {
		this.referenceExam = referenceExam;
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.getReferenceExam(), 0);
		dest.writeTypedList(this.getStudentsExamsList());
	}

	public static final Parcelable.Creator<GraderSession> CREATOR = new Parcelable.Creator<GraderSession>() {

		@Override()
		public GraderSession createFromParcel(Parcel source) {

			return (new GraderSession(source));
		}

		@Override()
		public GraderSession[] newArray(int size) {

			return (new GraderSession[size]);
		}
	};
}