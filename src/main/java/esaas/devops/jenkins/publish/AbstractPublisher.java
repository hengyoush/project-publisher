package esaas.devops.jenkins.publish;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Version;
import esaas.devops.jenkins.publish.remote.Transporter;

/**
 * Publisher的公共父类，抽象了发布的逻辑，子类需要实现：
 * getTransporter方法，用于提供传输到远程的Transporter实现类。
 * getNextVersion方法，用于提供发布工程的版本。
 */
public abstract class AbstractPublisher implements Publisher {
    @Override
    public void publish(Project project) {
        Transporter transporter = getTransporter(project);
        Version nextVer = getNextVersion(project);
        project.setVersion(nextVer);
        uploadWith(transporter, project);
    }

    abstract Transporter getTransporter(Project project);
    abstract Version getNextVersion(Project project);
    abstract void uploadWith(Transporter transporter,Project project);
}
