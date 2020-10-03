package com.example.english.constants;

public class EmailConstants {
    public static final String LOGIN = System.getenv("MAILGUN_SMTP_LOGIN");
    public static final String PASSWORD = System.getenv("MAILGUN_SMTP_PASSWORD");
    public static final String PORT = System.getenv("MAILGUN_SMTP_PORT");
    public static final String SERVER = System.getenv("MAILGUN_SMTP_SERVER");

    public static final String EMAIL = System.getenv("EMAIL");
//    public static final String EMAIL = "requiemforadream@abv.bg";
    public static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");

    public static final String SUBJECT = "Welcome to Hello-English!";
    public static final String TEXT = "Thank you for joining us. Stick with the site and improve your knowledge" +
            " What is Lorem Ipsum?\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
}
