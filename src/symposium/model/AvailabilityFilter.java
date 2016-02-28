package symposium.model;

import java.util.*;

public class AvailabilityFilter extends Filter {
    private static List<String> panelistsViolating;

    public AvailabilityFilter(ConstraintPriority priority, Panel p) {
        super(priority, p);
    }

    /**
     * AvailabilityFilter checks the availability of the panel and keep only the times accepted by the panel.
     *
     * @param vtScoreMap A map from venueTime to score
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        Set<VenueTime> keys = new HashSet<>(vtScoreMap.keySet());
        for (VenueTime vt : keys) {
            if (this.isConstraintViolated(vt) && this.PRIORITY == ConstraintPriority.REQUIRED) {
                // TODO : doesn't do anything if it's not required. This TODO is low priority since it's impossible to be not required
                vtScoreMap.remove(vt);
                //
                if (!requiredViolationMap.containsKey(this)) {
                    requiredViolationMap.put(this, 1);
                } else {
                    requiredViolationMap.put(this, requiredViolationMap.get(this) + 1);
                }
            }
        }
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        panelistsViolating = this.PANEL.PANELISTS;
        return !this.PANEL.AVAILABILITY.doesEnclose(venueTime.TIME);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Availability Constraint violated: ").append("\n");
        sb.append("\t\t\t").append("The Panelists are: ").append(panelistsViolating);


        return sb.toString();
    }
}
