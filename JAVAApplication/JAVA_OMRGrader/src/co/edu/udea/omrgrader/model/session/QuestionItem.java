package co.edu.udea.omrgrader.model.session;

public class QuestionItem {

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

	public int describeContents() {

		return (0);
	}
}