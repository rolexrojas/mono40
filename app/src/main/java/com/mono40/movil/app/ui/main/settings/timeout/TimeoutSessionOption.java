package com.mono40.movil.app.ui.main.settings.timeout;

public class TimeoutSessionOption {
    private int timeInMinutes;

    public TimeoutSessionOption(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public String getTitle() {
        String suffix = timeInMinutes == 1 ? "Minuto" : "Minutos";
        return timeInMinutes + " " + suffix;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }
}
