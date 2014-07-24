package co.edu.udea.android.omrgrader.process.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class StudentExam extends AbstractExam implements Parcelable {

	private static final long serialVersionUID = 5473039490111890206L;

	private float grade;

	private String idNumber;

	public StudentExam(Parcel parcel) {
		this.setGrade(parcel.readFloat());
		this.setIdNumber(parcel.readString());
		super.setPictureAbsolutePath(parcel.readString());

		List<QuestionItem> questionsItemsList = new ArrayList<QuestionItem>();
		parcel.readTypedList(questionsItemsList, QuestionItem.CREATOR);

		super.setQuestionsItemsList(questionsItemsList);
	}

	public StudentExam(String idNumber, String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList) {
		this(idNumber, pictureAbsolutePath, questionsItemsList, -1.0F);
	}

	public StudentExam(String idNumber, String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList, float grade) {
		super(pictureAbsolutePath, questionsItemsList);

		this.setIdNumber(idNumber);
		this.setGrade(grade);
	}

	public float getGrade() {

		return (this.grade);
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public String getIdNumber() {

		return (this.idNumber);
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(this.getGrade());
		dest.writeString(this.getIdNumber());
		dest.writeString(super.getPictureAbsolutePath());
		dest.writeTypedList(super.getQuestionsItemsList());
	}

	public static final Parcelable.Creator<StudentExam> CREATOR = new Parcelable.Creator<StudentExam>() {

		@Override()
		public StudentExam createFromParcel(Parcel source) {

			return (new StudentExam(source));
		}

		@Override()
		public StudentExam[] newArray(int size) {

			return (new StudentExam[size]);
		}
	};
}