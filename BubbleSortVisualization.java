import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BubbleSortVisualization extends JFrame implements ActionListener {
    private JTextField sizeField, arrayField;
    private JComboBox<String> orderComboBox;
    private JButton runButton, resetButton, leftButton, rightButton;
    private JLabel stepLabel, swapCountLabel;
    private JPanel inputPanel, visualizationPanel, arrayPanel;

    private List<int[]> intermediateArrays;
    private List<Integer> compare;
    private int currentIndex = 0;
    private int swapCount = 0;
    private boolean sortingComplete = false;
    private boolean back=false;

    public BubbleSortVisualization() {
        setTitle("Bubble Sort Visualization");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputPanel = new JPanel(new GridLayout(5, 2));
        visualizationPanel = new JPanel(new BorderLayout());
        arrayPanel = new JPanel(new GridLayout(1, 10, 5, 5));

        sizeField = new JTextField(10);
        arrayField = new JTextField(20);
        orderComboBox = new JComboBox<>(new String[]{"Ascending", "Descending"});
        runButton = new JButton("Run");
        resetButton = new JButton("Reset");
        leftButton = new JButton("◀");
        rightButton = new JButton("▶");
        stepLabel = new JLabel();
        swapCountLabel = new JLabel();

        runButton.addActionListener(this);
        resetButton.addActionListener(this);
        leftButton.addActionListener(this);
        rightButton.addActionListener(this);

        inputPanel.add(new JLabel("Array Size:"));
        inputPanel.add(sizeField);
        inputPanel.add(new JLabel("Array Values (comma-separated):"));
        inputPanel.add(arrayField);
        inputPanel.add(new JLabel("Order of Sorting:"));
        inputPanel.add(orderComboBox);
        inputPanel.add(new JLabel());
        inputPanel.add(runButton);
        inputPanel.add(new JLabel());
        inputPanel.add(resetButton);

        visualizationPanel.add(leftButton, BorderLayout.WEST);
        visualizationPanel.add(arrayPanel, BorderLayout.CENTER);
        visualizationPanel.add(rightButton, BorderLayout.EAST);
        visualizationPanel.add(stepLabel, BorderLayout.SOUTH);
        visualizationPanel.add(swapCountLabel, BorderLayout.NORTH);

        add(inputPanel, BorderLayout.NORTH);
        add(visualizationPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runButton) {
            runSorting();
        } else if (e.getSource() == resetButton) {
            reset();
        } else if (e.getSource() == leftButton) {
            if (currentIndex > 0) {
                back=true;
                currentIndex--;
                updateVisualization();
            }
        } else if (e.getSource() == rightButton) {
            if (currentIndex < intermediateArrays.size() - 1) {
                back=false;
                currentIndex++;
                updateVisualization();
            }
        }
    }

    private void runSorting() {
        String[] values = arrayField.getText().split(",");
        int[] initialArray = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            initialArray[i] = Integer.parseInt(values[i].trim());
        }

        intermediateArrays = new ArrayList<>();
        compare=new Vector<>();
        intermediateArrays.add(initialArray.clone());

        swapCount = 0;
        bubbleSort(initialArray);
        currentIndex = 0;
        updateVisualization();
    }

    private void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int j = 0; j < n-1; j++) {
            swapped = false;
            for (int i = 0; i < n-j-1; i++) {
                if (orderComboBox.getSelectedIndex() == 0) {
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true;
                    }
                } else {
                    if (arr[i] < arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        swapped = true;
                    }
                }
                compare.add(i);
                intermediateArrays.add(arr.clone());
            }
            if(!swapped)
                break;
        }
    }

    private void updateVisualization() {
        if (currentIndex == intermediateArrays.size() - 1) {
            stepLabel.setText("Sorting is complete");
            swapCountLabel.setText("Total swaps: " + swapCount);
            sortingComplete=true;
        } else {
            sortingComplete=false;
            int[] currentArray = intermediateArrays.get(currentIndex);
            int[] nextArray = intermediateArrays.get(currentIndex + 1);
            boolean noSwappingNeeded = true;
            StringBuilder swapValues = new StringBuilder();

            for (int i = 0; i < currentArray.length; i++) {
                if (currentArray[i] != nextArray[i]) {
                    if (!noSwappingNeeded) {
                        swapValues.append(", ");
                    }
                    swapValues.append(currentArray[i]).append(" <-> ").append(nextArray[i]);
                    noSwappingNeeded = false;
                    break;
                }
            }

            if (noSwappingNeeded) {
                stepLabel.setText("Step "+ (currentIndex+1) +": (No swapping needed)");
                swapCountLabel.setText("Swap count: " + swapCount);
            } else if(back) {
                stepLabel.setText("Step "+ (currentIndex+1) +": Swap values: " + swapValues);
                swapCountLabel.setText("Swap count: " + swapCount);
                if(currentIndex!=0)
                    swapCount--;
                back=false;
            } else {
                stepLabel.setText("Step "+ (currentIndex+1) +": Swap values: " + swapValues);
                swapCountLabel.setText("Swap count: " + ++swapCount);
            }
        }

        int[] currentArray = intermediateArrays.get(currentIndex);
        int currentCmp=-1;
        int[] nextArray=null;
        if(!sortingComplete){
            currentCmp=compare.get(currentIndex);
            nextArray = intermediateArrays.get(currentIndex + 1);
        }
        
        arrayPanel.removeAll();
        for (int i = 0; i < currentArray.length; i++) {
            JLabel box = new JLabel(Integer.toString(currentArray[i]));
            box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            box.setHorizontalAlignment(JLabel.CENTER);

            if(!sortingComplete && (i==currentCmp || i==currentCmp+1)){
                if(currentArray[i] != nextArray[i]){
                    box.setOpaque(true);
                    box.setBackground(Color.RED);
                }
                else{
                    box.setOpaque(true);
                    box.setBackground(Color.ORANGE);
                }
            }
            else{
                box.setOpaque(true);
                box.setBackground(Color.WHITE);
            }
            arrayPanel.add(box);
        }
        arrayPanel.revalidate();
        arrayPanel.repaint();
    }

    private void reset() {
        sizeField.setText("");
        arrayField.setText("");
        orderComboBox.setSelectedIndex(0);
        intermediateArrays = null;
        currentIndex = 0;
        swapCount = 0;
        sortingComplete = false;
        stepLabel.setText("");
        swapCountLabel.setText("");
        arrayPanel.removeAll();
        arrayPanel.revalidate();
        arrayPanel.repaint();
    }

    public static void main(String[] args) {
        new BubbleSortVisualization();
    }
}
