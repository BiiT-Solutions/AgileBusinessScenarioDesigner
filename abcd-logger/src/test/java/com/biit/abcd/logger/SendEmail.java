package com.biit.abcd.logger;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Logger)
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

import org.testng.annotations.Test;

@Test(groups = {"sendEmail"})
public class SendEmail {

    @Test
    public void checkLoggerName() {
        AbcdLogger.info(SendEmail.class.getName(), "Initializing the logger... ");
        System.out.println(" ----------------------- EXPECTED ERROR ------------------------");
        AbcdLogger.errorMessage(SendEmail.class.getName(), new Exception(
                "Catastrophic Error: Godzilla is eating all your code!"));
        System.out.println(" --------------------- END EXPECTED ERROR ----------------------");
    }
}
