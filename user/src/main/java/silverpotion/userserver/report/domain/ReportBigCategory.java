package silverpotion.userserver.report.domain;

public enum ReportBigCategory {
    CAHT("채팅"),
    POST("게시물"),
    USER("유저"),
    GATHERING("모임");

    private final String description;

    ReportBigCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
