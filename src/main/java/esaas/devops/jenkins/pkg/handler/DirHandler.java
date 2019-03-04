package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.Project;

/**
 * 打包时处理不同类型的路径，根据需要做不同的处理
 */
public interface DirHandler {

    void handle(Project project);
}
