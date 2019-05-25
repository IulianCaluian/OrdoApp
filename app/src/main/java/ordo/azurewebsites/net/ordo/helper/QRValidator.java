package ordo.azurewebsites.net.ordo.helper;

public class QRValidator {
    public static boolean isValid(String token) {
        int n = token.length();
        if (n != 6)
            return false;
        for (int i = 0; i<n; ++i) {
            if (!Character.isDigit(token.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
