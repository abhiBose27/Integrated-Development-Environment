package UI;

import java.io.File;

public class TreeFile extends File {
    public TreeFile(String dir){
        super(dir);
    }

    public TreeFile(TreeFile parent, String child) {
        super(parent, child);
    }


    public String toString() {
        return getName();
    }
}
