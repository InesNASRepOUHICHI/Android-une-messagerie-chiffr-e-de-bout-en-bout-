package edu.unice.messenger.messageriembds.helper;

public class ContactUtils {

    public static String contactNameToDisplayHisMessages = "ALL";
    public static String usernameConnected = "";

    public static String getContactNameToDisplayHisMessages() {
        return contactNameToDisplayHisMessages;
    }

    public static void setContactNameToDisplayHisMessages(String contactNameToDisplayHisMessages) {
        ContactUtils.contactNameToDisplayHisMessages = contactNameToDisplayHisMessages;
    }

    public static String getUsernameConnected() {
        return usernameConnected;
    }

    public static void setUsernameConnected(String usernameConnected) {
        ContactUtils.usernameConnected = usernameConnected;
    }
}
