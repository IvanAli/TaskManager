package com.main;

import com.TaskManagerGUI;
import com.task.Priority;
import com.task.Task;
import com.task.TaskManager;
import com.timer.*;
import view.Application;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        TaskManager taskManager = new TaskManager();
        TimeCounter stopWatch = new TimeCounter(new StopWatchStrategy());
        TimeCounter countdownTimer = new TimeCounter(new CountdownTimerStrategy());
        Application gui = new Application(taskManager, stopWatch, countdownTimer);
        JFrame frame = new JFrame("Task Manager");
        frame.setPreferredSize(new Dimension(400, 440));;
        frame.setContentPane(gui.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
