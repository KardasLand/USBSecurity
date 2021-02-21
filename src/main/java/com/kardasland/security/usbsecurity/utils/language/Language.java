package com.kardasland.security.usbsecurity.utils.language;

public class Language {
    public static class register {
        public String title;
        public String message;
        public String empty;
        public String success;
    }
    public static class login {
        public String title;
        public String message;
        public String wrong;
    }
    public static class lock {
        public String enterPassword;
        public String enterPasswordTitle;
        public String offMessage;
        public String onMessage;
        public String preventedUSB;
    }
    public static class systemTray {
        public String about;
        public String lockState;
        public String offTitle;
        public String onTitle;
        public String quit;
    }
    public static class panelTitles{
        public String success;
        public String error;
    }
    public register register;
    public panelTitles panelTitles;
    public login login;
    public lock lock;
    public systemTray systemTray;
}
