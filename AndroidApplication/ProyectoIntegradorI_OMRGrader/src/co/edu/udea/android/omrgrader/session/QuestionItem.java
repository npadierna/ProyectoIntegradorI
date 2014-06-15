package co.edu.udea.android.omrgrader.session;

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

	private static final long serialVersionUID = 1L;

	private boolean choises[];
	private short id;

	public QuestionItem() {
		this((short) 0, null);
	}

	public QuestionItem(short id, boolean[] choises) {
		super();

		this.setChoises(choises);
		this.setId(id);
	}

	public QuestionItem(Parcel parcel) {
		// DEBUGME: How does Parcel manage the Booleans Array?
		boolean booleans[] = {};

		parcel.readBooleanArray(booleans);
		this.setChoises(booleans);

		this.setId((short) parcel.readInt());
	}

	public boolean[] getChoises() {

		return (this.choises);
	}

	public void setChoises(boolean[] choises) {
		this.choises = choises;
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
		dest.writeBooleanArray(this.getChoises());
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