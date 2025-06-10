package com.edp.projekt.events;

public class ChangeThemeEvent {
    private final String theme;

    public ChangeThemeEvent(String theme) {
        this.theme = theme;
        System.out.println(this.theme);
    }

    public String getTheme() {
        return theme;
    }
}
