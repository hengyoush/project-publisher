package esaas.devops.jenkins.publish;

import esaas.devops.jenkins.common.Project;

/**
 * 实现工程的发布功能，根据不同的传输方式分类
 */
public interface Publisher {

    void publish(Project project);
}
