import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private final Picture pic;
    private final int width;
    private final int height;
    private final double[][] energies;
    private final double[][] minCost;
    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        pic = copyConstructor(picture);
        energies = new double[width][height];
        minCost = new double[width][height];
    }
    private static Picture copyConstructor(Picture picture) {
        Picture copy = new Picture(picture.width(), picture.height());
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                copy.set(i, j, picture.get(i, j));
            }
        }
        return copy;
    }
    public Picture picture() {
        return new Picture(pic);
    }
    public int width() {
        return width;
    }
    public int height() {
        return height;
    }
    private int[] exceedCheckOp(int x, int y) {
        int[] result = {x, y};

        if (x == -1) {
            result[0] = width - 1;
        } else if (x == width) {
            result[0] = 0;
        }
        if (y == -1) {
            result[1] = height - 1;
        } else if (y == height) {
            result[1] = 0;
        }

        return result;
    }
    private int[] getRGB(int x, int y) {
        int[] checkResult = exceedCheckOp(x, y);
        x = checkResult[0];
        y = checkResult[1];

        Color pixel = pic.get(x, y);
        int[] rgb = new int[3];
        rgb[0] = pixel.getRed();
        rgb[1] = pixel.getGreen();
        rgb[2] = pixel.getBlue();

        return rgb;
    }
    private static int cptDiffSqr(int[] one, int[] aoh) {
        int rDiff = one[0] - aoh[0];
        int gDiff = one[1] - aoh[1];
        int bDiff = one[2] - aoh[2];

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }

        if (energies[x][y] == 0) {
            int[] left = getRGB(x - 1, y);
            int[] right = getRGB(x + 1, y);
            int[] up = getRGB(x, y - 1);
            int[] down = getRGB(x, y + 1);

            int horDiffSqr = cptDiffSqr(right, left);
            int verDiffSqr = cptDiffSqr(down, up);

            energies[x][y] = horDiffSqr + verDiffSqr;
        }

        return energies[x][y];
    }

    private int[][] getNeighbors(int x, int y) {
        int[][] neighbors = new int[3][2];

        if (y == 0) {
            neighbors = new int[][]{null, null, null};
            return neighbors;
        }

        neighbors[0] = new int[]{x - 1, y - 1};
        neighbors[1] = new int[]{x, y - 1};
        neighbors[2] = new int[]{x + 1, y - 1};

        if (x == 0) {
            neighbors[0] = null;
        }
        if (x == width - 1) {
            neighbors[2] = null;
        }

        return neighbors;
    }
    private void cptMinCost() {
        int y = 0;
        for (int x = 0; x < width; x++) {
            minCost[x][y] = energy(x, y);
        }
        y += 1;

        while (y < height) {
            for (int x = 0; x < width; x++) {
                int[] minNbr = getMinNbr(getNeighbors(x, y));
                minCost[x][y] = energy(x, y) + minCost[minNbr[0]][minNbr[1]];
            }
            y++;
        }
    }
    private int getMinPos() {
        int minColumn = 0;
        double min = Integer.MAX_VALUE;

        for (int c = 0; c < width; c++) {
            double curr = minCost[c][height - 1];
            if (curr < min) {
                min = curr;
                minColumn = c;
            }
        }

        return minColumn;
    }
    private int[] getMinNbr(int[][] neighbors) {
        int[] minNbr = new int[2];
        double min = Double.MAX_VALUE;

        for (int[] nbr : neighbors) {
            if (nbr == null) {
                continue;
            }
            double cost = minCost[nbr[0]][nbr[1]];
            if (cost < min) {
                min = cost;
                minNbr = nbr;
            }
        }

        return minNbr;
    }
    private int[] convertToPath(int x) {
        int[] path = new int[height];
        path[height - 1] = x;

        int[] minNbr = {x, height - 1};
        for (int i = height - 2; i >= 0; i--) {
            minNbr = getMinNbr(getNeighbors(minNbr[0], minNbr[1]));
            path[i] = minNbr[0];
        }

        return path;
    }
    public int[] findVerticalSeam() {
        cptMinCost();
        int x = getMinPos();
        return convertToPath(x);
    }
    private static Picture rotatePic(Picture picture) {
        Picture newPicture = new Picture(picture.height(), picture.width());
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                newPicture.set(row, col, picture.get(col, row));
            }
        }
        return newPicture;
    }
    public int[] findHorizontalSeam() {
        SeamCarver rotatePic = new SeamCarver(rotatePic(pic));
        return rotatePic.findVerticalSeam();
    }
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(pic, seam);
    }
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(pic, seam);
    }
}
