package silverpotion.postserver.common.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseTimeEntity {
  @CreationTimestamp
    private LocalDateTime createdTime;
  @UpdateTimestamp
    private LocalDateTime updatedTime;

}
