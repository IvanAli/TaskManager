package view;

import com.task.Priority;
import com.task.Task;
import com.task.TaskManager;
import com.timer.TimerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Created by ivan on 25/04/17.
 */
public class Application {
    private JPanel mainPanel;
    private TaskManager taskManager;
    private JPanel[][] gridPanel;
    private DefaultListModel model;
    private JButton addTaskBtn, deleteTaskBtn, modifyTaskBtn;
    private JList taskJList;
    private JLabel stopWatchLabel, countdownLabel;
    private JButton startTimer, stopTimer, resetTimer;
    private Task currentTask;
    private TimerThread timerThread;
    public Application(TaskManager taskManager) {
        this.taskManager = taskManager;
        mainPanel = new JPanel(new GridLayout(2, 2));
        gridPanel = new JPanel[2][2];
        for (int i = 0; i < 2; i++) for (int j = 0; j < 2; j++) {
            gridPanel[i][j] = new JPanel();
            mainPanel.add(gridPanel[i][j]);
        }

        addTaskBtn = new JButton("Add new task");
        modifyTaskBtn = new JButton("Modify selected task");
        deleteTaskBtn = new JButton("Delete selected task");
        startTimer = new JButton("Play");
        stopTimer = new JButton("Stop");
        resetTimer = new JButton("Reset");
        model = new DefaultListModel();
        taskJList = new JList(model);
        stopWatchLabel = new JLabel("00:00:00");
        countdownLabel = new JLabel("--:--:--");

//        model.add(2, "Code all night");
        gridPanel[0][0].add(taskJList);
        gridPanel[0][1].add(addTaskBtn);
        gridPanel[0][1].add(modifyTaskBtn);
        gridPanel[0][1].add(deleteTaskBtn);
        gridPanel[1][1].add(new JLabel("Total task time: "));
        gridPanel[1][1].add(stopWatchLabel);
        gridPanel[1][1].add(new JLabel("Time remaining: "));
        gridPanel[1][1].add(countdownLabel);
        gridPanel[1][1].add(startTimer);
        gridPanel[1][1].add(stopTimer);
        gridPanel[1][1].add(resetTimer);

        setTaskListeners();
        setTimerListeners();

        timerThread = new TimerThread(currentTask, stopWatchLabel, countdownLabel);
        timerThread.start();

        updateTimers();
    }

    private void updateTimers() {
        while (true) {
            if (1 == 1) break;
            if (currentTask != null) {
                currentTask.getStopWatch().run();
                currentTask.getCountdownTimer().run();
                Date date; String formattedDate;
                date = new Date((long)currentTask.getRunningTime());
                formattedDate = new SimpleDateFormat("HH/mm/ss").format(date);
                stopWatchLabel.setText(formattedDate);
                date = new Date((long)currentTask.getCountdownTimer().getCurrentTime());
                formattedDate = new SimpleDateFormat("HH/mm/ss").format(date);
                countdownLabel.setText(formattedDate);
            }
        }
    }

    private void setTimerListeners() {
        startTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.startTimers();
                } catch (Exception e) {};

            }
        });
        stopTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.stopTimers();
                } catch (Exception e) {};

            }
        });
        resetTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
//                    Task task = taskManager.getTaskAtIndex(selectedIndex);
                    timerThread.resetTimers();
                } catch (Exception e) {};

            }
        });
    }
    private void setTaskListeners() {
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        System.out.println("Double-clicked on: " + o.toString());
                    }
                }
            }
        };
        taskJList.addMouseListener(mouseListener);

        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                System.out.print("First index: " + listSelectionEvent.getFirstIndex());
                System.out.print(", Last index: " + listSelectionEvent.getLastIndex());
                boolean changed = listSelectionEvent.getFirstIndex() != listSelectionEvent.getLastIndex();
                if (changed) {
                    if (currentTask != null && !currentTask.getStopWatch().getIsStopped()) {
                        JOptionPane.showMessageDialog(null, "You have not stopped the clock");
                        taskJList.setSelectedIndex(listSelectionEvent.getFirstIndex());
                        return;
                    }
                }
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                System.out.println(", Adjusting? " + adjust);
                try {
                    synchronized (this) {
                        int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                        currentTask = taskManager.getTask(id);
                        timerThread.setCurrentTask(currentTask);
                    }

                } catch (Exception e) {}
                if (!adjust) {
                    JList list = (JList) listSelectionEvent.getSource();
                    int selections[] = list.getSelectedIndices();
                    Object selectionValues[] = list.getSelectedValues();
                    for (int i = 0, n = selections.length; i < n; i++) {
                        if (i == 0) {
                            System.out.print("  Selections: ");
                        }
                        System.out.print(selections[i] + "/" + selectionValues[i] + " ");
                    }
                    System.out.println();
                }
            }
        };
        taskJList.addListSelectionListener(listSelectionListener);

        addTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();
                Object[] message = {
                        "Task name:", jTaskName,
                        "Priority:", jTaskPriority,
                        "Task Time (in minutes): ", jTaskTime
                };
                int option = JOptionPane.showConfirmDialog(null, message, "New task", JOptionPane.OK_CANCEL_OPTION);

                String taskName = jTaskName.getText();

                String selectedPriority = jTaskPriority.getSelectedItem().toString();
                Priority taskPriority = Priority.HIGH;
                if (selectedPriority.equals("High")) taskPriority = Priority.HIGH;
                else if (selectedPriority.equals("Medium")) taskPriority = Priority.MEDIUM;
                else if (selectedPriority.equals("Low")) taskPriority = Priority.LOW;

                if (option == JOptionPane.OK_OPTION) {
                    if (!taskName.equals("")) {
                        try {
                            int taskTime = Integer.parseInt(jTaskTime.getText());
                            taskManager.addTask(taskName, taskPriority, taskTime);
                            System.out.println("Task successfully added");
                            updateTaskJList();
                        } catch (NumberFormatException e) {

                        }
                    }
                }
            }
        });

        modifyTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String taskName;
                int taskTime;
                Priority taskPriority = Priority.HIGH;
                String[] priorities = {"High", "Medium", "Low"};
                JTextField jTaskName = new JTextField();
                JComboBox<String> jTaskPriority = new JComboBox<>(priorities);
                JTextField jTaskTime = new JTextField();

                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }
                try {
                    int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                    taskName = taskManager.getTask(id).getName();
                    jTaskName.setText(taskName);
                    taskTime = taskManager.getTask(id).getTaskTime();
                    jTaskTime.setText(String.valueOf(taskTime));
                    taskPriority = taskManager.getTask(id).getPriority();
                    jTaskPriority.setSelectedIndex(taskPriority.ordinal());
                } catch (Exception e) {};
                Object[] message = {
                        "Task name:", jTaskName,
                        "Priority:", jTaskPriority,
                        "Task Time (in minutes): ", jTaskTime
                };
                int option = JOptionPane.showConfirmDialog(null, message, "Modify task", JOptionPane.OK_CANCEL_OPTION);

                taskName = jTaskName.getText();

                String selectedPriority = jTaskPriority.getSelectedItem().toString();

                if (selectedPriority.equals("High")) taskPriority = Priority.HIGH;
                else if (selectedPriority.equals("Medium")) taskPriority = Priority.MEDIUM;
                else if (selectedPriority.equals("Low")) taskPriority = Priority.LOW;

                if (option == JOptionPane.OK_OPTION) {
                    if (!taskName.equals("")) {
                        try {
                            taskTime = Integer.parseInt(jTaskTime.getText());
                            try {
                                int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                                currentTask = taskManager.modifyTask(id, taskName, taskPriority, taskTime);
                                timerThread.setCurrentTask(currentTask);
                                System.out.println("Task successfully modified");
                                updateTaskJList();
                            } catch (Exception e) {

                            }
                        } catch (NumberFormatException e) {

                        }

                    }
                }
            }
        });

        deleteTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedIndex = taskJList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "You have not selected a task yet");
                    return;
                }

                String message = "Are you sure you want to delete this task?";
                int option = JOptionPane.showConfirmDialog(null, message, "Delete task", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        int id = taskManager.parseId(taskJList.getSelectedValue().toString());
                        Task deletedTask = taskManager.getTask(id);
                        if (deletedTask == currentTask) {
                            stopWatchLabel.setText("00:00:00");
                            countdownLabel.setText("--:--:--");
                            currentTask = null;
                            timerThread.setCurrentTask(currentTask);
                        }
                        taskManager.deleteTask(id);
                        System.out.println("Task successfully deleted");
                        updateTaskJList();
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void updateTaskJList() {
        model.removeAllElements();
        ArrayList<Task> taskList = taskManager.getTaskList();
        ArrayList<Task> clonedTaskList = new ArrayList<Task>();
        for (Task t : taskList) clonedTaskList.add(new Task(t));
        Collections.sort(clonedTaskList);
        for (int i = 0; i < clonedTaskList.size(); i++) {
            Task t = clonedTaskList.get(i);
            model.add(i, String.valueOf(t.getId()) + ' ' + t.getName());
        }
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
