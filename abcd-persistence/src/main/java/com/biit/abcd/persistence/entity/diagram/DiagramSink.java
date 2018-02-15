package com.biit.abcd.persistence.entity.diagram;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "diagram_sink")
public class DiagramSink extends DiagramExpression {
	private static final long serialVersionUID = 1993423029316963730L;

	public DiagramSink() {
		super();
		DiagramText biitText = new DiagramText();
		biitText.setText("End");
		setText(biitText);
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DiagramSink) {
			super.copyData(object);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of DiagramSink.");
		}
	}

}