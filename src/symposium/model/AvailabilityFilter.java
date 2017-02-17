package symposium.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AvailabilityFilter inherits from Filter, {@link symposium.model.Filter
 * Filter} for documentation. AvailabilityFilter checks the availability of the
 * panel and keep only the times accepted by the panel
 *
 */
public class AvailabilityFilter extends Filter {
	private static List<String> panelistsViolating;

	/**
	 * Constructs for the AvailabilityFilter class.
	 *
	 * @param priority
	 *          enum which determines if a filter is REQUIRED, VERY_IMPORTANT, or
	 *          DESIRED.
	 * @param panel
	 *          The Panel that the filter is part of.
	 */
	public AvailabilityFilter(ConstraintPriority priority, Panel panel) {
		super(priority, panel);
	}

	/**
	 * Filters out venueTimes that cannot be used by the panel
	 * 
	 * @param vtScoreMap
	 *          A map from venueTime to score
	 * @param requiredViolationMap
	 *          A map from only required Constraints to the number of violations
	 */
	@Override
	public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
		Set<VenueTime> keys = new HashSet<>(vtScoreMap.keySet());
		for (VenueTime vt : keys) {
			if (this.isConstraintViolated(vt) && this.PRIORITY == ConstraintPriority.REQUIRED) {
				// TODO : doesn't do anything if it's not required. This TODO is low priority since it's impossible to be not required
				vtScoreMap.remove(vt);
				if (!requiredViolationMap.containsKey(this)) {
					requiredViolationMap.put(this, 1);
				} else {
					requiredViolationMap.put(this, requiredViolationMap.get(this) + 1);
				}
			}
		}
	}

	/**
	 * Checks to see if panel can be placed in the given venueTime
	 * 
	 * @param venueTime
	 *          The time being checked
	 * @return true if panel fits, otherwise, return false
	 */
	@Override
	public boolean isConstraintViolated(VenueTime venueTime) {
		panelistsViolating = this.PANEL.PANELISTS;
		return !this.PANEL.AVAILABILITY.doesEnclose(venueTime.TIME);
	}

	/**
	 * @return String of the violation message and the PRIORITY
	 */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Availability Constraint violated: ").append("\n");
		sb.append("\t\t\t").append("The Panelists are: ").append(panelistsViolating);

		return sb.toString();
	}
}
