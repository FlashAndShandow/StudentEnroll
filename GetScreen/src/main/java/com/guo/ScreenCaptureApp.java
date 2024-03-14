package com.guo;

import com.guo.db.StudentDatabaseManager;
import com.guo.entity.Student;
import com.guo.ui.ButtonEditor;
import com.guo.ui.ButtonRenderer;
import com.guo.util.ScreenCapture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScreenCaptureApp {
    private static final StudentDatabaseManager dbManager = new StudentDatabaseManager();
    private static final JLabel imageLabel = new JLabel(); // 用于展示图片的标签
    private static final JTable table = getjTable(); // 初始化表格

    public static void main(String[] args) {
        JFrame frame = new JFrame("学生信息录入系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JButton saveStudentInfoButton = new JButton("录入学生信息");
        saveStudentInfoButton.addActionListener(e -> showStudentInfoForm(frame));

        // 创建表格模型，包含展示图片的按钮列
        JTable table = getjTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // 设置表格的渲染器和编辑器为按钮
        table.getColumn("Show Portrait").setCellRenderer(new ButtonRenderer());
        table.getColumn("Show Portrait").setCellEditor(new ButtonEditor(new JCheckBox()));

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(saveStudentInfoButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JTable getjTable() {
        DefaultTableModel tableModel = getDefaultTableModel();

        java.util.List<Student> students = dbManager.getAllStudents();
        for (Student student : students) {
            Object[] row = new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getIdentityNumber(),
                    "Show Portrait"
            };
            tableModel.addRow(row);
        }

        return new JTable(tableModel);
    }

    private static void refreshTable() {
        DefaultTableModel tableModel = getDefaultTableModel();
        java.util.List<Student> students = dbManager.getAllStudents();
        for (Student student : students) {
            Object[] row = new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getIdentityNumber(),
                    "Show Portrait"
            };
            tableModel.addRow(row);
        }
        table.setModel(tableModel); // 更新表格模型
        table.repaint(); // 通知表格重绘
    }


    private static DefaultTableModel getDefaultTableModel() {
        String[] columnNames = {"Student ID", "Name", "Identity Number", "Show Portrait"};
        return new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // 只有按钮列可编辑
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? JButton.class : Object.class; // 按钮列使用JButton类型
            }
        };
    }


    private static void showStudentInfoForm(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "学生信息", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 300);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameTextField = new JTextField(20);
//        nameTextField.setText("张三");
        JTextField identityTextField = new JTextField(20);
//        identityTextField.setText("1234567812345678");

        JButton captureButton = new JButton("开始屏幕截图");
        String[] imagePath = new String[1]; // 使用数组来存储路径，以便在匿名内部类中修改

        captureButton.addActionListener(e -> {
            // 创建ScreenCapture实例并执行单次截图
            ScreenCapture screenCap = new ScreenCapture();
            imagePath[0] = screenCap.captureOnce(); // 假设这里返回最近保存的截图路径
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath[0]).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            imageLabel.setIcon(imageIcon);
        });

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            dbManager.insertStudent(nameTextField.getText(), identityTextField.getText(), imagePath[0]);
            refreshTable(); // 刷新主表单的表格
            dialog.dispose();
        });


        dialog.add(new JLabel("姓名:"), gbc);
        dialog.add(nameTextField, gbc);
        dialog.add(new JLabel("身份证号:"), gbc);
        dialog.add(identityTextField, gbc);
        dialog.add(captureButton, gbc);
        dialog.add(saveButton, gbc);
        dialog.add(imageLabel, gbc); // 添加用于展示图片的标签

        dialog.setVisible(true);
    }

    private static JButton getjButton(JTextField nameTextField, JTextField identityTextField) {
        JButton captureButton = new JButton("开始屏幕截图");
        captureButton.addActionListener(e -> {
            // 创建ScreenCapture实例并执行单次截图
            ScreenCapture screenCap = new ScreenCapture();
            String imagePath = screenCap.captureOnce(); // 假设这里返回最近保存的截图路径
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            imageLabel.setIcon(imageIcon);
            dbManager.insertStudent(nameTextField.getText(), identityTextField.getText(), imagePath);
        });
        return captureButton;


    }
}
