package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MapCanvas extends Canvas {
    public double planeX, planeY, destX, destY;
    public boolean isMapLoaded, isDestMarked;
    private double widthBlock, heightBlock;
    private int max, min;
    private double dist, initPlaneX, initPlaneY;
    public Integer[][] matrix;

    public void setData(Integer[][] coords, double x, double y, int maxMapValue, int minMapValue, double distance) {
        this.matrix = coords;
        this.planeX = x;
        this.planeY = y;
        this.max = maxMapValue;
        this.min = minMapValue;
        this.dist = distance;
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        this.widthBlock = getWidth() / matrix[0].length;
        this.heightBlock = getHeight() / matrix.length;

        for (int i = 0; i < matrix.length; i ++) {
            for (int j = 0; j < matrix[i].length; j++) {
                gc.setFill(getColor(matrix[i][j]));
                gc.fillRect(j * widthBlock, i * heightBlock, widthBlock, heightBlock);
                gc.setFont(new Font("TimesRoman", heightBlock / 5));
                gc.setFill(Color.BLACK);
                gc.fillText(matrix[i][j] + "", (j + 0.5) * widthBlock, (i + 0.5) * heightBlock);
            }
        }
    }

    public void markPlane(double posX, double posY) {
        this.planeX = (int)(posX / widthBlock);
        this.planeY = (int)(posY / heightBlock);
        drawImage("./resources/plane.png", posX, posY);
    }

    public void markDest(double posX, double posY) {
        if (!isMapLoaded)
            return;
        drawImage("./resources/dest.png", posX, posY);
    }

    private void drawImage(String name, double posX, double posY) {
        try {
            Image img = new Image(new FileInputStream(name));
            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(img, (int)(posX / widthBlock)*widthBlock, (int)(posY / heightBlock)*heightBlock);
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    private Color getColor(int val) {
        int red, green, blue;
        double percent = ((double)(val - this.min) / (double)(this.max - this.min)) * 100;

        if (val == this.max) {
            red = 255;
            green = 0;
            blue = 0;
        } else if (percent < 50) {
            red = (int)(255 * (percent / 50));
            green = 255;
            blue = 0;
        } else {
            red = 255;
            green = (int)(255 * ((50 - percent % 50) / 50));
            blue = 0;
        }
        return Color.rgb(red, green, blue);
    }

}
