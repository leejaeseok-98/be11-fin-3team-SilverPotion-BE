package silverpotion.userserver.report.domain;

public enum ReportSmallCategory {
    SEXUAL_CONTENT("성적 행위"),
    HATE_SPEECH("혐오 발언"),
    FRAUD("사기"),
    VIOLENCE("폭력"),
    ILLEGAL_ACT("불법"),
    BULLYING("따돌림");

    private final String description;

    ReportSmallCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
