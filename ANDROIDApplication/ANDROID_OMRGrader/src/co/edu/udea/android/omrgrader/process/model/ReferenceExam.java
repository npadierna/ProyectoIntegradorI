package co.edu.udea.android.omrgrader.process.model;

import java.util.List;

import co.edu.udea.android.omrgrader.imageprocess.model.QuestionItem;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ReferenceExam extends AbstractExam {

	private static final long serialVersionUID = 1820239615586789746L;

	public ReferenceExam(String pictureAbsolutePath,
			List<QuestionItem> questionsItemsList) {
		super(pictureAbsolutePath, questionsItemsList);
	}
}