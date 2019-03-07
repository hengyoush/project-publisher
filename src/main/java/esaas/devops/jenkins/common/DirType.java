package esaas.devops.jenkins.common;

import esaas.devops.jenkins.pkg.handler.*;

public enum DirType {
    BIN("bin") {
        public DirHandler getHandler() { return BinHandler.INSTANCE; }
    },
    CONF("conf") {
        public DirHandler getHandler() { return ConfHandler.INSTANCE; }
    },
    LIB("lib") {
        public DirHandler getHandler () { return DefaultLibHandler.INSTANCE; }
        public DirHandler getHandler(Project project) {
            return project.getProjectKey().contains("rule.middle") ? 
                    new RuleLibHandler() : getHandler();
        }
    },
    LOGS("logs") {
        public DirHandler getHandler () { return LogsHandler.INSTANCE; }
    },
    WAR("war") {
        public DirHandler getHandler () {  return WarHandler.INSTANCE; }
        public boolean needPreCreate () { return false; }
    },
    JAR("jar") {
        public DirHandler getHandler() { return JarHandler.INSTANCE; }
        public boolean needPreCreate() { return false; }
    };

    private final String name;
    DirType(String name) { this.name = name; }

    public DirHandler getHandler(Project project) { return getHandler(); }

    public boolean needPreCreate() { return true; }

    public abstract DirHandler getHandler();

    @Override
    public String toString() { return this.name; }
}
