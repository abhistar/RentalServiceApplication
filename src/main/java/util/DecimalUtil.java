package util;

import java.text.DecimalFormat;

public class DecimalUtil {
    private static final String decimalFormatString = "#.##";

    private static final DecimalFormat decimalFormat = new DecimalFormat(decimalFormatString);

    public static double roundDouble(double number) {
        return Double.parseDouble(decimalFormat.format(number));
    }
}
