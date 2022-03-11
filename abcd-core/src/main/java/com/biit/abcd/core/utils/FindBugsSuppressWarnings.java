package com.biit.abcd.core.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Suppress FindBugs warnings on the annotated element. FindBugs will recognize
 * any annotation that has class retention and whose name ends with
 * "SuppressWarnings".
 *
 * @hide
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(CLASS)
public @interface FindBugsSuppressWarnings {
    /**
     * The <a href="http://findbugs.sourceforge.net/bugDescriptions.html">FindBugs
     * Patterns</a> to suppress, such as {@code SE_TRANSIENT_FIELD_NOT_RESTORED}
     * or {@code Se}. Full, upper case names are preferred.
     */
    String[] value();
}