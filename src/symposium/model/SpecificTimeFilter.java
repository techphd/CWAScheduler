package symposium.model;
import java.util.*;

/**
 * The class SpecificTimeFilter inherits from Filter, @see Filter for documentation.
 * SpecificTimeFilter is a Filter removing all venueTime's not matching the time
 * given in the constructor.
 *
 */

public class SpecificTimeFilter extends Filter {
    public final int TIME;
    /**
    * Constructs for the SpecificTimeFilter class.
    *
    * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
    * @param p    The Panel that the constraint is part of.
    * @param t The wanted time
    */
    public SpecificTimeFilter(ConstraintPriority priority, Panel p, int t) {
        super(priority, p);
        TIME = t;
    }

    /**
     * Any time of venueTime not matching TIME will be removed.
     * @param vtScoreMap A map from venueTime to score
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        Set<VenueTime> keys = new HashSet<>(vtScoreMap.keySet());
        for (VenueTime vt : keys) {
            if(isConstraintViolated(vt)){
                if(!PANEL.LOCKED){
                    if(!requiredViolationMap.containsKey(this)){
                        requiredViolationMap.put(this, 1);
                    } else{
                        requiredViolationMap.put(this, requiredViolationMap.get(this)+1);
                    }
                }
                vtScoreMap.remove(vt);
            }
        }
    }

    /**
     * @param venueTime
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return (!TimeFormat.withinError(venueTime.TIME.getStart(), TIME, 1));
    }
    @Override
    public String toString() {
        return "SpecificTimeFilter (Panel must be scheduled at specific time)";
    }
}
