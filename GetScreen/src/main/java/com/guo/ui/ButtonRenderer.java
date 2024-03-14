package com.guo.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// 自定义渲染器，用于渲染按钮
public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
