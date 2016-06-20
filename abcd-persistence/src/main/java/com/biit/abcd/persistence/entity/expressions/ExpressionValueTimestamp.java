package com.biit.abcd.persistence.entity.expressions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.date.DateManager;

/**
 * Defines a value as a timestamp.
 */
@Entity
@Table(name = "expression_value_timestamp")
public class ExpressionValueTimestamp extends ExpressionValue<Timestamp> {
	private static final long serialVersionUID = -5688942686213064713L;
	private Timestamp value;
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	protected ExpressionValueTimestamp() {
		super();
	}

	public ExpressionValueTimestamp(String value) throws ParseException {
		super();
		Date date = getFormatter().parse(value);
		setValue(new Timestamp(date.getTime()));
	}

	public ExpressionValueTimestamp(Timestamp value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	@Override
	public Timestamp getValue() {
		return value;
	}

	@Override
	public void setValue(Timestamp value) {
		this.value = value;
	}

	@Override
	public String getExpression() {
		if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueTimestamp) {
			super.copyData(object);
			ExpressionValueTimestamp expressionValueTimestamp = (ExpressionValueTimestamp) object;
			this.setValue(expressionValueTimestamp.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueTimestamp.");
		}
	}

	public static SimpleDateFormat getFormatter() {
		return new SimpleDateFormat(DATE_FORMAT);
	}
}
