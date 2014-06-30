package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.Question;

@Entity
@Table(name = "DIAGRAM_FORK")
public class DiagramFork extends DiagramElement {

	@ManyToOne(fetch = FetchType.EAGER)
	private Question question;
	
	public DiagramFork() {
		super();
		DiagramBiitText biitText = new DiagramBiitText();
		biitText.setText("Fork");
		setBiitText(biitText);
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}