package silverpotion.userserver.healthData.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeartRateData {
    //    심장박동 수
    private double bpm;
    //    측정시간
    private String time;
}
