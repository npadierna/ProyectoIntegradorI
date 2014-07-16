package co.edu.udea.android.omrgrader.imageprocess.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class QuestionItem implements Parcelable, Serializable {

	private static final long serialVersionUID = 8772507323900628318L;

	private boolean questionsOptionsChosen[];
	private short id;

	public QuestionItem(short id, boolean[] questionsOptionsChosen) {
		super();

		this.setQuestionsOptionsChosen(questionsOptionsChosen);
		this.setId(id);
	}

	public QuestionItem(Parcel parcel) {
		// DEBUGME: How does Parcel manage the Booleans Array?
		boolean booleans[] = new boolean[] {};

		parcel.readBooleanArray(booleans);
		this.setQuestionsOptionsChosen(booleans);

		this.setId((short) parcel.readInt());
	}

	public boolean[] getQuestionsOptionsChosen() {

		return (this.questionsOptionsChosen);
	}

	public void setQuestionsOptionsChosen(boolean[] choises) {
		this.questionsOptionsChosen = choises;
	}

	public short getId() {

		return (this.id);
	}

	public void setId(short id) {
		this.id = id;
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeBooleanArray(this.getQuestionsOptionsChosen());
		dest.writeInt(this.getId());
	}

	public static final Parcelable.Creator<QuestionItem> CREATOR = new Parcelable.Creator<QuestionItem>() {

		@Override()
		public QuestionItem createFromParcel(Parcel source) {

			return (new QuestionItem(source));
		}

		@Override()
		public QuestionItem[] newArray(int size) {

			return (new QuestionItem[size]);
		}
	};
}