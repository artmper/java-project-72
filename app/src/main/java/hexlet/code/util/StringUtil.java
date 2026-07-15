package hexlet.code.util;

public class StringUtil {
    private static final int LIMIT = 200;

    public static String limitText(String text) {
        if (text == null) {
            return null;
        }

        if (text.length() <= LIMIT) {
            return text;
        }

        return text.substring(0, LIMIT) + "...";
    }
}
