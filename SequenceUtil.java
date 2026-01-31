public class SequenceUtil {

    public static String nextSequence(String current) {

        // 01 → 99
        if (current.matches("\\d{2}")) {
            int num = Integer.parseInt(current);
            if (num < 99) {
                return String.format("%02d", num + 1);
            } else {
                return "A0";
            }
        }

        // A0 → A9 → B0 → B9 → ...
        if (current.matches("[A-Z][0-9]")) {
            char letter = current.charAt(0);
            int digit = current.charAt(1) - '0';

            if (digit < 9) {
                return "" + letter + (digit + 1);
            } else {
                return "" + (char)(letter + 1) + "0";
            }
        }

        return "01";
    }
}
