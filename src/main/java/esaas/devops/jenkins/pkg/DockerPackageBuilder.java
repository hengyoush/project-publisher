package esaas.devops.jenkins.pkg;

import java.io.File;
import java.io.IOException;

import com.kenai.jffi.Platform.OS;

import esaas.devops.jenkins.common.DockerProject;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.Launcher;
import hudson.model.TaskListener;

/**
 * 使用maven插件--Jib构建镜像tar, tar包文件名做处理等
 */
public class DockerPackageBuilder implements PackageBuilder {

    private static final String POM_XML = "pom.xml";
    private static final String CMD_C = "cmd /c ";
    private static final String JIB_CMD_TEMPLATE = "mvn compile -f ${pom} jib:buildTar -pl ${project} -P docker";
    private static final String POM_PLACE_HOLDER = "${pom}";
    private static final String PROJECT_PLACE_HOLDER = "${project}";
    private static final String JIB_IMAGE_TAR = "jib-image.tar";

    @Override
    public File build(Project project) {
        // 执行命令
        // 1. 进入到要构建的路径
        DockerProject dockerProject = (DockerProject) project;
        String projectRoot = dockerProject.getProjectRoot().getAbsolutePath();
        // 2. 执行mvn命令
        String mvnCommand = JIB_CMD_TEMPLATE.replace(POM_PLACE_HOLDER, projectRoot + File.separator + POM_XML)
                .replace(PROJECT_PLACE_HOLDER, dockerProject.getTargetProject());
        if (System.getProperty("os.name").toUpperCase().contains(OS.WINDOWS.name())) {
            mvnCommand = CMD_C + mvnCommand;
        }
        Launcher launcher = dockerProject.getLauncher();
        TaskListener listener = launcher.getListener();

        try {
            launcher.launch().stdout(listener).cmdAsSingleString(mvnCommand).join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }

        // 3.把tar包重命名好就行
        File tar = new File(Util.getSrcTargetPath(project) + File.separator + JIB_IMAGE_TAR);
        if (!tar.exists()) {
            throw new IllegalStateException("docker构建执行异常！");
        }
        File renameTo = new File(Util.getSrcTargetPath(project) + File.separator + 
                project.getProjectKey().replace(",", "-") + "-image.tar");
        tar.renameTo(renameTo);
        return renameTo;
    }
}