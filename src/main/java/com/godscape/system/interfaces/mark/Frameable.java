package com.godscape.system.interfaces.mark;

import javax.swing.*;

public interface Frameable {

    JButton createButton(String text);

    JCheckBox createCheckBox(String text);

    <E> JList<E> createList(ListModel<E> model);

    JScrollPane createScrollPane(JComponent component);
}
