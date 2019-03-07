package esaas.devops.jenkins.pkg.handler;

import java.io.File;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.FilePath;

public class JarHandler implements DirHandler {

    public static final DirHandler INSTANCE = new JarHandler();
    @Override
    public void handle(Project project) {
        // 拷贝jar包到打包目录下
        FilePath jarFile = Util.getJarFileInSrcTargetPath(project);
        if (jarFile == null) {
            throw new IllegalStateException("jar包不存在！");
        }
        FilePath targetJar = Util.getFilePathFromPathStr(project.getPackageRoot().getAbsolutePath() +
                File.separator + project.getProjectName() + ".jar");
        try {
            jarFile.copyTo(targetJar);
        } catch (Exception e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }
    
    private JarHandler() {}
}