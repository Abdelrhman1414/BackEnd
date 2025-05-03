package com.BackEnd.BidPro.Util;

import java.util.UUID;

public class VerificationTokenUtil {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
