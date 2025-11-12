package com.biit.abcd.language;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.entity.AnswerType;

public enum AnswerTypeUi {

	RADIO(AnswerType.RADIO, LanguageCodes.ANSWER_TYPE_RADIO),

	MULTI_CHECKBOX(AnswerType.MULTI_CHECKBOX, LanguageCodes.MULTI_CHECKBOX),

	// Uses AnswerFormat.
	INPUT(AnswerType.INPUT, LanguageCodes.ANSWER_TYPE_INPUT);

	private AnswerType answerType;
	private LanguageCodes languageCode;

	private AnswerTypeUi(AnswerType answerType, LanguageCodes languageCode) {
		this.answerType = answerType;
		this.languageCode = languageCode;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}

	public static AnswerTypeUi getFromAnswerType(AnswerType answerType) {
		for (AnswerTypeUi answerTypeUi : values()) {
			if (answerTypeUi.getAnswerType().equals(answerType)) {
				return answerTypeUi;
			}
		}
		return null;
	}
}
