package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;

/**
 * 处理日志目录
 */
public class LogsHandler implements DirHandler {

    public static final DirHandler INSTANCE = new LogsHandler();

    /**
     * 处理log目录，NO-OP
     * @param project
     */
    @Override public void handle(Project project) {
        Util.getLogger().println("处理logs目录完成");
    }

    private LogsHandler() {}
}
