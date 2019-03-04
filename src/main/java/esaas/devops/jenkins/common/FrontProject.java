package esaas.devops.jenkins.common;

public class FrontProject extends Project {
    @Override
    public ProjectType getProjectType() {
        return ProjectType.front;
    }
}
