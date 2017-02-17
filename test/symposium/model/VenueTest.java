package symposium.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VenueTest {

	/**
	 * Calls the constructor. Test fails if constructed Venue does not return
	 * appropriate values.
	 */
	@Test
	public void testConstructor() throws Exception {
		String name = "vnuName";
		int size = 3;
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));
		ranges.add(new TimeRange(70, 80));

		Venue venue = new Venue(name, size, 2, ranges);

		assertTrue(venue.NAME.equals(name));
		assertTrue(venue.SIZE == size);
		assertTrue(venue.getAssignedVenueTimes().isEmpty());

		List<VenueTime> venueTimes = venue.getFreeVenueTimes();
		for (int i = 0; i < ranges.size(); i++) {
			assertTrue(venueTimes.get(i).VENUE == venue);
			assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
		}
	}

	/**
	 * Tests whether the changeToAssigned() method works. Test succeeds if Venue
	 * is removed from free list, and added to assigned list.
	 */
	@Test
	public void testChangeToAssigned_Normal() throws Exception {
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));
		ranges.add(new TimeRange(70, 80));

		Venue venue = new Venue("vnuName", 3, 2, ranges);

		venue.changeToAssigned(venue.getFreeVenueTimes().get(0));

		// Should be in assigned venueTimes
		VenueTime vt = venue.getAssignedVenueTimes().get(0);
		assertTrue(vt.VENUE == venue);
		assertTrue(vt.TIME == ranges.get(0));

		// Should be removed from free venueTimes
		assertFalse(venue.getFreeVenueTimes().contains(vt));
	}

	/**
	 * This tests a case where changeToAssigned() shouldn't have any effect. The
	 * test fails if changeToAssigned() actually changed something.
	 */
	@Test
	public void testChangeToAssigned_VenueTimeDifferentVenue() throws Exception {
		List<TimeRange> ranges_1 = new ArrayList<>(2);
		ranges_1.add(new TimeRange(10, 20));
		ranges_1.add(new TimeRange(30, 40));
		Venue venue_1 = new Venue("vnuName", 3, 2, ranges_1);

		List<TimeRange> ranges_2 = new ArrayList<>(2);
		ranges_2.add(new TimeRange(50, 60));
		ranges_2.add(new TimeRange(70, 80));
		Venue venue_2 = new Venue("diffVenue", 2, 2, ranges_2);

		VenueTime fromVenue_1 = venue_1.getFreeVenueTimes().get(0);

		// This command should not change anything
		venue_2.changeToAssigned(fromVenue_1);

		assertFalse(venue_2.getAssignedVenueTimes().contains(fromVenue_1));
	}

	/**
	 * This test assigns, then frees a VenueTime. The test succeeds if the
	 * VenueTime is removed from the assigned list, and added to the free list.
	 */
	@Test
	public void testChangeToFree_Normal() throws Exception {
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));

		Venue venue = new Venue("vnuName", 3, 2, ranges);

		VenueTime testVT = venue.getFreeVenueTimes().get(0);

		venue.changeToAssigned(testVT);

		venue.changeToFree(testVT);

		assertFalse(venue.getAssignedVenueTimes().contains(testVT));
		assertTrue(venue.getFreeVenueTimes().contains(testVT));
	}

	/**
	 * This creates two venues, assigns a VenueTime to the first, then tries to
	 * free that venue time in the second. This test fails if freeing worked.
	 */
	@Test
	public void testChangeToFree_VenueTimeDifferentVenue() throws Exception {
		List<TimeRange> ranges_1 = new ArrayList<>(2);
		ranges_1.add(new TimeRange(10, 20));
		ranges_1.add(new TimeRange(30, 40));
		Venue venue_1 = new Venue("vnuName", 3, 2, ranges_1);

		List<TimeRange> ranges_2 = new ArrayList<>(2);
		ranges_2.add(new TimeRange(50, 60));
		ranges_2.add(new TimeRange(70, 80));
		Venue venue_2 = new Venue("diffVenue", 2, 2, ranges_2);

		VenueTime fromVenue_1 = venue_1.getFreeVenueTimes().get(0);

		// This command should not change anything
		venue_2.changeToFree(fromVenue_1);

		assertFalse(venue_2.getFreeVenueTimes().contains(fromVenue_1));
	}

	/**
	 * This test assigns two VenueTimes to a Venue, then checks if they are
	 * actually assigned. It fails if they are not assigned.
	 */
	@Test
	public void testGetAssignedVenueTimes() throws Exception {
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));
		ranges.add(new TimeRange(70, 80));

		Venue venue = new Venue("vnuName", 3, 2, ranges);

		// Change first 2 venueTimes to assigned
		venue.changeToAssigned(venue.getFreeVenueTimes().get(0));
		venue.changeToAssigned(venue.getFreeVenueTimes().get(0));

		List<VenueTime> venueTimes = venue.getAssignedVenueTimes();

		// Check if these two are returned
		for (int i = 0; i < 2; i++) {
			assertTrue(venueTimes.get(i).VENUE == venue);
			assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
		}
	}

	/**
	 * This test checks whether getFreeVenueTimes() works. It fails if an
	 * incorrect list of VenueTimes is returned.
	 */
	@Test
	public void testGetFreeVenueTimes() throws Exception {
		List<TimeRange> ranges = new ArrayList<>(4);
		ranges.add(new TimeRange(10, 20));
		ranges.add(new TimeRange(30, 40));
		ranges.add(new TimeRange(50, 60));
		ranges.add(new TimeRange(70, 80));

		Venue venue = new Venue("vnuName", 3, 2, ranges);

		List<VenueTime> venueTimes = venue.getFreeVenueTimes();
		for (int i = 0; i < ranges.size(); i++) {
			assertTrue(venueTimes.get(i).VENUE == venue);
			assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
		}
	}
}