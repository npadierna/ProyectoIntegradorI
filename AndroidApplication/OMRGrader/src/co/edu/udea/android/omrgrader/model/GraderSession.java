package co.edu.udea.android.omrgrader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSession implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;

	private short studentsTestsAmount;
	private String referencePicturePath;
	private String sessionName;
	private List<String> studentsPicturesPath;
	private Map<String, List<QuestionItem>> questionItemsMap;

	public GraderSession(short studentsTestsAmount, String sessionName) {
		super();

		this.setReferencePicturePath(null);
		this.setSessionName(sessionName);
		this.setStudentsPicturesPath(new ArrayList<String>());
		this.setStudentsTestsAmount(studentsTestsAmount);
		this.setQuestionItemMap(new HashMap<String, List<QuestionItem>>());
	}

	public GraderSession(Parcel parcel) {
		this.setStudentsTestsAmount((short) parcel.readInt());
		this.setReferencePicturePath(parcel.readString());
		this.setSessionName(parcel.readString());

		List<String> list = new ArrayList<String>();
		parcel.readStringList(list);

		this.setStudentsPicturesPath(list);

		// DEBUGME: How does Parcel for reading Maps?
		Map<String, List<QuestionItem>> map = new HashMap<String, List<QuestionItem>>();
		parcel.readMap(map, QuestionItem.class.getClassLoader());

		this.setQuestionItemMap(map);
	}

	public short getStudentsTestsAmount() {

		return (this.studentsTestsAmount);
	}

	public void setStudentsTestsAmount(short studentsTestAmount) {
		this.studentsTestsAmount = studentsTestAmount;
	}

	public String getReferencePicturePath() {

		return (this.referencePicturePath);
	}

	public void setReferencePicturePath(String referencePicturePath) {
		this.referencePicturePath = referencePicturePath;
	}

	public String getSessionName() {

		return (this.sessionName);
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public List<String> getStudentsPicturesPath() {

		return (this.studentsPicturesPath);
	}

	public void setStudentsPicturesPath(List<String> studentsPicturesPath) {
		this.studentsPicturesPath = studentsPicturesPath;
	}

	public Map<String, List<QuestionItem>> getQuestionItemMap() {

		return (this.questionItemsMap);
	}

	public void setQuestionItemMap(
			Map<String, List<QuestionItem>> questionItemMap) {
		this.questionItemsMap = questionItemMap;
	}

	public void addNewStudentsPicturePath(String newStudentsPicutrePath) {
		this.getStudentsPicturesPath().add(newStudentsPicutrePath);
	}

	public int studentsTestTaken() {

		return (this.getStudentsPicturesPath().size());
	}

	public boolean isEnd() {

		return (this.getStudentsTestsAmount() == this.getStudentsPicturesPath()
				.size());
	}

	@Override()
	public int describeContents() {

		return (0);
	}

	@Override()
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getStudentsTestsAmount());
		dest.writeString(this.getReferencePicturePath());
		dest.writeString(this.getSessionName());
		dest.writeStringList(this.getStudentsPicturesPath());
		dest.writeMap(this.getQuestionItemMap());
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