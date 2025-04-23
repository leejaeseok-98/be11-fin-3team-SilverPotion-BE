package silverpotion.postserver.post.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PostCategory {
    free("자유글"),
    notice("공지사항"),
    vote("투표");

    private final String label;

    PostCategory(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
