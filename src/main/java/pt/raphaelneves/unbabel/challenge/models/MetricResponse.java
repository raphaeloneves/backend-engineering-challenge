package pt.raphaelneves.unbabel.challenge.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricResponse implements Serializable {

    private String timestamp;
    private Double averageDeliveryTime;

    @Override
    public String toString() {
        return "{\"date\": " + timestamp + ", \"average_delivery_time\": " + averageDeliveryTime +"}";
    }
}
