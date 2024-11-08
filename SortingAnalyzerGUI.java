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

public class SortingAnalyzerGUI extends JFrame {

    private JComboBox<String> algorithmSelector;
    private JButton startButton;
    private JPanel sortingPanel;
    private JLabel timeLabel;
    private int[] data;

    public SortingAnalyzerGUI() {
        setTitle("Sorting Algorithm Analyzer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the layout
        setLayout(new BorderLayout());

        // Sorting panel (visualization)
        sortingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawArray(g, data);
            }
        };
        sortingPanel.setPreferredSize(new Dimension(600, 300));
        add(sortingPanel, BorderLayout.CENTER);

        // Bottom panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Dropdown for selecting the algorithm
        algorithmSelector = new JComboBox<>(
                new String[] { "Bubble Sort", "Quick Sort", "Merge Sort", "Selection Sort" });
        controlPanel.add(algorithmSelector);

        // Start Button
        startButton = new JButton("Start Sorting");
        controlPanel.add(startButton);

        // Label for displaying sorting time
        timeLabel = new JLabel("Sorting Time: ");
        controlPanel.add(timeLabel);

        add(controlPanel, BorderLayout.SOUTH);

        // Generate random array data
        generateRandomArray();

        // Add action listener to the button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
                startSorting(selectedAlgorithm);
            }
        });
    }

    // Method to generate random array
    private void generateRandomArray() {
        data = new int[50];
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (Math.random() * sortingPanel.getHeight());
        }
    }

    // Method to start sorting based on user selection
    private void startSorting(String algorithm) {
        long startTime = System.nanoTime();

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
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Display sorting time
        timeLabel.setText("Sorting Time: " + duration + " ns");

        // Repaint the panel to reflect sorted array
        sortingPanel.repaint();
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
        int i = (low - 1); // Index of smaller element
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
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
        }
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SortingAnalyzerGUI gui = new SortingAnalyzerGUI();
                gui.setVisible(true);
            }
        });
    }
}