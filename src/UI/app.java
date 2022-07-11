package UI;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;



public class app  extends JFrame{
    private JPanel mainPanel;
    private JPanel topBar;
    private JButton runButton;
    private JTabbedPane tabbedPane1;
    private JPanel codeBLocks;
    private JPanel BottomPanel;
    private JToolBar topTool;
    private JToolBar bottomTool;
    private JButton newButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton undo;
    private JButton play_btn;

    private JTextArea ConsoleText;
    private JButton pause_btn;
    private JTabbedPane openedFilesPanel;
    //private JTextPane textPane1;
    private JTabbedPane sidePanel;
    private JTree tree1;
    private JPanel symblosTab;
    private JPanel projectsTab;
    private JPanel filesTab;
    private JButton deleteBtn;
    private JButton redo_Btn;
    private Clip theme_clip;
    private long clipTime;
    private boolean is_paused = false;

    Color lightGray = new Color(242, 242, 242);
    private String curr_Project;

    List<Map.Entry<RSyntaxTextArea, String>> tabList = new ArrayList<>();

    public float getVolume() {
        FloatControl gainControl = (FloatControl) theme_clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) theme_clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void PlayMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //here I duplicated the line because some of us require the PingUI before src for the path and some no
        //Just comment / un-comment the line that works for you
        Random random = new Random();
        int index = random.nextInt(7);
        File musicPath = new File("src/SoundEffects/main-theme.wav");
        if (musicPath.exists()) {
            AudioInputStream audio = AudioSystem.getAudioInputStream(musicPath);
            theme_clip = AudioSystem.getClip();
            theme_clip.open(audio);

            theme_clip.start();
            setVolume((float) 0.2);
        }
        else
        {
            System.out.println("Cannot find audio file");
        }
    }

    public void change_Btn_font(){
        //check line 65
        File is = new File("src/Fonts/Pokemon Solid.ttf");
        //File is = new File("src/Fonts/Pokemon Solid.ttf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            Font sizedFont = font.deriveFont(20f);
            Font treeFont = font.deriveFont(17f);
            Font welocmeFont = font.deriveFont(80f);
            newButton.setFont(sizedFont);
            openButton.setFont(sizedFont);
            saveButton.setFont(sizedFont);
            undo.setFont(sizedFont);
            redo_Btn.setFont(sizedFont);
            play_btn.setFont(sizedFont);
            pause_btn.setFont(sizedFont);
            runButton.setFont(sizedFont);
            deleteBtn.setFont(sizedFont);
            tree1.setFont(treeFont);
            sidePanel.setFont(treeFont);
            tabbedPane1.setFont(treeFont);
            openedFilesPanel.setFont(treeFont);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBkImage(){
        ImageIcon image = new ImageIcon("src/Icons/welcome.jpg");


    }

    public void openfile_Btn(TreePath selPath)
    {
        if (is_dir(selPath))
            return;
        String title = selPath.getLastPathComponent().toString();

        int n = openedFilesPanel.indexOfTab(title);
        if (n != -1) {
            System.out.println("file already open");
            openedFilesPanel.setSelectedIndex(n);
            return;
        }
        TreeFile node = (TreeFile)selPath.getLastPathComponent();
        String path = node.getAbsolutePath();
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        String fileName = selPath.getLastPathComponent().toString();

        String content = "";
        File file = new File(path);
        if (file.exists()) {
            try {
                content = Files.readString(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //check the type of the file and set the syntax highlight
        if (fileName.matches("^.*\\.java$"))
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        else if (fileName.matches("^.*\\.c$"))
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        else if (fileName.matches("^.*\\.cpp$") || fileName.matches("^.*\\.cc$"))
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        else if (fileName.matches("^.*\\.js$"))
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        else
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        
        textArea.setCodeFoldingEnabled(true);
        textArea.append(content);
        textArea.discardAllEdits();
        RTextScrollPane sp = new RTextScrollPane(textArea);

        Map.Entry<RSyntaxTextArea,String> tab = new AbstractMap.SimpleEntry<>(textArea, path);
        tabList.add(tab);
        openedFilesPanel.addTab(title, sp);
        int index = openedFilesPanel.indexOfTab(title);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JButton btnClose = new JButton("x");

        JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);



        GridBagConstraints gbc = new GridBagConstraints();
        int top = 0;
        int left = 10;
        int bottom = 0;
        int right = 0;
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;

        pnlTab.add(btnClose, gbc);

        openedFilesPanel.setTabComponentAt(index, pnlTab);
        openedFilesPanel.setSelectedIndex(index);

        class MyCloseActionHandler implements ActionListener {

            private String tabName;

            public MyCloseActionHandler(String tabName) {
                this.tabName = tabName;
            }

            public String getTabName() {
                return tabName;
            }

            public void actionPerformed(ActionEvent evt) {

                int index = openedFilesPanel.indexOfTab(getTabName());
                tabList.remove(index);
                openedFilesPanel.removeTabAt(index);
            }

        }
        btnClose.addActionListener(new MyCloseActionHandler(title));
    }




    public boolean is_dir(TreePath selPath)
    {
        return !tree1.getModel().isLeaf(selPath.getLastPathComponent());
    }

    public app(String title){
        super(title);


        theme_clip = null;
        clipTime = 0;
        change_Btn_font();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        ImageIcon image = new ImageIcon("src/Icons/pokemonlogo.png");
        this.setIconImage(image.getImage());
        //PrintStream printStream = new PrintStream(new CustomOutputStream(ConsoleText));
        //System.setOut(printStream);
        //System.setErr(printStream);
        MessageConsole mc = new MessageConsole(ConsoleText);
        //mc.redirectOut();
        //mc.redirectErr();
        mc.redirectOut(Color.YELLOW, System.out);
        //mc.redirectOut(Color.YELLOW,System.err);
        mc.redirectErr(Color.RED, System.err);
        //mc.setMessageLines(100);
        System.out.println("Welcome, fellow Pokemon Trainer");

        this.pack();
        tree1.setModel(null);

        setBkImage();



        // For having open file tabs
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree1.getRowForLocation(e.getX(), e.getY());
                //TreePath selPath = tree1.getPathForLocation(e.getX(), e.getY());
                TreePath selPath = tree1.getSelectionPath();
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        // mySingleClick(selRow, selPath);
                    }
                    else if(e.getClickCount() == 2) {
                        openfile_Btn(selPath);
                    }
                }
            }
        };
        tree1.addMouseListener(ml);

        // New Button mouse listener and actionListener
        newButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                newButton.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                newButton.setBackground(lightGray);
            }
        });

        redo_Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                redo_Btn.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                redo_Btn.setBackground(lightGray);
            }
        });

        redo_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                featureTools.plat_sfx();
            }
        });

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    featureTools.plat_sfx();
                    if (tree1.getSelectionPath() != null){
                        var selectedNode = tree1.getSelectionPath();
                        if (selectedNode != null) {
                            try {
                                CreateNewNode(selectedNode);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    else
                        System.out.println("Select a directory");
            }
        });

        // Play_btn Mouse and action listener
        play_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                play_btn.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                play_btn.setBackground(lightGray);
            }
        });

        play_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureTools.plat_sfx();
                if (theme_clip != null && is_paused)
                {
                    theme_clip.setMicrosecondPosition(clipTime);
                    is_paused = false;
                    theme_clip.start();
                    return;
                }
                if (theme_clip != null && theme_clip.isRunning() && !is_paused)
                    theme_clip.stop();
                if (!pause_btn.isEnabled())
                {
                    pause_btn.setVisible(true);
                    pause_btn.setEnabled(true);
                }
                try {
                    PlayMusic();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Pause Btn action Listener
        pause_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureTools.plat_sfx();
                if (theme_clip.isRunning())
                {
                    clipTime = theme_clip.getMicrosecondPosition();
                    theme_clip.stop();
                    is_paused = true;
                }
            }
        });

        pause_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                pause_btn.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                pause_btn.setBackground(lightGray);
            }
        });

        // Open Button action and mouse listener
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                openButton.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                openButton.setBackground(lightGray);
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    featureTools.plat_sfx();
                    openProject();

            }
        });

        // Save btn action and mouse listener
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                saveButton.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                saveButton.setBackground(lightGray);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = openedFilesPanel.getSelectedIndex();
                if (index == -1){
                    featureTools.plat_sfx();
                    return;
                }
                RSyntaxTextArea textArea = tabList.get(index).getKey();
                String Path = tabList.get(index).getValue();
                try {
                    featureTools.plat_sfx();
                    FileWriter writer = new FileWriter(Path);
                    //System.out.println(Path);
                    writer.write(textArea.getText());
                    writer.close();
                    System.out.println("file saved");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Undo Btn action and mouse listener
        undo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                undo.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                undo.setBackground(lightGray);
            }
        });
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureTools.plat_sfx();
                int index = openedFilesPanel.getSelectedIndex();
                if (index == -1){
                    featureTools.plat_sfx();
                    return;
                }
                RSyntaxTextArea textArea = tabList.get(index).getKey();
                textArea.undoLastAction();
            }
        });

        // Delete btn mouse and action listener
        deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                deleteBtn.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                deleteBtn.setBackground(lightGray);
            }
        });


        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                featureTools.plat_sfx();
                if (tree1.getSelectionPath() != null){
                    var selectedNode = tree1.getSelectionPath();
                    if (selectedNode != null)
                        deleteNode(selectedNode);
                }
                else
                    System.out.println("Select a file or directory to delete");

            }
        });

        runButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                runButton.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                runButton.setBackground(lightGray);
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureTools.plat_sfx();
            }
        });

        sidePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                sidePanel.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                sidePanel.setBackground(lightGray);
            }
        });

        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                tabbedPane1.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                tabbedPane1.setBackground(lightGray);
            }
        });


        openedFilesPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e))
                {
                    int index = openedFilesPanel.getSelectedIndex();
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem delete = new JMenuItem("Delete");
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openedFilesPanel.remove(index);
                            tabList.remove(index);
                        }
                    });
                    popupMenu.add(delete);
                    popupMenu.show(openedFilesPanel,e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        redo_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = openedFilesPanel.getSelectedIndex();
                if (index == -1){
                    featureTools.plat_sfx();
                    return;
                }
                RSyntaxTextArea textArea = tabList.get(index).getKey();
                textArea.redoLastAction();
            }
        });
        MouseListener comp = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ConsoleText.setText("");
                //int selRow = tree1.getRowForLocation(e.getX(), e.getY());
                //TreePath selPath = tree1.getPathForLocation(e.getX(), e.getY());
                TreePath selPath = tree1.getSelectionPath();
                if (selPath != null) {
                    if (e.getClickCount() == 1) {
                        var compiler = new MyCompiler.CompileAndRun(selPath);
                        try {
                            compiler.compile();
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        };
        runButton.addMouseListener(comp);
    }


    public void deleteNode(TreePath selectedNode){
        //File fileDir = new File(UI.featureTools.createFilePath(selectedNode, false));
        TreeFile node = (TreeFile) selectedNode.getLastPathComponent();
        File fileDir = new File(node.getAbsolutePath());
        if (fileDir.isDirectory())
            UI.featureTools.deleteDir(fileDir);
        else
            fileDir.delete();
        if (selectedNode.getParentPath() == null){
            curr_Project = null;
            tree1.setModel(null);
            return;
        }
        ((FileSystemModel)tree1.getModel()).reload(selectedNode, selectedNode.getParentPath());
    }

    private void loadProject(String curr_Project){
        FileSystemModel fileSystemModel = new FileSystemModel(curr_Project);
        tree1.setModel(fileSystemModel);
    }

    public void openProject(){
        String usrHomeDir = System.getProperty("user.home");
        var dialog = new openDialogBox(usrHomeDir);
        dialog.setForeground(Color.BLACK);
        dialog.setBackground(Color.lightGray);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if (dialog.isOk()){
            TreeFile treeNode = dialog.getSelectedNode();
            if (treeNode != null) {
                curr_Project = treeNode.getAbsolutePath();
                loadProject(curr_Project);
            }
        }
    }


    //For adding nodes in JTree
    public void CreateNewNode(TreePath selectedNode) throws IOException {
        var dialogNewFile = new NameNewFile();
        dialogNewFile.pack();
        dialogNewFile.setVisible(true);
        if (dialogNewFile.isOnOk()){
            var textarea = dialogNewFile.getTextArea1();
            var nodeName = textarea.getText().replaceAll("\\s","");
            boolean isDir = true;
            String path = "";

            TreeFile node = (TreeFile) selectedNode.getLastPathComponent();
            // if selected node is a leaf
            if (!is_dir(selectedNode)) {
                if (!node.isDirectory()) {
                    if (selectedNode.getParentPath() == null){
                        System.out.println("You are not in the project");
                        return;
                    }
                    node = (TreeFile) selectedNode.getParentPath().getLastPathComponent();
                }
                isDir = false;
            }
            path = node.getAbsolutePath();

            String Path = path + File.separatorChar + nodeName;
            File file = new File(Path);
            if (file.createNewFile())
                System.out.println("File creation successful");
            else
                System.out.println("Error while creating a file. File exists");

            if (isDir) {
                TreePath newPath = selectedNode.pathByAddingChild(nodeName);
                ((FileSystemModel) tree1.getModel()).reload(newPath, newPath.getParentPath());
            }
            else{
                ((FileSystemModel) tree1.getModel()).reload(selectedNode, selectedNode.getParentPath());
            }
        }

    }

    public static void main(String[] args) {
        JFrame frame = new app("Pokedex IDE");
        frame.setSize(1500, 720);
        frame.setVisible(true);
    }
}
