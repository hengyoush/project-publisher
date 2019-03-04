package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.Project;

/**
 * 不做任何操作的目录处理类
 */
public class NoopHandler implements DirHandler {

    @Override
    public void handle(Project project) {
        // NO-OP
    }
}
