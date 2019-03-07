package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.DirType;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.FilePath;

import java.io.File;
import java.io.IOException;

/**
 * 处理lib目录，需要把jenkins的workspace下的ProjectRoot目录下的lib中的jar包
 * copy到packageRoot下的lib目录,将application.properties和key.properties也拷贝到
 * 指定目录的lib下。
 */
public class DefaultLibHandler implements DirHandler {
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String KEY_PROPERTIES = "key.properties";

    @Override
    public void handle(Project project) {
        // 拷贝jar
        // 获取src下lib目录
        String libPath = Util.getLibClassPath(project);
        FilePath libFilePath = new FilePath(new File(libPath));
        try {
            FilePath[] srcLibs = libFilePath.list("*jar");
            for (FilePath srcLib : srcLibs) {
                String targetPathStr = Util.getTargetDirStr(project, DirType.LIB) +
                        File.separator +
                        srcLib.getName();
                FilePath target = new FilePath(new File(targetPathStr));
                srcLib.copyTo(target);
            }
            // 拷贝application.properties 和 key.properties
            copyProperties(project, APPLICATION_PROPERTIES);
            copyProperties(project, KEY_PROPERTIES);
        } catch (Exception e) {
            Util.getLogger().println("LibHandler执行失败，"  + e.getMessage());
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }

    }

    private void copyProperties(Project project, String propertiesFileName) throws IOException, InterruptedException {
        String appPropSrc = Util.getSrcClassPath(project) + File.separator +
                propertiesFileName;
        String appPropTarget = Util.getTargetDirStr(project, DirType.LIB) +
                File.separator +
                propertiesFileName;
        Util.getFilePathFromPathStr(appPropSrc)
        .copyTo
        (Util.getFilePathFromPathStr(appPropTarget));
    }

    public static final DirHandler INSTANCE = new DefaultLibHandler();
}
