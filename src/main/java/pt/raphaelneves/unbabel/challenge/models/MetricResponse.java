/**
 * This class represents the return that must be shown after the translation average duration calculation
 * It uses the Project Lombok to avoid boilerplate code, such as Setters and Getters, and also provide an
 * out of the box Builder class.
 * @author Raphael Neves
 **/

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

    /**
     * The flat timestamp with format yyyy-MM-dd HH:mm:00 used as index and aggregator to expose
     * translation events that have been occurred at the same time interval.
     */
    private String timestamp;
    /**
     * The average duration from a set of translations in a specific interval
     */
    private Double averageDeliveryTime;

    /**
     * Define the specific notation for the MetricResponse model when serialized as flat String
     * @return A flat JSON notation from MetricResponse
     */
    @Override
    public String toString() {
        return "{\"date\": " + timestamp + ", \"average_delivery_time\": " + averageDeliveryTime +"}";
    }
}
