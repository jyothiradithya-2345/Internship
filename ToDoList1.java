import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class ToDoList1 {
    static class ToDo {
        String text;
        boolean completed;
        ToDo(String text) {
            this.text = text;
            this.completed = false;
        }

        String getDisplayText(int index) {
            return (index + 1) + ". " + (completed ? "Completed " : "") + text;
        }
    }
    static Stack<ToDo> tasks = new Stack<>();

    public static void main(String[] args) {
        launchGUI();
    }

    public static void launchGUI() {
        JFrame frame = new JFrame("To-Do List Manager");
        frame.setSize(1000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 230));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel title = new JLabel("Task List Manager", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 36));
        title.setForeground(new Color(150, 150, 170));
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> taskList = new JList<>(model);
        taskList.setFont(new Font("Times New Roman", Font.BOLD, 16));
        taskList.setSelectionBackground(new Color(180, 210, 215));
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 230), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 250));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTextField taskField = new JTextField();
        decorateInput(taskField);
        JTextField dateField = new JTextField();
        dateField.setToolTipText("yyyy-mm-dd");
        decorateInput(dateField);
        JTextField timeField = new JTextField();
        timeField.setToolTipText("HH:mm");
        decorateInput(timeField);
        inputPanel.add(new JLabel("Task:", SwingConstants.RIGHT));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Date:", SwingConstants.RIGHT));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Time:", SwingConstants.RIGHT));
        inputPanel.add(timeField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 250));
        JButton addBtn = createButton("Push Task", new Color(120, 180, 255));
        JButton deleteBtn = createButton("Pop / Delete Index", new Color(255, 120, 120));
        JButton completeBtn = createButton("Mark Complete by Index", new Color(140, 255, 140));
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(completeBtn);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        addBtn.addActionListener(e -> {
        String taskText = taskField.getText();
        if (taskText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Task cannot be empty");
                return;
        }
           LocalDate date = null;
           LocalTime time = null;
            try {
                if (!dateField.getText().isEmpty())
                    date = LocalDate.parse(dateField.getText());

                if (!timeField.getText().isEmpty())
                    time = LocalTime.parse(timeField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date/time format!");
                return;
            }

            String full = formatTaskText(taskText, date, time);

            tasks.push(new ToDo(full)); // STACK PUSH
            refreshList(model);

            taskField.setText("");
            dateField.setText("");
            timeField.setText("");
        });

        deleteBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter index to delete OR leave empty to pop:");

            if (input == null)
                return;

            if (input.isEmpty()) {
                // POP behavior
                if (tasks.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Stack is empty!");
                    return;
                }
                tasks.pop();
                refreshList(model);
                return;
            }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index < 0 || index >= tasks.size()) {
                    JOptionPane.showMessageDialog(frame, "Invalid index!");
                    return;
                }

                tasks.remove(index);
                refreshList(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number!");
            }
        });

        completeBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter index to mark complete:");
            if (input == null || input.isEmpty())
                return;

            try {
                int index = Integer.parseInt(input) - 1;

                if (index < 0 || index >= tasks.size()) {
                    JOptionPane.showMessageDialog(frame, "Invalid index!");
                    return;
                }

                tasks.get(index).completed = true;
                refreshList(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number!");
            }
        });
    }

    static void refreshList(DefaultListModel<String> model) {
        model.clear();
        for (int i = 0; i < tasks.size(); i++) {
            model.addElement(tasks.get(i).getDisplayText(i));
        }
    }

    static String formatTaskText(String task, LocalDate date, LocalTime time) {
        StringBuilder sb = new StringBuilder(task);

        if (date != null)
            sb.append(" (Due: ").append(date).append(", ").append(date.getDayOfWeek()).append(")");

        if (time != null)
            sb.append(" [Reminder: ").append(time).append("]");

        return sb.toString();
    }

    static void decorateInput(JTextField field) {
        field.setFont(new Font("Times New Roman", Font.BOLD, 16));
        field.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 2));
    }

    static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
}
