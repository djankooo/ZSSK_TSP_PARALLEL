package TSP_GA;

import java.util.Random;

public class GeneticAlg {
    private static final float DEFAULT_CROSSOVER_PROBABILITY = 0.9f;
    private static final float DEFAULT_MUTATION_PROBABILITY = 0.01f;
    private static final int DEFAULT_POPULATION_SIZE = 30;

    private float crossoverProbability = DEFAULT_CROSSOVER_PROBABILITY;
    private float mutationProbability = DEFAULT_MUTATION_PROBABILITY;
    private int populationSize = DEFAULT_POPULATION_SIZE;

    private int mutationTimes = 0;
    private int currentGeneration = 0;
    private int maxGeneration = 1000;

    private int pointNum;
    private int[][] population;
    private float[][] dist;

    private int[] bestIndivial;
    private float bestDist;
    private int currentBestPosition;
    private float currentBestDist;

    private float[] values;
    private float[] fitnessValues;
    private float[] roulette;

    private boolean isAutoNextGeneration = false;




    private void init() {
        mutationTimes = 0;
        currentGeneration = 0;
        bestIndivial = null;
        bestDist = 0;
        currentBestPosition = 0;
        currentBestDist = 0;

        values = new float[populationSize];
        fitnessValues = new float[populationSize];
        roulette = new float[populationSize];
        population = new int[populationSize][pointNum];

        //initDist(points);
        // 父代
        for (int i = 0; i < populationSize; i++) {
            population[i] = randomIndivial(pointNum);
        }
        evaluateBestIndivial();
    }


    private int[] randomIndivial(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        return shuffle(a);
    }

    /**
     *
     * @param a
     * @return
     */
    private int[] shuffle(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int p = random(a.length);
            int tmp = a[i];
            a[i] = a[p];
            a[p] = tmp;
        }
        return a;
    }

    private static Random rd;
    private int random(int n) {
        Random ran = rd;
        if (ran == null) {
            ran = new Random();
        }
        return ran.nextInt(n);
    }

    /**
     *
     * @return
     */
    private float calculateIndivialDist(int[] indivial) {
        float sum = dist[indivial[0]][indivial[indivial.length - 1]];
        for (int i = 1; i < indivial.length; i++) {
            sum += dist[indivial[i]][indivial[i - 1]];
        }
        return sum;
    }

    /**
     *
     *
     */
    private void evaluateBestIndivial() {
        for (int i = 0; i < population.length; i++) {
            values[i] = calculateIndivialDist(population[i]);
        }
        evaluateBestCurrentDist();
        if (bestDist == 0 || bestDist > currentBestDist) {
            bestDist = currentBestDist;
            bestIndivial = population[currentBestPosition].clone();
        }
    }

    /**
     *
     *
     */
    public void evaluateBestCurrentDist() {
        currentBestDist = values[0];
        for (int i = 1; i < populationSize; i++) {
            if (values[i] < currentBestDist) {
                currentBestDist = values[i];
                currentBestPosition = i;
            }
        }
    }
}
