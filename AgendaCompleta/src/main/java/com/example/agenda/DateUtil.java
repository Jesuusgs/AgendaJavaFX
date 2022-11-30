package com.example.agenda;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    //este será el patrón que seguirá la fecha cuando sea convertida
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String format(LocalDate date) {

        //mientras la fecha que se pasa por parámetro sea distinta de null, formateará la fecha siguiendo el patrón de DATE_PATTERN
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            //SI NO SEPUEDE CONVERTIR, RETORNA NULL
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        // Comprueba si la fecha tiene el formato correcto
        return DateUtil.parse(dateString) != null;
    }
}
