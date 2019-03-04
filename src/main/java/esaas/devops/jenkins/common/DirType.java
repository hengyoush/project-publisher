package esaas.devops.jenkins.common;

import esaas.devops.jenkins.pkg.handler.*;

public enum DirType {
    BIN("bin") {
        @Override
        public DirHandler getHandler() {
            return BinHandler.INSTANCE;
        }

        @Override
        public DirHandler getHandler(Project project) {
            if (project.getProjectKey().contains("rule.middle")) {
                return new RuleLibHandler();
            }
            return getHandler();
        }
    },
    CONF("conf") {
        @Override
        public DirHandler getHandler() {
            return ConfHandler.INSTANCE;
        }
    },
    LIB("lib") {
        @Override
        public DirHandler getHandler () {
            return LibHandler.INSTANCE;
        }
    },
    LOGS("logs") {
        @Override
        public DirHandler getHandler () {
            return LogsHandler.INSTANCE;
        }
    },
    WAR("war") {
        @Override
        public DirHandler getHandler () {
            return WarHandler.INSTANCE;
        }

        @Override
        public boolean needPreCreate () {
            return false;
        }
    };

    private final String name;
    DirType(String name) {
        this.name = name;
    }

    public DirHandler getHandler(Project project) { return getHandler(); }

    public boolean needPreCreate() { return true; }

    public abstract DirHandler getHandler();

    @Override
    public String toString() {
        return this.name;
    }
}
