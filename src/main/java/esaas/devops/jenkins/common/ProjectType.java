package esaas.devops.jenkins.common;

public enum ProjectType {
    middle {
        public DirType[] dirs() {
            return new DirType[] {DirType.BIN, DirType.LIB, DirType.CONF, DirType.LOGS};
        }
    },
    front {
        public DirType[] dirs() {
            return new DirType[] {DirType.WAR};
        }
    },
    sprboot {
        public DirType[] dirs() {
            return new DirType[] {DirType.BIN, DirType.LIB, DirType.CONF, DirType.LOGS};
        }
    };

    public abstract DirType[] dirs();
}
