package silverpotion.userserver.report.domain;

public enum ReportStatus {
    WAIT("대기"),
    COMPLETE("완료");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
