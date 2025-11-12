package com.biit.abcd.serialization;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.form.jackson.serialization.BaseStorableObjectDeserializer;
import com.biit.form.jackson.serialization.BaseStorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class SimpleFormViewWithContentSerializer extends BaseStorableObjectSerializer<SimpleFormViewWithContent> {

    @Override
    public void serialize(SimpleFormViewWithContent src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getName() != null) {
            jgen.writeStringField("name", src.getName());
        }
        if (src.getLabel() != null) {
            jgen.writeStringField("label", src.getLabel());
        }
        if (src.getVersion() != null) {
            jgen.writeNumberField("version", src.getVersion());
        }
        if (src.getOrganizationId() != null) {
            jgen.writeNumberField("organizationId", src.getOrganizationId());
        }
        if (src.getAvailableFrom() != null) {
            jgen.writeStringField("availableFrom", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableFrom().toLocalDateTime()));
        }
        if (src.getAvailableTo() != null) {
            jgen.writeStringField("availableTo", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableTo().toLocalDateTime()));
        }
        if (src.getStatus() != null) {
            jgen.writeStringField("status", src.getStatus().name());
        }
        if (src.getJson() != null) {
            jgen.writeStringField("json", src.getJson());
        }
    }
}
