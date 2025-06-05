package com.edp.projekt.components;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BudgetIndicator extends Region {
    private double percentage;

    private Rectangle backgroundRect;
    private Rectangle spendRect;
    private Text overBudgetText;

    public BudgetIndicator() {
        this.percentage = 0.5;
        setPrefSize(100.0, 100.0);

        backgroundRect = new Rectangle();
        backgroundRect.setFill(Color.LIGHTGRAY);

        spendRect = new Rectangle();
        spendRect.setFill(Color.GREEN);

        overBudgetText = new Text("PRZEKROCZONO!");
        overBudgetText.setFill(Color.WHITE);
        overBudgetText.setFont(Font.font(16));
        overBudgetText.setVisible(false);

        getChildren().addAll(backgroundRect, spendRect, overBudgetText);

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
            overBudgetText.setVisible(true);
        } else {
            spendRect.setFill(Color.GREEN);
            overBudgetText.setVisible(false);
        }
    }

    public void setSpendingValue(double value, double maxValue) {
        double newPercentage = value / maxValue;
        setPercentage(newPercentage);
    }

    private void redraw() {
        double w = getWidth();
        double h = getHeight();

        backgroundRect.setWidth(w);
        backgroundRect.setHeight(h);

        spendRect.setWidth(w * percentage);
        spendRect.setHeight(h);

        // Wyśrodkowanie tekstu
        double textWidth = overBudgetText.getBoundsInLocal().getWidth();
        double textHeight = overBudgetText.getBoundsInLocal().getHeight();

        overBudgetText.setLayoutX((w - textWidth) / 2);
        overBudgetText.setLayoutY((h + textHeight / 2) / 2);

    }
}
