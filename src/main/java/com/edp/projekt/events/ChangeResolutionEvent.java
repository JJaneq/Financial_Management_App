package com.edp.projekt.events;

import java.awt.*;

public class ChangeResolutionEvent {
    private final Dimension resolution;

    public ChangeResolutionEvent(Dimension resolution) {
        this.resolution = resolution;
    }

    public Dimension getResolution() {
        return resolution;
    }
}
