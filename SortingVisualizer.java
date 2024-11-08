import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SortingVisualizer extends JFrame {

    private JComboBox<String> algorithmSelector;
    private JButton startButton, resetButton;
    private JPanel sortingPanel;
    private JLabel timeLabel;
    private int[] data;
    private final int SIZE = 100;
    private boolean isSorting = false;

    public SortingVisualizer() {
        setTitle("Sorting Algorithm Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Sorting panel (visualization)
        sortingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawArray(g, data);
            }
        };
        sortingPanel.setPreferredSize(new Dimension(800, 500));
        add(sortingPanel, BorderLayout.CENTER);

        // Bottom panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Dropdown for selecting the algorithm
        algorithmSelector = new JComboBox<>(new String[] {
                "Bubble Sort", "Quick Sort", "Merge Sort", "Selection Sort", "Insertion Sort"
        });
        controlPanel.add(algorithmSelector);

        // Start Button
        startButton = new JButton("Start Sorting");
        controlPanel.add(startButton);

        // Reset Button
        resetButton = new JButton("Reset Array");
        controlPanel.add(resetButton);

        // Label for displaying sorting time
        timeLabel = new JLabel("Sorting Time: ");
        controlPanel.add(timeLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Generate random array data
        generateRandomArray();

        // Action listener for start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSorting) {
                    String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
                    startSorting(selectedAlgorithm);
                }
            }
        });

        // Action listener for reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomArray();
                sortingPanel.repaint();
            }
        });
    }

    // Method to generate random array
    private void generateRandomArray() {
        data = new int[SIZE];
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (Math.random() * sortingPanel.getHeight());
        }
    }

    // Method to start sorting based on user selection
    private void startSorting(String algorithm) {
        isSorting = true;
        long startTime = System.nanoTime();

        new Thread(() -> {
            switch (algorithm) {
                case "Bubble Sort":
                    bubbleSort(data);
                    break;
                case "Quick Sort":
                    quickSort(data, 0, data.length - 1);
                    break;
                case "Merge Sort":
                    mergeSort(data, 0, data.length - 1);
                    break;
                case "Selection Sort":
                    selectionSort(data);
                    break;
                case "Insertion Sort":
                    insertionSort(data);
                    break;
            }

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            SwingUtilities.invokeLater(() -> {
                timeLabel.setText("Sorting Time: " + duration / 1_000_000 + " ms");
                sortingPanel.repaint();
                isSorting = false;
            });
        }).start();
    }

    // Method to visualize the array as bars
    private void drawArray(Graphics g, int[] array) {
        int width = sortingPanel.getWidth() / array.length;
        for (int i = 0; i < array.length; i++) {
            g.setColor(Color.BLUE);
            g.fillRect(i * width, sortingPanel.getHeight() - array[i], width, array[i]);
        }
    }

    // Bubble Sort
    private void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // Swap array[j] and array[j + 1]
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
                sleepAndRepaint();
            }
        }
    }

    // Quick Sort
    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
            sleepAndRepaint();
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        sleepAndRepaint();
        return i + 1;
    }

    // Merge Sort
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
            sleepAndRepaint();
        }
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
            sleepAndRepaint();
        }
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
            sleepAndRepaint();
        }
    }

    // Selection Sort
    private void selectionSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
            sleepAndRepaint();
        }
    }

    // Insertion Sort
    private void insertionSort(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                sleepAndRepaint();
            }
            array[j + 1] = key;
            sleepAndRepaint();
        }
    }

    // Method to add delay and repaint the array after each step
    private void sleepAndRepaint() {
        try {
            Thread.sleep(30); // 30 ms delay for visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sortingPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}