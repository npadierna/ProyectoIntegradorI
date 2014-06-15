package co.edu.udea.android.omrgrader.activity.exam;

final class FileListItem {

	private String creationDate;
	private String fullName;

	public FileListItem(String creationDate, String fullName) {
		super();

		this.setCreationDate(creationDate);
		this.setFullName(fullName);
	}

	public String getCreationDate() {

		return (this.creationDate);
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getFullName() {

		return (this.fullName);
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}