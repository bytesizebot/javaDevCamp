package za.co.entelect.java_devcamp.util;

public final class MaskingUtils {

    private MaskingUtils() {}

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];

        String maskedLocal = local.length() > 1
                ? local.charAt(0) + "***"
                : "***";

        return maskedLocal + "@" + domain;
    }

    public static String maskId(String id) {
        if (id == null || id.length() <= 4) return "****";
        String start = id.substring(0, 2);
        String end = id.substring(id.length() - 2);
        String maskedMiddle = "*".repeat(id.length() - 4);
        return start + maskedMiddle + end;
    }
}