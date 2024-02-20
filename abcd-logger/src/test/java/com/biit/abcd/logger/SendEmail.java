package com.biit.abcd.logger;

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
