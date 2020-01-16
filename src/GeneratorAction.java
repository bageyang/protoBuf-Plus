import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 组别在 generator 内
 *
 * @author NING MEI
 */
public class GeneratorAction extends AnAction {
    private static final String PROTO_BUF_EXTENSION_NAME = "proto";
    private static final List<String> BASE_COMMANDS = new ArrayList<>(2);
    private static final String STAT_CMD = " -I=";
    private static final String CENTER_CMD = " --java_out=";

    static {
        BASE_COMMANDS.add("cmd.exe");
        BASE_COMMANDS.add("/c");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        String extension = e.getData(PlatformDataKeys.FILE_EDITOR).getFile().getExtension();
        if (!PROTO_BUF_EXTENSION_NAME.equals(extension)) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        runMakeClass(e);
    }

    private void runMakeClass(AnActionEvent e) {
        List<String> commands = analysisCommand(e);
        runCommand(commands);
    }

    private List<String> analysisCommand(AnActionEvent e) {
        VirtualFileImpl file = (VirtualFileImpl) e.getData(PlatformDataKeys.FILE_EDITOR).getFile();
        String fileName = file.getName();
        String fileDirectory = file.getParent().getPath();
        String outputPath = fileDirectory;


        String basePath = e.getProject().getBasePath() + "/src/main/java";

        ProtoBufConfig config = ProtoBufConfig.getInstance();
        String compilerPath = config.getProtoPath();
        String defaultFolderPlace = config.getDefaultFolderPlace();

        String lastCommand;
        if (declaredPackage(file)) {
            outputPath = basePath;
        } else if (StringUtils.isNotBlank(defaultFolderPlace)) {
            outputPath = outputPath + "/" + defaultFolderPlace;
            File outPath = new File(outputPath);
            if (!outPath.exists()) {
                outPath.mkdirs();
            }
        }

        //              CompilerPath + "-I=" + fileDirectory +" --java_out="+outputPath + " "+ filePath
        lastCommand = compilerPath + STAT_CMD + fileDirectory + CENTER_CMD + outputPath + " " + fileDirectory + "/" + fileName;
        List<String> commands = new ArrayList(BASE_COMMANDS);
        commands.add(lastCommand);
        return commands;
    }

    private boolean declaredPackage(VirtualFileImpl e) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(e.getInputStream()));
            String str;
            while ((str = bf.readLine()) != null) {
                if (str.trim().startsWith("package")) {
                    return true;
                } else if (str.trim().startsWith("message")) {
                    return false;
                }
            }
            bf.close();
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    private void runCommand(List<String> commands) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
