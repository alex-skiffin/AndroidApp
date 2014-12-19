package com.example.skif.testingapp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by skif on 13.12.2014.
 */
public class AnonymousInfo
{
    public UUID Id = UUID.randomUUID();

    public UUID PhoneId = UUID.randomUUID();

    public String ContactName = "user";

    public Map<String, String> VerySecretInfo = new HashMap<String, String>();

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(ContactName);
        return str.toString(); }
}

