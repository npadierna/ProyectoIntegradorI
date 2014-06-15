package co.edu.udea.android.omrgrader.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public final class OMRExam implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;

	private String idNumber;
	private String pictureRoutePath;
	private List<QuestionItem> questionsItemsList;

	public OMRExam(String idNumber, String pictureRoutePath,
			List<QuestionItem> questionsItemsList) {
		this.setIdNumber(idNumber);
		this.setPictureRoutePath(pictureRoutePath);
		this.setQuestionsItemsList(questionsItemsList);
	}

	public OMRExam(Parcel parcel) {
		// DEBUGME: How does the parcel map these values?

		this.setIdNumber(parcel.readString());
		this.setPictureRoutePath(parcel.readString());

		List<QuestionItem> questionsItemsList = new ArrayList<QuestionItem>();
		parcel.readList(questionsItemsList, QuestionItem.class.getClassLoader());

		this.setQuestionsItemsList(questionsItemsList);
	}

	public String getIdNumber() {

		return (this.idNumber);
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getPictureRoutePath() {

		return (this.pictureRoutePath);
	}

	public void setPictureRoutePath(String pictureRoutePath) {
		this.pictureRoutePath = pictureRoutePath;
	}

	public List<QuestionItem> getQuestionsItemsList() {

		return (this.questionsItemsList);
	}

	public void setQuestionsItemsList(List<QuestionItem> questionsItemsList) {
		this.questionsItemsList = questionsItemsList;
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getIdNumber());
		dest.writeString(this.getPictureRoutePath());
		dest.writeList(this.getQuestionsItemsList());
	}

	public static final Parcelable.Creator<OMRExam> CREATOR = new Parcelable.Creator<OMRExam>() {

		@Override()
		public OMRExam createFromParcel(Parcel source) {

			return (new OMRExam(source));
		}

		@Override()
		public OMRExam[] newArray(int size) {

			return (new OMRExam[size]);
		}
	};
}