package UI;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;

public class FileSystemModel implements TreeModel {

    private TreeFile root;
    private Vector<TreeModelListener> listeners = new Vector<>();

    public FileSystemModel(String rootDir){
        root = new TreeFile(rootDir);
    }
    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        TreeFile directory = (TreeFile) parent;
        File[] children = directory.listFiles();
        assert children != null;
        sortFileChildren(children);
        return new TreeFile(directory, children[index].getName());
    }

    private void sortFileChildren(File[] children){
        Arrays.parallelSort(children, (f1, f2) -> {
            if (f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            } else if (!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    @Override
    public int getChildCount(Object parent) {
        TreeFile file = (TreeFile) parent;
        if (file != null && file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null)
                return fileList.length;
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        File file = (File) node;
        return file.isFile();
    }

    public void reload(Object node, TreePath parent){
        if (node != null)
            fireTreeStructureChanged(this, parent, null, null);
    }


    @Override
    public void valueForPathChanged(TreePath path, Object value) {
        File oldFile = (File) path.getLastPathComponent();
        String fileParentPath = oldFile.getParent();
        String newFileName = (String) value;
        File targetFile = new File(fileParentPath, newFileName);
        oldFile.renameTo(targetFile);
        File parent = new File(fileParentPath);
        int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
        Object[] changedChildren = { targetFile };
        fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
    }

    private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
        TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
        Iterator iterator = listeners.iterator();
        TreeModelListener listener = null;
        while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeNodesChanged(event);
        }
    }

    private void fireTreeStructureChanged(Object source, TreePath Parentpath, int[] indices, Object[] children){
        TreeModelEvent event = new TreeModelEvent(source, Parentpath, indices, children);
        Iterator iterator = listeners.iterator();
        TreeModelListener listener = null;
        while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeStructureChanged(event);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        TreeFile parentNode = (TreeFile) parent;
        TreeFile childNode = (TreeFile) child;
        File[] children = parentNode.listFiles();
        assert children != null;
        sortFileChildren(children);
        //return Arrays.asList(children).indexOf(childNode.getName());
        return Arrays.asList(children).indexOf(childNode);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

}
