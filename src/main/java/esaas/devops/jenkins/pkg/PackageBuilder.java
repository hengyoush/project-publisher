package esaas.devops.jenkins.pkg;

import esaas.devops.jenkins.common.Project;

import java.io.File;

/**
 * 根据不同的打包方式实现打包
 * 有app docker
 */
public interface PackageBuilder {
    File build(Project project);
}
