package symposium.model;

import java.util.*;
//TODO: MinPanelsFilter is always assumed that the priority is VeryImportant

/**
 * MinPanelsFilter inherits from {@link symposium.model.Filter Filter}.
 * MinPanelsFilter determines which of the panelists is not utilized, as in not scheduled on given days
 * then order them to prioritize panels with those panelists
 *
 */
public class MinPanelsFilter extends Filter {
    public static int COST_OF_MIN_PANELIST_VIOLATION = 400; // Cost picked to utilize the violation, different values yield different results

    /**
     * Constructs for the MinPanelsFilter class.
     *
     * @param priority enum which determines if a filter is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the filter is part of.
     */

    public MinPanelsFilter(ConstraintPriority priority,Panel panel) {
        super(priority, panel);
    }

    /**
     * Checks who is not scheduled on the given day and how many times
     * @return Map<available_day,available> number of panelists not scheduled on a certain day
     */
    private Map<Integer, Integer> getMinPanelistDayGain() {
        //TODO: better implementation is possible in scheduleData
        Set<Integer> daysInAvailability = new HashSet<>();
        Iterator<TimeRange> pItr = this.PANEL.AVAILABILITY.iterator();
        while (pItr.hasNext()) {
            daysInAvailability.add(TimeFormat.getNumberOfDay(pItr.next().START));
        }
        Map<Integer, Integer> dayGainMap = new HashMap<>();
        for (String pnst : this.PANEL.PANELISTS) {
            for (int avDay : daysInAvailability) {
                if (ScheduleData.instance().getPanelistAppearanceNo(avDay, pnst) > 0) {
                    continue;
                }
                // If this line is reached, pnst is not scheduled in avDay
                if (dayGainMap.get(avDay) == null) {
                    dayGainMap.put(avDay, 1);
                } else {
                    dayGainMap.put(avDay, dayGainMap.get(avDay) + 1);
                }
            }
        }
        return dayGainMap;
    }

    /**
     * Never actually called in scheduling stage, only for report purposes
     * @param venueTime The time being checked
     * @return false
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false; // not violated because the panel is scheduled. and its panels are scheduled in this day.
    }

    /**
     *  for each VenueTime vt in the map vtScoreMap:
     *    increase vt's score by the number of panelist not scheduled yet in vt's day
     *
     * @param vtScoreMap A map from possible venueTime to score to be evaluated
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        Map<Integer, Integer> minDayToGainMap = getMinPanelistDayGain();
        //
        for (VenueTime vt : vtScoreMap.keySet()) {
            vtScoreMap.put(vt, vtScoreMap.get(vt) + COST_OF_MIN_PANELIST_VIOLATION * (minDayToGainMap.containsKey(vt.getDay()) ? minDayToGainMap.get(vt.getDay()) : 0));
        }
    }

    /**
     * Never actually used.
     * @return "String with the name of the class and what it does."
     */

    @Override
    public String toString() {
        return "Min Panels Filter: priority = " + PRIORITY;
    }

}
