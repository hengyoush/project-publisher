package esaas.devops.jenkins.pkg.handler;

import esaas.devops.jenkins.common.DirType;
import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import hudson.FilePath;

import java.io.File;

/**
 * 对于Rule中台工程，需要进行特殊处理，即将rules文件夹单独放入lib文件夹内
 */
public class RuleLibHandler extends LibHandler {

    @Override
    public void handle(Project project) {
        super.handle(project);
        // 需要把classes下的rule文件夹也复制到对应位置
        String rulePath = Util.getSrcClassPath(project) + File.separator + "rules";
        FilePath ruleFile = Util.getFilePathFromPathStr(rulePath);

        String targetRulePath = Util.getTargetDirStr(project, DirType.LIB) + File.separator + "rules";
        FilePath targetRuleFile = Util.getFilePathFromPathStr(targetRulePath);

        try {
            ruleFile.copyTo(targetRuleFile);
        } catch (Exception e) {
            e.printStackTrace(Util.getLogger());
            throw new RuntimeException(e);
        }
    }
}
