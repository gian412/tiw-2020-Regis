package it.polimi.tiw.crowdsourcing.utils;

import java.util.regex.Pattern;

public final class Email {

    private static String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isValid(String email) {

        Pattern pat = Pattern.compile(emailRegex);

        if (email == null) {
            return false;
        }

        return pat.matcher(email).matches();

    }

}
