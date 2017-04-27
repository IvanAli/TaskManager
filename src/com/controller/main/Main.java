package com.controller.main;

import com.controller.task.TaskManager;
import com.model.timer.CountdownTimerStrategy;
import com.model.timer.StopWatchStrategy;
import com.model.timer.TimeCounter;
import com.view.Application;

import javax.swing.*;
import java.awt.*;

/**
 * Main class that falls under the controller package.
 * Modules are divided in model, view and controller. Each source file can be found in its respective category.
 */
public class Main {
    /**
     * Gets an instance of Application and sets the GUI frame
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Getting the application object through the getInstance method (Singleton pattern)
         */
        Application app = Application.getInstance();
        /**
         * Creation of the GUI frame
         */
        JFrame frame = new JFrame("Task Manager");
        frame.setPreferredSize(new Dimension(400, 440));;
        frame.setContentPane(app.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
