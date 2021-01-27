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
    private GraphicsContext gc;
    public Integer[][] matrix;
    private boolean isPathExists;
    private String path;

    public void setData(Integer[][] coords, double x, double y, int maxMapValue, int minMapValue, double distance) {
        this.matrix = coords;
        this.planeX = x;
        this.planeY = y;
        this.max = maxMapValue;
        this.min = minMapValue;
        this.dist = distance;
    }
    
    public void setPath(String path) {
        isPathExists = true;
        this.path = path;
    }

    public void draw() {
        gc = getGraphicsContext2D();
        this.widthBlock = getWidth() / matrix[0].length;
        this.heightBlock = getHeight() / matrix.length;

        for (int i = 0; i < matrix.length; i ++) {
            for (int j = 0; j < matrix[i].length; j++) {
                gc.setFill(getColor(matrix[i][j]));
                gc.fillRect(j * widthBlock, i * heightBlock, widthBlock, heightBlock);
            }
        }
    }

    public void drawPath(String path) {
        String[] mapPath = path.split(",");
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        double posX = planeX;
        double posY = planeY;
        int len = mapPath.length - 1;
        for(int i = 0; i < len; i++) {
            switch (mapPath[i]) {
                case "Right":
                    ++posX;
                    gc.fillRect(posX * widthBlock, posY * heightBlock + heightBlock / 3, widthBlock / 2, heightBlock / 6);
                    break;
                case "Down":
                    ++posY;
                    gc.fillRect(posX * widthBlock + widthBlock / 3, posY * heightBlock, widthBlock / 6, heightBlock / 2);
                    break;
                case "Left":
                    --posX;
                    gc.fillRect(posX * widthBlock, posY * heightBlock + heightBlock / 3, widthBlock / 2, heightBlock / 6);
                    break;
                case "Up":
                    --posY;
                    gc.fillRect(posX * widthBlock + widthBlock / 3, posY * heightBlock, widthBlock / 6, heightBlock / 2);
                    break;
            }
        }
    }
    public void setPlaneOnMap(double simPlaneX, double simPlaneY) {

        double planePosX = ((int)((simPlaneX - initPlaneX) / dist)) + initPlaneX;
        double planePosY = -1 * ((int)((simPlaneY - initPlaneY) / dist)) + initPlaneY;

        if(simPlaneX == -5506399.0 && simPlaneY == -2231083.25)
            return;

        if (simPlaneX == 0 && simPlaneY == 0)
            return;

        if (planePosX < 0 || planePosY < 0) {
            System.out.println("simPlaneX < 0 || simPlaneY < 0");
            return;
        }

        draw();
        markPlane(planePosX, planePosY);

        if (isDestMarked)
            markDestByPosition(destX, destY);
    }

    public void markDestByPosition(double posX, double posY) {
        isDestMarked = true;
        destX = posX;
        destY = posY;
        drawImage("./resources/close.png", posX, posY);
    }

    public void markPlane(double posX, double posY) {
        this.planeX = (int)(posX / widthBlock);
        this.planeY = (int)(posY / heightBlock);
        drawImage("./resources/plane.png", posX, posY);
    }

    public void markDest(double posX, double posY) {
        if (!isMapLoaded)
            return;
        // this is an ugly work around, but i can't find something better
        if (isDestMarked) {
            // delete old map first
            draw();
            markPlane(planeX, planeY);
        }
        this.destX = (int)(posX / widthBlock);
        this.destY = (int)(posY / heightBlock);
        drawImage("./resources/dest.png", posX, posY);
        isDestMarked = true;
    }

    private void drawImage(String name, double posX, double posY) {
        try {
            Image img = new Image(new FileInputStream(name));
            gc = getGraphicsContext2D();
            gc.drawImage(img, (int)(posX / widthBlock)*widthBlock, (int)(posY / heightBlock)*heightBlock);
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    private Color getColor(int val) {
        int red, green, blue;
        double percent = ((double)(val - this.min) / (double)(this.max - this.min));

        if (val == this.min) {
            red = 255;
            green = 0;
            blue = 0;
        } else if (percent > 0.5) {
            red = (int)(255 * percent);
            green = 255;
            blue = 0;
        } else {
            red = 255;
            green = (int)(255 * percent * 2);
            blue = 0;
        }
        try {
            return Color.rgb(red, green, blue);
        } catch (Exception e) {
            System.out.println(red + " " + green + " " + blue);
            return Color.rgb(255, 255, 255);
        }
    }
}
