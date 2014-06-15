package co.edu.udea.android.omrgrader.session;

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

	private static final long serialVersionUID = 1L;

	private OMRExam templateReferenceOMRExam;
	private List<OMRExam> studentsOMRExamList;

	public GraderSession(OMRExam templateReferenceOMRExam,
			List<OMRExam> studentsOMRExamList) {
		this.setTemplateReferenceOMRExam(templateReferenceOMRExam);
		this.setStudentsOMRExamList(studentsOMRExamList);
	}

	public GraderSession(Parcel parcel) {
		this.setTemplateReferenceOMRExam((OMRExam) parcel
				.readParcelable(OMRExam.class.getClassLoader()));

		List<OMRExam> studentsOMRExamList = new ArrayList<OMRExam>();
		parcel.readList(studentsOMRExamList, OMRExam.class.getClassLoader());

		this.setStudentsOMRExamList(studentsOMRExamList);
	}

	public OMRExam getTemplateReferenceOMRExam() {

		return (this.templateReferenceOMRExam);
	}

	public void setTemplateReferenceOMRExam(OMRExam templateReferenceOMRExam) {
		this.templateReferenceOMRExam = templateReferenceOMRExam;
	}

	public List<OMRExam> getStudentsOMRExamList() {

		return (this.studentsOMRExamList);
	}

	public void setStudentsOMRExamList(List<OMRExam> studentsOMRExamList) {
		this.studentsOMRExamList = studentsOMRExamList;
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.getTemplateReferenceOMRExam(), 0);
		dest.writeList(this.getStudentsOMRExamList());
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