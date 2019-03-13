package esaas.devops.jenkins;

import java.io.IOException;
import java.util.Properties;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import esaas.devops.jenkins.common.Project;
import esaas.devops.jenkins.common.Util;
import esaas.devops.jenkins.pkg.PackageBuilder;
import esaas.devops.jenkins.pkg.PackageBuilderFactory;
import esaas.devops.jenkins.publish.Publisher;
import esaas.devops.jenkins.publish.PublisherFactory;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;

public class EsaasPublishBuilder extends Builder implements SimpleBuildStep {

    private String properties;

    @DataBoundConstructor
    public EsaasPublishBuilder() {}

    // 远程调试
    // set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8080,suspend=n
    // mvn hpi:run
    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        Util.setLogger(launcher.getListener().getLogger());
        Util.getLogger().println(this.properties);
        try {
            Properties properties = Util.loadPropertiesFromString(this.properties);
            // 构建project对象
            Project projectInner = Util.getProjectFromProperties(properties, (AbstractBuild<?, ?>) run, launcher);
            // 打包
            PackageBuilder builder = PackageBuilderFactory.getPackageBuilder(projectInner.getPackageType());
            projectInner.setTar(builder.build(projectInner));
            // 发布
            Publisher publisher = PublisherFactory.getPublisher(projectInner.getProtocolType());
            publisher.publish(projectInner);
        } catch (Exception e) {
            e.printStackTrace(Util.getLogger());
            throw e;
        } 
    }

    public String getProperties() { return properties; }

    @DataBoundSetter
    public void setProperties(String properties) { this.properties = properties; }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        // public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
        //         throws IOException, ServletException {
        //     return FormValidation.ok();
        // }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }
    }
}
