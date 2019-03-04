package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.FilePath;

import java.io.File;

/**
 * 处理聚合工程的War包
 */
public class WarHandler implements DirHandler {
    public static final DirHandler INSTANCE = new WarHandler();

    @Override
    public void handle (Project project) {
        // 拷贝war包到打包目录下
        FilePath warFile = Util.getWarFileForFront(project);
        if (warFile == null) {
            throw new IllegalStateException("war包不存在！");
        }
        FilePath targetWar = Util.getFilePathFromPathStr(project.getPackageRoot().getAbsolutePath() +
                File.separator + project.getProjectName() + ".war");
        try {
            warFile.copyTo(targetWar);
        } catch (Exception e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }

    private WarHandler() {}
}
