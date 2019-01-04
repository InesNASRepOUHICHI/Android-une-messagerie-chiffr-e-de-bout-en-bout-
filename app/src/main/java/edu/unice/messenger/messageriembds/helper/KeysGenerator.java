package edu.unice.messenger.messageriembds.helper;

import android.util.Log;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeysGenerator {

    static final String TAG = "AsymmetricAlgorithmRSA";

    public String getPublicAsymmetricAlgorithmRSA(){
        // Generate key pair for 1024-bit RSA encryption and decryption
        Key publicKey = null;
        Key privateKey = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (Exception e) {
            Log.e(TAG, "RSA key pair error");
        }

        return publicKey.toString();
    }
}
