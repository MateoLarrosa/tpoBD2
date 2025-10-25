package modelo;

public final class ChatUtils {

    private ChatUtils() {
    }

    // Clave canónica para chat 1-a-1 (ordena IDs para evitar duplicados):
    public static String chatKeyForUsers(String userA, String userB) {
        if (userA.compareTo(userB) <= 0) {
            return "messages:" + userA + ":" + userB;
        } else {
            return "messages:" + userB + ":" + userA;
        }
    }

    public static String consumerGroupForChat(String chatKey) {
        return "cg:" + chatKey;
    }
}
