package ru.hse.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label stepsLabel;

    private final int SIZE = ImageProcessor.SIZE;
    private int steps = 0;
    private boolean sorted = true;
    private double imageWidth, imageHeight;
    private BufferedImage image, squaredImage;
    private BufferedImage[] tiles;
    private ImageView[][] images;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Calculate ImageView size without gaps and paddings
        double hGap = gridPane.getHgap();
        double vGap = gridPane.getVgap();
        imageWidth = (gridPane.getPrefWidth() - (gridPane.getPadding().getLeft() + gridPane.getPadding().getRight()) - hGap * (SIZE - 1)) / SIZE;
        imageHeight = (gridPane.getPrefHeight() - (gridPane.getPadding().getBottom() + gridPane.getPadding().getTop()) - vGap * (SIZE - 1)) / SIZE;
    }

    /**
     * Handles Open Button click event
     */
    @FXML
    private void onOpenClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images",
                "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                ImageProcessor processor = new ImageProcessor(file.getPath(), false);
                image = processor.getImage();
                squaredImage = processor.getSquaredImage();
                tiles = processor.getTiles();
                initializeImageMatrix();
                shuffle();
                refreshGrid();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onShowClick() {
        if (squaredImage != null) {
            ImageView imageView = new ImageView(SwingFXUtils.toFXImage(squaredImage, null));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(600);
            StackPane root = new StackPane();
            root.getChildren().add(imageView);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void onShuffleClick() {
        steps = 0;
        if (images != null) {
            shuffle();
            refreshGrid();
            setLabelText();
        }
    }


    private void setLabelText() {
        stepsLabel.setText("Steps: " + steps);
    }

    /**
     * Shows message box with congratulations
     */
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You win! Total steps: " + steps);
        alert.showAndWait();
    }

    /**
     * Sets stage to current controller
     *
     * @param stage stage, which will be binded to
     *              current controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Shuffles cells
     */
    private void shuffle() {
        Random random = new Random();
        // Shuffle two random cells n times
        for (int i = 0; i < SIZE * SIZE; i++) {
            swapCellContent(random.nextInt(SIZE), random.nextInt(SIZE), random.nextInt(SIZE), random.nextInt(SIZE));
        }
    }

    /**
     * Refreshes GridPane and checks
     * if cells are sorted
     */
    private void refreshGrid() {
        sorted = true;
        gridPane.getChildren().clear();
        for (int i = 0; i < SIZE * SIZE; i++) {
            int row = i / SIZE;
            int col = i % SIZE;
            gridPane.add(images[row][col], col, row);
            // If one of images has wrong position
            if (!images[row][col].getId().equals(i + "") && (i != SIZE * SIZE - 1)) {
                sorted = false;
            }
        }
    }

    /**
     * Swaps content of two cells
     *
     * @param i1 first row index
     * @param i2 first column index
     * @param j1 second row index
     * @param j2 second column index
     */
    private void swapCellContent(int i1, int i2, int j1, int j2) {
        ImageView temp = new ImageView(images[i1][i2].getImage());
        temp.setId(images[i1][i2].getId());
        images[i1][i2].setImage(images[j1][j2].getImage());
        images[i1][i2].setId(images[j1][j2].getId());
        images[j1][j2].setImage(temp.getImage());
        images[j1][j2].setId(temp.getId());
    }

    /**
     * Initializes matrix of ImageViews
     */
    private void initializeImageMatrix() {
        images = new ImageView[SIZE][SIZE];

        for (int i = 0; i < SIZE * SIZE; i++) {
            int row = i / SIZE;
            int col = i % SIZE;

            images[row][col] = new ImageView(SwingFXUtils.toFXImage(tiles[i], null));
            images[row][col].setId(i + "");
            images[row][col].setFitWidth(imageWidth);
            images[row][col].setFitHeight(imageHeight);

            // Attaching event handlers
            images[row][col].setOnMouseEntered(event -> images[row][col].setEffect(new Glow()));
            images[row][col].setOnMouseExited(event -> images[row][col].setEffect(null));
            images[row][col].setOnMouseClicked(event -> {
                System.out.println("clicked " + images[row][col].getId());
                if (col < SIZE - 1 && images[row][col + 1].getId().isEmpty()) {
                    swapCellContent(row, col, row, col + 1);
                    refreshGrid();
                    steps++;
                    setLabelText();
                } else if (col > 0 && images[row][col - 1].getId().isEmpty()) {
                    swapCellContent(row, col, row, col - 1);
                    refreshGrid();
                    steps++;
                    setLabelText();
                } else if (row < SIZE - 1 && images[row + 1][col].getId().isEmpty()) {
                    swapCellContent(row + 1, col, row, col);
                    refreshGrid();
                    steps++;
                    setLabelText();
                } else if (row > 0 && images[row - 1][col].getId().isEmpty()) {
                    swapCellContent(row - 1, col, row, col);
                    refreshGrid();
                    steps++;
                    setLabelText();
                }
                if (sorted) {
                    showAlert();
                }
                System.out.println(sorted);
            });
        }

        // Set last image to empty
        images[SIZE - 1][SIZE - 1].setId("");
        images[SIZE - 1][SIZE - 1].setImage(null);
    }
}
