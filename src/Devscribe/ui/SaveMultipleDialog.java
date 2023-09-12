package Devscribe.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;

public class SaveMultipleDialog extends JDialog implements ActionListener {
    private EditorUI editorUI;
    private JList<TextEditor> fileList;
    private JButton btnSave, btnSaveAll, btnIgnoreAll, btnCancel;

    static final int OPERATION_CANCELED = 1;
    static final int OPERATION_COMPLETED = 0;

    private int returnValue = OPERATION_COMPLETED;

    private int itemCount;

    // Constructor for creating the SaveMultipleDialog.
    private SaveMultipleDialog(JFrame parent, java.util.List<TextEditor> fileList) {
        super(parent, "Save Changes", true);
        setSize(370, 170);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("icon/Devscribe.png")).getImage());

        // Listener to handle window closing.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnValue = OPERATION_CANCELED;
            }
        });

        editorUI = (EditorUI) parent;
        itemCount = fileList.size();

        GridBagConstraints constraints = new GridBagConstraints();

        // Row 0, Column 0.
        constraints.gridx = constraints.gridy = 0;
        constraints.gridheight = 4;
        constraints.weightx = constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Create and populate a JList with text editors.
        this.fileList = new JList<>(new DefaultListModel<TextEditor>());
        DefaultListModel<TextEditor> listModel = (DefaultListModel<TextEditor>) this.fileList.getModel();
        for (TextEditor editor : fileList)
            listModel.addElement(editor);

        this.fileList.setSelectedIndex(0);
        add(new JScrollPane(this.fileList), constraints);

        // Row 0, Column 1.
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.weighty = constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 0, 10, 10);

        // Buttons for Save, Save All, Ignore All, and Cancel.
        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        add(btnSave, constraints);

        // Row 1, Column 1.
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 10, 10);

        btnSaveAll = new JButton("Save All");
        btnSaveAll.addActionListener(this);
        add(btnSaveAll, constraints);

        // Row 2, Column 1.
        constraints.gridy = 2;

        btnIgnoreAll = new JButton("Ignore All");
        btnIgnoreAll.addActionListener(this);
        add(btnIgnoreAll, constraints);

        // Row 3, Column 1.
        constraints.gridy = 3;

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        add(btnCancel, constraints);

        setVisible(true);
    }

    // Static method to show the dialog and return the user's choice.
    static int showDialog(JFrame parent, java.util.List<TextEditor> fileList) {
        return new SaveMultipleDialog(parent, fileList).returnValue;
    }

    // ActionListener implementation to handle button actions.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            int selectedItemCount = fileList.getSelectedIndices().length;

            // Check if one or more items are selected.
            if (selectedItemCount != 0) {
                // Get the count of items that won't be removed.
                itemCount -= selectedItemCount;
                for (TextEditor editor : fileList.getSelectedValuesList()) {
                    editorUI.saveFile(editor);

                    if (itemCount != 0)
                        ((DefaultListModel<TextEditor>) fileList.getModel()).removeElement(editor);
                }

                switch (itemCount) {
                    case 0:
                        dispose(); // Close.
                        break;
                    case 1:
                        btnSaveAll.setEnabled(false);
                        btnIgnoreAll.setText("Don't Save");
                        break;
                }
            }

        } else {
            if (e.getSource() == btnSaveAll) {
                for (int i = 0; i < fileList.getModel().getSize(); i++) {
                    TextEditor editor = fileList.getModel().getElementAt(i);
                    editorUI.saveFile(editor);
                }

            } else {
                if (e.getSource() == btnCancel)
                    returnValue = OPERATION_CANCELED;
            }

            dispose(); // Close.
        }
    }
}
