package com.guo.ui;

import com.guo.db.StudentDatabaseManager;
import com.guo.entity.Student;

import javax.swing.*;
import java.awt.*;


// 自定义的编辑器，用于处理表格中按钮的点击事件
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private static final StudentDatabaseManager dbManager = new StudentDatabaseManager();

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped()
        );
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            // 按钮点击事件处理
            JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
            int row = table.getEditingRow();
            Integer studentId = (Integer) table.getValueAt(row, 0);
            Student student = dbManager.getStudentById(studentId);
            if (student != null) {
                showStudentPortrait(student.getPortraitPath());
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }


    private static void showStudentPortrait(String portraitPath) {
        // 创建一个新的窗口来显示学生的照片
        JFrame portraitFrame = new JFrame("学生照片");
        portraitFrame.setSize(300, 400);
        portraitFrame.setLocationRelativeTo(null);

        // 将照片加载为一个图像对象
        ImageIcon portraitIcon = new ImageIcon(portraitPath);
        JLabel portraitLabel = new JLabel(portraitIcon);

        // 将图像添加到窗口中
        portraitFrame.getContentPane().add(portraitLabel);
        portraitFrame.setVisible(true);
    }


}


