package UI;

import javax.sound.sampled.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class featureTools {


    public static void deleteDir(File dir){
        File[] contents = dir.listFiles();
        if (contents != null){
            for (File f: contents){
                if (!Files.isSymbolicLink(f.toPath())){
                    deleteDir(f);
                }
            }
        }
        dir.delete();
    }

    private static void Btn_Onclick_sfx() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //here I duplicated the line because some of us require the PingUI before src for the path and some no
        //Just comment / un-comment the line that works for you
        AudioInputStream audio = AudioSystem.getAudioInputStream(new File("src/SoundEffects/Pokemon_btn_sfx.wav"));
        //AudioInputStream audio = AudioSystem.getAudioInputStream(new File("src/SoundEffects/Pokemon_btn_sfx.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
    }

    public static void plat_sfx(){
        try{
            Btn_Onclick_sfx();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            throw new RuntimeException(e);
        }
    }

    public static String createFilePath(TreePath treePath, boolean isOpen)
    {
        StringBuilder sb = new StringBuilder();
        String homeDir = System.getProperty("user.home");
        sb.append(homeDir);
        Object[] nodes = treePath.getPath();
        int i = 0;
        if (isOpen)
            i += 1;
        for (; i < nodes.length; i++)
                sb.append(File.separatorChar).append(nodes[i].toString());
        return sb.toString();
    }
}
