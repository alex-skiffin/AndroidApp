package com.example.skif.testingapp;

import java.util.Date;
import java.util.UUID;

public class Profile {
    public UUID Id = UUID.randomUUID();

    public String ProfileName;

    public String Prefix;
    public String Name;
    public String Patronymic;
    public String LastName;

    public String HomePhone;
    public String MobilePhone;
    public String WorkPhone;

    public String HomeEmail;
    public String MobileEmail;
    public String WorkEmail;

    public String WorkPlace;

    public Date Birthday;

    public UUID PhotoId = UUID.randomUUID();
}
