package ru.vsu.savina.graphvisualization.layout;

import ru.vsu.savina.graphvisualization.graphics.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridLayout {

    public static double[][] gridLayout(List<List<Integer>> adjList, Rectangle rect) {
        double T_MAX = 10;
        double T_MIN = 0.1;
        double[][] R = new double[adjList.size()][2];
        double fmin, f;
        double NE = 10;
        double RC = 0.8;
        double P = 0.55;

        int xMax = (int) (2 * Math.round(Math.sqrt(adjList.size())));

        boolean[][] used = new boolean[xMax][xMax];

        double[][] w = fillWeightMatrix(adjList);

        double T = T_MAX;
        initializeRandomLayout(R, xMax, used);

        fmin = localMin(R, used, adjList, xMax, w);
        f = fmin;

        while (T > T_MIN) {
            int n = 0;

            while (n < NE) {
                double[][] r = neighbor(R, P, used, xMax);
                boolean[][] used1 = updateUsed(r, used);

                double f1 = localMin(r, used1, adjList, xMax, w);
                double epsilon = Math.random() * 1;

                if (epsilon < Math.exp((f - f1) / T)) {
                    f = f1;
                    R = r.clone();
                    used = updateUsed(R, used);

                    if (f < fmin) {
                        fmin = f;
                    }
                }
                n++;
            }

            T *= RC;
        }

        double width = (rect.getWidth() - 50) / xMax;
        double height = (rect.getHeight() - 50) / xMax;

        for (int i = 0; i < R.length; i++) {
            R[i][0] *= width;
            R[i][0] += 50;
            R[i][1] *= height;
            R[i][1] += 50;
        }

        return R;
    }

    private static double[][] neighbor(double[][] R, double p, boolean[][] used, int maxX) {
        double[][] r1 = new double[R.length][2];
        boolean[][] addedPoints = new boolean[used.length][used.length];

        for (int i = 0; i < R.length; i++) {
            double epsilon = Math.random() * 1;
            if (epsilon > p) {
                r1[i][0] = R[i][0];
                r1[i][1] = R[i][1];

                addedPoints[(int) r1[i][0]][(int) r1[i][1]] = true;
            } else {
                double[] point = getFirstVacantPoint(used, addedPoints, maxX);
                r1[i][0] = point[0];
                r1[i][1] = point[1];

                addedPoints[(int) r1[i][0]][(int) r1[i][1]] = true;
            }
        }
        return r1;
    }

    private static double[] getFirstVacantPoint(boolean[][] used, boolean[][] addedPoints, int maxX) {
        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxX);

        while (used[x][y] || addedPoints[x][y]) {
            x = (int) (Math.random() * maxX);
            y = (int) (Math.random() * maxX);
        }

        return new double[]{x, y};
    }

    private static double localMin(double[][] r, boolean[][] used, List<List<Integer>> adjList, int xMax, double[][] w) {
        double dmax = 5;
        double f0 = fR(r, w, dmax);
        double dmin = Integer.MAX_VALUE;

        double[][] d = new double[adjList.size()][xMax * xMax];
        double[][] d1 = new double[adjList.size()][xMax * xMax];
        double d1min = 0;

        int beta = 0;
        double[] q = new double[]{};

        List<double[]> vacantPoints = getVacantPoints(used);

        for (int alpha = 0; alpha < r.length; alpha++) {
            for (double[] vacantPoint : vacantPoints) {

                int x = (int) (vacantPoint[1] + xMax * vacantPoint[0]);

                d[alpha][x] = FAlphaR(TR(r, alpha, vacantPoint), w, alpha, dmax) - FAlphaR(r, w, alpha, dmax);

                if (d[alpha][x] < dmin) {
                    dmin = d[alpha][x];
                    beta = alpha;
                    q = vacantPoint.clone();
                }
            }
        }

        while (dmin < 0) {
            double dmin1 = 0;
            int beta1 = 0;
            double[] q1 = new double[]{};
            for (int alpha = 0; alpha < r.length; alpha++) {
                if (alpha != beta) {
                    for (double[] vacantPoint : vacantPoints) {
                        if (!Arrays.equals(vacantPoint, q)) {
                            int x = (int) (vacantPoint[1] + xMax * vacantPoint[0]);

                            d1[alpha][x] = deltaAlphaTR(r, w, alpha, beta, vacantPoint, q, dmax);

                            if (d1[alpha][x] < dmin1) {
                                dmin1 = d1[alpha][x];
                                beta1 = alpha;
                                q1 = vacantPoint.clone();
                            }
                        }
                    }
                }
            }

            r = TR(r, beta, q);
            used = updateUsed(r, used);
            vacantPoints = getVacantPoints(used);

            int x1 = (int) (r[beta][1] + xMax * r[beta][0]);

            d[beta][x1] = -dmin;

            for (int alpha = 0; alpha < r.length; alpha++) {
                if (alpha != beta) {
                    for (double[] vacantPoint : vacantPoints) {
                        if (!Arrays.equals(vacantPoint, q)) {
                            int x = (int) (vacantPoint[1] + xMax * vacantPoint[0]);
                            d[beta][x] = d1[beta][x];
                        }
                    }
                }
            }

            beta = beta1;
            q = q1.clone();
            dmin = d1min;
        }
        return f0 + d1min;
    }

    private static boolean[][] updateUsed(double[][] r, boolean[][] used) {
        boolean[][] newUsed = new boolean[used.length][used.length];

        for (double[] doubles : r)
            newUsed[(int) doubles[0]][(int) doubles[1]] = true;

        return newUsed;
    }

    private static List<double[]> getVacantPoints(boolean[][] used) {
        List<double[]> points = new ArrayList<>();

        for (int i = 0; i < used.length; i++) {
            for (int j = 0; j < used.length; j++) {
                if (!used[i][j]) points.add(new double[]{i, j});
            }
        }

        return points;
    }


    private static void initializeRandomLayout(double[][] r, int maxX, boolean[][] used) {
        for (double[] line : r) {
            int x = (int) (Math.random() * maxX);
            int y = (int) (Math.random() * maxX);

            while (used[x][y]) {
                x = (int) (Math.random() * maxX);
                y = (int) (Math.random() * maxX);
            }

            line[0] = x;
            line[1] = y;
            used[x][y] = true;
        }
    }

    private static double[][] fillWeightMatrix(List<List<Integer>> adjList) {
        double[][] M1 = new double[adjList.size()][adjList.size()];
        double[][] M2;
        double[][] M3;
        double[][] M4;

        for (int i = 0; i < adjList.size(); i++) {
            for (int j = 0; j < adjList.size(); j++) {
                M1[i][j] = adjList.get(i).contains(j) ? 1 : 0;

                if (i == j)
                    M1[i][j] = 1;
            }
        }

        M2 = matrixMultiplication(M1, M1);
        M3 = matrixMultiplication(M1, M2);
        M4 = matrixMultiplication(M1, M3);

        double[][] w = new double[adjList.size()][adjList.size()];

        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w.length; j++) {
                if (M1[i][j] > 0)
                    w[i][j] = 3;
                else if (M1[i][j] == 0 && M2[i][j] > 0)
                    w[i][j] = 1;
                else if (M2[i][j] == 0 && M3[i][j] > 0)
                    w[i][j] = 0;
                else if (M3[i][j] == 0 && M4[i][j] > 0)
                    w[i][j] = -1;
                else
                    w[i][j] = -2;
            }
        }

        return w;
    }

    private static double[][] matrixMultiplication(double[][] M, double[][] T) {
        double[][] c = new double[M.length][M.length];

        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M.length; j++) {
                c[i][j] = 0;
                for (int k = 0; k < M.length; k++) {
                    c[i][j] += M[i][k] * T[k][j];
                }
            }
        }

        return c;
    }

    private static double fR(double[][] r, double[][] w, double dmax) {
        double sum = 0;

        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r.length; j++) {
                if (i < j)
                    sum += fij(r, w, i, j, dmax);
            }
        }

        return sum;
    }

    private static double fij(double[][] r, double[][] w, int i, int j, double dmax) {
        if (w[i][j] >= 0)
            return w[i][j] * d(r[i], r[j]);
        else
            return w[i][j] * Math.min(d(r[i], r[j]), dmax);
    }

    private static double d(double[] i, double[] j) {
        return Math.abs(i[0] - j[0]) + Math.abs(i[1] - j[1]);
    }

    private static double FAlphaR(double[][] r, double[][] w, int alpha, double dmax) {
        double sum = 0;
        for (int j = 0; j < r.length; j++) {
            sum += fij(r, w, alpha, j, dmax);
        }

        return sum;
    }

    private static double deltaAlphaR(double[][] r, double[][] w, double dmax, int alpha, double[] p) {

        return fR(TR(r, alpha, p), w, dmax) - fR(r, w, dmax);
    }

    private static double deltaAlphaTR(double[][] r, double[][] w, int alpha, int beta, double[] p, double[] q, double dmax) {
        double delta;
        if (alpha != beta && (p[0] != r[beta][0] && p[1] != r[beta][1]))
            delta = deltaAlphaR(r, w, dmax, alpha, p) + term(r, w, alpha, beta, p, q, dmax);
        else if (alpha == beta) {
            delta = deltaAlphaR(r, w, dmax, beta, p)
                    - deltaAlphaR(r, w, dmax, beta, q);
        } else
            delta = FAlphaR(TR(TR(r, beta, q), alpha, p), w, alpha, dmax)
                    - FAlphaR(TR(r, beta, q), w, alpha, dmax);
        return delta;
    }

    private static double[][] TR(double[][] r, int index, double[] point) {
        double[][] rCopy = r.clone();
        rCopy[index] = point;

        return rCopy;
    }

    private static double term(double[][] r, double[][] w, int alpha, int beta, double[] p, double[] q, double dmax) {

        return fij(TR(TR(r, beta, q), alpha, p), w, alpha, beta, dmax)
                - fij(TR(r, beta, q), w, alpha, beta, dmax) - fij(TR(r, alpha, p), w, alpha, beta, dmax)
                + fij(r, w, alpha, beta, dmax);
    }

}
