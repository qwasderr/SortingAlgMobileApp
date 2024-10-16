package com.example.sort2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import java.util.Random;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "array.txt";
    private Button btnSort, btnSave, btnLoad;
    private EditText inputArray;
    private TextView tvResults;
    private BarChart chartOriginal, chartSorted;
    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputArray = findViewById(R.id.inputArray);
        tvResults = findViewById(R.id.tvResults);
        btnSort = findViewById(R.id.btnSort);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        chartOriginal = findViewById(R.id.chartOriginal);
        chartSorted = findViewById(R.id.chartSorted);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chartOriginal.setNoDataText("");
        chartSorted.setNoDataText("");

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputArray.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введіть значення масиву", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] stringNumbers = input.split(",");
                double[] array = new double[stringNumbers.length];

                for (int i = 0; i < stringNumbers.length; i++) {
                    try {
                        array[i] = Double.parseDouble(stringNumbers[i].trim());
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Введіть правильні числа (цілі або дробові), розділені комами", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (array.length == 0) {
                    Toast.makeText(MainActivity.this, "Масив не може бути порожнім", Toast.LENGTH_SHORT).show();
                    return;
                }

                drawChart(array, chartOriginal);
                runSortingAlgorithms(array);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveArrayToFile();
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadArrayFromFile();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_developer_info) {
            Intent intent = new Intent(MainActivity.this, DeveloperInfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawChart(double[] array, BarChart chart) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            entries.add(new BarEntry(i, (float) array[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Значення масиву");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate();

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void saveArrayToFile() {
        String input = inputArray.getText().toString();
        if (!input.isEmpty()) {
            try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
                fos.write(input.getBytes());
                tvResults.setText("Масив збережено у файл");
            } catch (IOException e) {
                e.printStackTrace();
                tvResults.setText("Помилка при збереженні масиву");
            }
        } else {
            tvResults.setText("Масив не введено");
        }
    }

    private void loadArrayFromFile() {
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String loadedArray = new String(bytes);
            inputArray.setText(loadedArray);
            tvResults.setText("Масив завантажено з файлу");
        } catch (IOException e) {
            e.printStackTrace();
            tvResults.setText("Помилка при завантаженні масиву");
        }
    }

    private void runSortingAlgorithms(double[] array) {
        StringBuilder result = new StringBuilder();

        SortingResult bubbleResult = bubbleSort(Arrays.copyOf(array, array.length));
        SortingResult quickResult = quickSort(Arrays.copyOf(array, array.length));
        SortingResult mergeResult = mergeSort(Arrays.copyOf(array, array.length));
        SortingResult insertionResult = insertionSort(Arrays.copyOf(array, array.length));
        SortingResult selectionResult = selectionSort(Arrays.copyOf(array, array.length));
        SortingResult bogosortResult = bogosort(Arrays.copyOf(array, array.length));

        SortingResult[] sortingResults = {bubbleResult, quickResult, mergeResult, insertionResult, selectionResult, bogosortResult};
        SortingResult fastestResult = sortingResults[0];

        for (SortingResult sortingResult : sortingResults) {
            if (sortingResult.timeTaken < fastestResult.timeTaken) {
                fastestResult = sortingResult;
            }
        }

        result.append("Bubble Sort: ").append(Arrays.toString(bubbleResult.sortedArray))
                .append("\nКількість порівнянь: ").append(bubbleResult.comparisons)
                .append("\nЧас сортування (нс): ").append(bubbleResult.timeTaken).append("\n\n");

        result.append("Quick Sort: ").append(Arrays.toString(quickResult.sortedArray))
                .append("\nКількість порівнянь: ").append(quickResult.comparisons)
                .append("\nЧас сортування (нс): ").append(quickResult.timeTaken).append("\n\n");

        result.append("Merge Sort: ").append(Arrays.toString(mergeResult.sortedArray))
                .append("\nКількість порівнянь: ").append(mergeResult.comparisons)
                .append("\nЧас сортування (нс): ").append(mergeResult.timeTaken).append("\n\n");

        result.append("Insertion Sort: ").append(Arrays.toString(insertionResult.sortedArray))
                .append("\nКількість порівнянь: ").append(insertionResult.comparisons)
                .append("\nЧас сортування (нс): ").append(insertionResult.timeTaken).append("\n\n");

        result.append("Selection Sort: ").append(Arrays.toString(selectionResult.sortedArray))
                .append("\nКількість порівнянь: ").append(selectionResult.comparisons)
                .append("\nЧас сортування (нс): ").append(selectionResult.timeTaken).append("\n\n");

        result.append("Bogo Sort: ").append(Arrays.toString(bogosortResult.sortedArray))
                .append("\nКількість порівнянь: ").append(bogosortResult.comparisons)
                .append("\nЧас сортування (нс): ").append(bogosortResult.timeTaken).append("\n\n");

        drawChart(bubbleResult.sortedArray, chartSorted);

        SpannableString spannable = new SpannableString(result.toString());

        if (fastestResult == bubbleResult) {
            highlightAlgorithm(spannable, "Bubble Sort");
        } else if (fastestResult == quickResult) {
            highlightAlgorithm(spannable, "Quick Sort");
        } else if (fastestResult == mergeResult) {
            highlightAlgorithm(spannable, "Merge Sort");
        } else if (fastestResult == insertionResult) {
            highlightAlgorithm(spannable, "Insertion Sort");
        } else if (fastestResult == selectionResult) {
            highlightAlgorithm(spannable, "Selection Sort");
        } else if (fastestResult == bogosortResult) {
            highlightAlgorithm(spannable, "Bogo Sort");
        }

        tvResults.setText(spannable);
    }

    private void highlightAlgorithm(SpannableString spannable, String algorithm) {
        int start = spannable.toString().indexOf(algorithm);
        int end = start + algorithm.length();
        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, 0);
    }
    private static class SortingResult {
        double[] sortedArray;
        long comparisons;
        long timeTaken;

        SortingResult(double[] sortedArray, long comparisons, long timeTaken) {
            this.sortedArray = sortedArray;
            this.comparisons = comparisons;
            this.timeTaken = timeTaken;
        }
    }

    private SortingResult bubbleSort(double[] array) {
        long comparisons = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                comparisons++;
                if (array[j] > array[j + 1]) {
                    double temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        long timeTaken = System.nanoTime() - startTime;
        return new SortingResult(array, comparisons, timeTaken);
    }

    private SortingResult quickSort(double[] array) {
        //long comparisons = 0;
        long startTime = System.nanoTime();
        long comparisons = quickSortHelper(array, 0, array.length - 1);
        long timeTaken = System.nanoTime() - startTime;
        return new SortingResult(array, comparisons, timeTaken);
    }

    private long quickSortHelper(double[] array, int low, int high) {
        long comparisons = 0;
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            comparisons += (high - low);
            comparisons += quickSortHelper(array, low, pivotIndex - 1);
            comparisons += quickSortHelper(array, pivotIndex + 1, high);
        }
        return comparisons;
    }

    private int partition(double[] array, int low, int high) {
        double pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                double temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        double temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    private SortingResult mergeSort(double[] array) {
        long[] comparisons = {0};
        long startTime = System.nanoTime();

        mergeSortRecursive(array, comparisons);

        long endTime = System.nanoTime();
        return new SortingResult(array, comparisons[0], endTime - startTime);
    }

    private void mergeSortRecursive(double[] array, long[] comparisons) {
        if (array.length > 1) {
            int mid = array.length / 2;
            double[] left = Arrays.copyOfRange(array, 0, mid);
            double[] right = Arrays.copyOfRange(array, mid, array.length);

            mergeSortRecursive(left, comparisons);
            mergeSortRecursive(right, comparisons);

            merge(array, left, right, comparisons);
        }
    }

    private void merge(double[] array, double[] left, double[] right, long[] comparisons) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            comparisons[0]++;
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) {
            array[k++] = left[i++];
        }
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

    private SortingResult insertionSort(double[] array) {
        long comparisons = 0;
        long startTime = System.nanoTime();

        for (int i = 1; i < array.length; i++) {
            double key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                comparisons++;
                array[j + 1] = array[j];
                j--;
            }
            comparisons++;
            array[j + 1] = key;
        }

        long endTime = System.nanoTime();
        return new SortingResult(array, comparisons, endTime - startTime);
    }

    private SortingResult selectionSort(double[] array) {
        long comparisons = 0;
        long startTime = System.nanoTime();

        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                comparisons++;
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            double temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }

        long endTime = System.nanoTime();
        return new SortingResult(array, comparisons, endTime - startTime);
    }

    public static SortingResult bogosort(double[] array) {
        long startTime = System.nanoTime();
        long comparisons = 0;

        while (!isSorted(array)) {
            shuffle(array);
            comparisons++;
        }

        long timeTaken = System.nanoTime() - startTime;
        return new SortingResult(array, comparisons, timeTaken);
    }

    private static boolean isSorted(double[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static void shuffle(double[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            double temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}

