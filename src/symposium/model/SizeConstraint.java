package symposium.model;

public class SizeConstraint extends Constraint {

    int minSize;

    /**
     *
     * @param mSize The minimum size necessary for the panel to fit.
     */
    public SizeConstraint(ConstraintPriority priority, Panel p, int mSize) {
        super(priority, p);
        minSize = mSize;
    }

    /**
     * Dependencies: (Additionally) venueTime.VENUE.SIZE variable
     * @param venueTime
     * @return true if the size is smaller than the minimum size required, false otherwise.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (venueTime.VENUE.SIZE >= minSize) {
            violated = false;
        }
        else {
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    public int getMinSize(){
        return this.minSize;
    }
}
