package org.slkxy;

import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception{
        Writer.builder().green().build().write("Welcome to the Auto Push.");
        if(!isReady()){
            Writer.builder().green().build().write("Please ensure that Git and Github CLI is installed.");
            System.exit(0);
        }
        Writer.builder().green().build().write("Git and Github CLI is ready.");

        boolean gitRepoExists = exec(Arrays.asList("git", "status"));
        if(gitRepoExists){
            String remote = execWithOutput(Arrays.asList("git", "remote"));
            if(remote.isBlank()){
                exec(Arrays.asList("git","add","-A"));
                exec(Arrays.asList("git","commit","-m", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-D hh:mm:ss")) + " Auto Commit"));
                exec(Arrays.asList("git","branch","-m","main"));
                pushToRemote();
            }
            else{
                exec(Arrays.asList("git","add","-A"));
                exec(Arrays.asList("git","commit","-m", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-D hh:mm:ss")) + " Auto Commit"));
                exec(Arrays.asList("git","pull"));
                boolean pushIsOk = exec(Arrays.asList("git","push"));
                if(pushIsOk){
                    Writer.builder().green().build().write("Done!");
                }
                else Writer.builder().red().build().write("Error!");
            }
        }
        else{
            exec(Arrays.asList("git","init"));
            exec(Arrays.asList("git","add","-A"));
            exec(Arrays.asList("git","commit","-m", LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-D hh:mm:ss")) + " Auto Commit"));
            exec(Arrays.asList("git","branch","-m","main"));
            pushToRemote();
        }

    }

    private static void pushToRemote() throws Exception {
        String remoteName = readLine("Please input remote name").trim();
        boolean isPublic = readLine("Create a public repo (Y/N)").equalsIgnoreCase("Y");
        String visibility = isPublic ? "--public" : "--private";
        boolean pushIsOk = exec(Arrays.asList("gh", "repo", "create", remoteName, visibility, "--source=.", "--remote=origin", "--push"));
        if(pushIsOk){
            Writer.builder().green().build().write("Done!");
        }
        else Writer.builder().red().build().write("Error!");
    }

    private static boolean isReady() throws Exception {
        boolean gitIsReady = exec(Arrays.asList("git","version"));
        boolean ghIsReady = execWithOutput(Arrays.asList("gh","auth","status")).contains("Logged in");
        return gitIsReady && ghIsReady;
    }

    private static String readLine(String prompt) throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        AttributedString str = new AttributedString(prompt + "> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        log.info(prompt + "> ");
        String line = reader.readLine(str.toAnsi());
        log.info(prompt + "> " + line);
        terminal.flush();
        terminal.close();
        return line;
    }

    private static boolean exec(List<String> commands) throws Exception{
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        Writer.builder().yellow().build().write("EXEC====== " + commands);
        BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = bf.readLine()) != null){
            Writer.builder().build().write("========== " + line);
        }
        return p.waitFor() == 0;
    }

    private static String execWithOutput(List<String> commands) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        Process p = pb.start();
        Writer.builder().yellow().build().write("EXEC====== " + commands);
        BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = bf.readLine()) != null){
            output.append(line).append("\n");
            Writer.builder().build().write("========== " + line);
        }
        return output.toString();
    }

}