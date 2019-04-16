package com.example.evolutionfitnessasd;

public class ContactsData {
    private String contactsName;
    private String contactsDescription;
    private int contactsImage;

    public ContactsData(String contactName, String contactDescription, int contactImage) {
        this.contactsName = contactName;
        this.contactsDescription = contactDescription;
        this.contactsImage = contactImage;
    }

    public String getContactName() {
        return contactsName;
    }

    public String getContactDescription() {
        return contactsDescription;
    }

    public int getContactImage() {
        return contactsImage;
    }
}
