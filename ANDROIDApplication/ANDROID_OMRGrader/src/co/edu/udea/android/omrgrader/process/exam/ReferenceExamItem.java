package co.edu.udea.android.omrgrader.process.exam;

import java.util.Date;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ReferenceExamItem {

	private Date lastModifiedDate;
	private String fullName;

	public ReferenceExamItem(Date lastModifiedDate, String fullName) {
		super();

		this.setLastModifiedDate(lastModifiedDate);
		this.setFullName(fullName);
	}

	public Date getLastModifiedDate() {

		return (this.lastModifiedDate);
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getFullName() {

		return (this.fullName);
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}