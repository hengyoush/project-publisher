package esaas.devops.jenkins.pkg.shell;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Shell;
import esaas.devops.jenkins.common.ShellType;

/**
 * 脚本shell生成器
 */
public interface ShellGenerator {

    Shell generate(Project project, ShellType shellType);
}
