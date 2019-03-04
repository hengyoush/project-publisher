package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.DirType;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.FilePath;

import java.io.File;

/**
 * 处理conf目录
 * 需要把log4j2.xml拷贝到conf下
 */
public class ConfHandler implements DirHandler {

    public static final String LOG4J_NAME = "log4j2.xml";
    @Override
    public void handle(Project project) {
        String srcClassPath = Util.getTrueProjectRoot(project).getAbsolutePath() + File.separator +
                "target" + File.separator + "classes" ;
        String targetConfPath = Util.getTargetDirStr(project, DirType.CONF);

        FilePath srclog4jFile = Util.getFilePathFromPathStr(srcClassPath + File.separator +
                LOG4J_NAME);
        FilePath targetlog4jFile = Util.getFilePathFromPathStr(targetConfPath + File.separator +
                LOG4J_NAME);
        try {
            srclog4jFile.copyTo(targetlog4jFile);
        } catch (Exception e) {
            e.printStackTrace(Util.getLogger());
        }

    }

    public static final DirHandler INSTANCE = new ConfHandler();
}
