package UI;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class openDialogBox extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel DialogPanel;
    private JPanel treePanel;
    private JTree tree1;

    private String homeDir;

    private boolean isOk = false;

    public boolean isOk() {
        return isOk;
    }

    private TreeFile selectedNode = null;

    public TreeFile getSelectedNode() {
        return selectedNode;
    }

    private FileSystemModel fileSystemModel;

    public openDialogBox(String homeDir) {
        setContentPane(contentPane);
        setModal(true);
        this.homeDir = homeDir;

        fileSystemModel = new FileSystemModel(homeDir);
        tree1.setModel(fileSystemModel);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    }

    private void onOK() {
        // add your code here
        if (tree1.getSelectionPath() != null){
            selectedNode = (TreeFile) tree1.getSelectionPath().getLastPathComponent();
        }
        isOk = true;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
