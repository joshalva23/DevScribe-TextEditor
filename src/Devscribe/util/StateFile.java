package Devscribe.util;

public class StateFile {
    private String absolutePath, content;
    private boolean updated;

    public StateFile(String route, String content) {
        absolutePath = route;
        this.content = content;
        this.updated = content == null;
    }

    public StateFile(String route) {
        this(route, null);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getContenido() {
        return content;
    }

    public void setContenido(String content) {
        this.content = content;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "FileState{" +
                "absolutepath='" + absolutePath + '\'' +
                ", content='" + content + '\'' +
                ", updated=" + updated +
                '}';
    }
}
