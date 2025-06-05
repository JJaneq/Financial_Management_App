package com.edp.projekt.components;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BudgetIndicator extends Region {
    private double percentage;

    private Rectangle backgroundRect;
    private Rectangle spendRect;

    public BudgetIndicator() {
        this.percentage = 0.5;
        setPrefSize(100.0, 100.0);

        backgroundRect = new Rectangle();
        backgroundRect.setFill(Color.LIGHTGRAY);

        spendRect = new Rectangle();
        spendRect.setFill(Color.GREEN);

        getChildren().addAll(backgroundRect, spendRect);

        // Nasłuchuj zmian rozmiaru, żeby przerysować
        widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        heightProperty().addListener((obs, oldVal, newVal) -> redraw());
    }

    public void setPercentage(double percentage) {
        this.percentage = Math.max(0, Math.min(percentage, 1.0));
        updateColor();
        redraw();
    }

    private void updateColor() {
        if (percentage >= 1) {
            spendRect.setFill(Color.RED);
        } else {
            spendRect.setFill(Color.GREEN);
        }
    }

    private void redraw() {
        double w = getWidth();
        double h = getHeight();

        backgroundRect.setWidth(w);
        backgroundRect.setHeight(h);

        spendRect.setWidth(w * percentage);
        spendRect.setHeight(h);
    }
}
