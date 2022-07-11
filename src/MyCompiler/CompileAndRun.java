package MyCompiler;

import UI.TreeFile;

import javax.swing.tree.TreePath;
import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CompileAndRun {
    private final String file_path;
    private PrintStream printStream;
    private final String filename;

    public CompileAndRun(TreePath file_path) {
        this.filename = file_path.getLastPathComponent().toString();
        TreeFile node = (TreeFile)file_path.getLastPathComponent();
        this.file_path = node.getAbsolutePath();
    }
    public static String createFilePath(TreePath treePath) {
        StringBuilder sb = new StringBuilder();
        Object[] nodes = treePath.getPath();
        for(int i=0;i<nodes.length;i++) {
            sb.append(File.separatorChar).append(nodes[i].toString());
        }
        return sb.toString();
    }


    public void compile() throws IOException, InterruptedException {
        if (filename.matches("^.*\\.java$")) {
            Java_compile();
        }
        else if (filename.matches("^.*\\.c$")) {
            C_compile();


        }
        else if (filename.matches("^.*\\.cpp$") || filename.matches("^.*\\.cc$")) {
            Cpp_compile();
        }
        else if (filename.matches("^.*\\.js$")) {
            Js_compile();
        }
        else
            System.out.println("Language not supported");
    }

    private void C_compile() throws IOException, InterruptedException {

        File dir = new File(file_path).getParentFile();
        try {
            //System.out.println(cmd);
            ProcessBuilder pb = new ProcessBuilder("gcc", file_path);
            pb.directory(dir);
            Process p = pb.start();
            try (BufferedReader input =
                         new BufferedReader(new
                                 InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
            p.waitFor();
            if (p.exitValue() !=0)
            {
                System.out.println("Exited with " + p.exitValue() + ". Abort");
                return;
            }

                ;
            //Process p = Runtime.getRuntime().exec(cmd, null, dir);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String OS = System.getProperty("os.name").toLowerCase();

        boolean IS_WINDOWS = (OS.contains("win"));
        boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
        ProcessBuilder pb2;
        if (IS_WINDOWS)
            pb2 = new ProcessBuilder(dir + "\\a.exe");
        else
            pb2 = new ProcessBuilder("./a.out");
        pb2.directory(dir);
       // pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
       // pb2.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p2 = pb2.start();
        try (BufferedReader input =
                     new BufferedReader(new
                             InputStreamReader(p2.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        }
        p2.waitFor();

        //Process p = Runtime.getRuntime().exec("./a.out", null, dir);
        //p.getOutputStream();
    }

    private void Cpp_compile() throws IOException, InterruptedException {
        File dir = new File(file_path).getParentFile();
        try {
            //System.out.println(cmd);
            ProcessBuilder pb = new ProcessBuilder("g++", filename);
            pb.directory(dir);
            Process p = pb.start();
            try (BufferedReader input =
                         new BufferedReader(new
                                 InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
            p.waitFor();
            if (p.exitValue() !=0)
            {
                System.out.println("Exited with " + p.exitValue() + ". Abort");
                return;
            }

            ;
            //Process p = Runtime.getRuntime().exec(cmd, null, dir);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String OS = System.getProperty("os.name").toLowerCase();

        boolean IS_WINDOWS = (OS.contains("win"));
        boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
        ProcessBuilder pb2;
        if (IS_WINDOWS)
            pb2 = new ProcessBuilder(dir + "\\a.exe");
        else
            pb2 = new ProcessBuilder("./a.out");
        pb2.directory(dir);
        // pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        // pb2.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p2 = pb2.start();
        try (BufferedReader input =
                     new BufferedReader(new
                             InputStreamReader(p2.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        }
        p2.waitFor();

        //Process p = Runtime.getRuntime().exec("./a.out", null, dir);
        //p.getOutputStream();
    }
    private void Js_compile() throws IOException {
        File dir = new File(file_path).getParentFile();
        try {
            //System.out.println(cmd);
            ProcessBuilder pb = new ProcessBuilder("node", file_path);
            pb.directory(dir);
            Process p = pb.start();
            try (BufferedReader input =
                         new BufferedReader(new
                                 InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
            p.waitFor();
            if (p.exitValue() !=0)
            {
                System.out.println("Exited with " + p.exitValue() + ". Abort");
                return;
            }
            //Process p = Runtime.getRuntime().exec(cmd, null, dir);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Process p = Runtime.getRuntime().exec("./a.out", null, dir);
        //p.getOutputStream();
    }
    private void Java_compile() throws InterruptedException, IOException {
        File dir = new File(file_path).getParentFile();
        try {
            //System.out.println(cmd);
            ProcessBuilder pb = new ProcessBuilder("javac", file_path);
            pb.directory(dir);
            Process p = pb.start();
            try (BufferedReader input =
                         new BufferedReader(new
                                 InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }
            p.waitFor();
            if (p.exitValue() !=0)
            {
                System.out.println("Exited with " + p.exitValue() + ". Abort");
                return;
            }

            ;
            //Process p = Runtime.getRuntime().exec(cmd, null, dir);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String OS = System.getProperty("os.name").toLowerCase();

        boolean IS_WINDOWS = (OS.contains("win"));
        boolean IS_UNIX = (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
        ProcessBuilder pb2;
        if (IS_WINDOWS)
            pb2 = new ProcessBuilder("java", file_path);
        else {
            File file = new File(file_path);
            File[] list = file.getParentFile().listFiles();
            assert list != null;
            var res = Arrays.stream(list) // not a directory
                    .map(File::toString) // convert path to string
                    .filter(f -> f.endsWith(".class")).toList();
            File execfile = new File(res.get(0));
            String name = execfile.getName();


            pb2 = new ProcessBuilder("java", name.replaceAll("\\.class", ""));
        }
        pb2.directory(dir);
        // pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        // pb2.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p2 = pb2.start();
        try (BufferedReader input =
                     new BufferedReader(new
                             InputStreamReader(p2.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        }
        p2.waitFor();

        //Process p = Runtime.getRuntime().exec("./a.out", null, dir);
        //p.getOutputStream();
    }



}
