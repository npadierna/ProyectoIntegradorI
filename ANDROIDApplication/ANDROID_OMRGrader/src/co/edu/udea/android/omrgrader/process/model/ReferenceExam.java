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
public class ReferenceExam extends AbstractExam implements Parcelable {

	private static final long serialVersionUID = 1820239615586789746L;

	public ReferenceExam(Parcel parcel) {
		super.setPictureAbsolutePath(parcel.readString());

		List<QuestionItem> questionsItemsList = new ArrayList<QuestionItem>();
		parcel.readTypedList(questionsItemsList, QuestionItem.CREATOR);

		super.setQuestionsItemsList(questionsItemsList);
	}

	public ReferenceExam(String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList) {
		super(pictureAbsolutePath, questionsItemsList);
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(super.getPictureAbsolutePath());
		dest.writeTypedList(super.getQuestionsItemsList());
	}

	public static final Parcelable.Creator<ReferenceExam> CREATOR = new Parcelable.Creator<ReferenceExam>() {

		@Override()
		public ReferenceExam createFromParcel(Parcel source) {

			return (new ReferenceExam(source));
		}

		@Override()
		public ReferenceExam[] newArray(int size) {

			return (new ReferenceExam[size]);
		}
	};
}