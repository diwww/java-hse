package ru.hse.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;


public class ImageProcessor {

    /**
     * Size of image grid
     */
    public static final int SIZE = 4;

    private String path;
    private boolean drawNums;
    private BufferedImage image;
    private BufferedImage squaredImage;
    private BufferedImage[] tiles;

    public ImageProcessor(String p, boolean d) throws IOException {
        path = p;
        drawNums = d;
        readImage();
        cropToSquare();
        splitToTiles();
    }

    /**
     * Reads image from file
     *
     * @throws IOException
     */
    private void readImage() throws IOException {
        try (FileInputStream input = new FileInputStream(path)) {
            image = ImageIO.read(input);
        }
    }

    /**
     * Crops an image into square form, using its min dimension
     * as a new size
     */
    private void cropToSquare() {
        int size = Math.min(image.getHeight(), image.getWidth());
        squaredImage = image.getSubimage(0, 0, size, size);
    }

    /**
     * Splits image into tiles
     */
    private void splitToTiles() {
        tiles = new BufferedImage[SIZE * SIZE];
        int count = 0;
        int width = squaredImage.getWidth() / SIZE;
        int height = squaredImage.getHeight() / SIZE;

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                tiles[count] = squaredImage.getSubimage(width * x, height * y, width, height);
                if (drawNums) {
                    Graphics graphics = tiles[count].getGraphics();
                    graphics.setColor(Color.RED);
                    graphics.setFont(new Font("Arial Black", Font.BOLD, 30));
                    graphics.drawString(count + "", 10, 35);
                }
                count++;
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getSquaredImage() {
        return squaredImage;
    }

    /**
     * Gets image tiles array
     *
     * @return array containing image tiles
     */
    public BufferedImage[] getTiles() {
        return tiles;
    }
}
