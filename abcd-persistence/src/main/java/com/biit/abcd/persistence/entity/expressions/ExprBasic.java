package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;

@Entity
@Table(name = "EXPRESSION_BASIC")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExprBasic extends StorableObject {

	@ManyToOne(fetch = FetchType.EAGER)
	protected ExprBasic parent;

	public abstract String getExpressionTableString();

	public ExprBasic getParent() {
		return parent;
	}

	protected ExprBasic getParentOfClass(Class<?> clazz) {
		if (parent == null) {
			return null;
		}
		if (clazz.isAssignableFrom(parent.getClass())) {
			return parent;
		}
		return parent.getParentOfClass(clazz);
	}

	protected String generateNullLabelCaption(String value) {
		return "<div style=\"background-color: rgb(179, 46, 46); color: rgb(255,255,255); display: inline;\">" + value
				+ "</div>";
	}

	@Override
	public String toString() {
		return getExpressionTableString();
	}
}
