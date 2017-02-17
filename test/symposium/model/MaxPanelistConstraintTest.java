package symposium.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Test;

public class MaxPanelistConstraintTest {

	@Test
	public void testConstructor() throws Exception {
		ConstraintPriority priority = ConstraintPriority.REQUIRED;

		String name = "new";
		List<String> panelists = new ArrayList<>(3);
		panelists.add("Trey Lyons");
		panelists.add("Jurek Martin");
		panelists.add("Patrick Boel");
		List<TimeRange> avaliability = new ArrayList<>(5);
		avaliability.add(new TimeRange(360, 1080));
		avaliability.add(new TimeRange(1800, 2520));
		avaliability.add(new TimeRange(3240, 3960));
		avaliability.add(new TimeRange(4680, 5400));
		avaliability.add(new TimeRange(6120, 6840));
		TimeRangeSeries range = new TimeRangeSeries(avaliability);
		List<String> constraints = new ArrayList<>(6);
		constraints.add("New-Panelist:2");
		constraints.add("Paired-Panelists:3");
		constraints.add("Single-Category:2");
		constraints.add("Max-Panels(3):1");
		constraints.add("Min-Panels(1):1");
		constraints.add("Minimum-Capacity(2):2");
		List<String> category = new ArrayList<>(1);
		category.add("IA");

		Venue v = new Venue("North Building", 4, 2, Collections.EMPTY_LIST);

		ScheduleData.init(Arrays.asList(v), 1);

		Panel p = new Panel(name, panelists, range, category, constraints);

		Constraint vC = new MaxPanelistConstraint(priority, p, 3);
		assert (vC.PRIORITY == ConstraintPriority.REQUIRED);
	}

	@Test
	public void test_isConstraintViolated_scheduled_true() throws Exception {

		String name1 = "new1";
		List<String> panelists1 = new ArrayList<>(3);
		panelists1.add("Trey Lyons");
		panelists1.add("Jurek Martin");
		panelists1.add("Patrick Boel");
		List<TimeRange> avaliability1 = new ArrayList<>(5);
		avaliability1.add(new TimeRange(360, 1080));
		avaliability1.add(new TimeRange(1800, 2520));
		avaliability1.add(new TimeRange(3240, 3960));
		avaliability1.add(new TimeRange(4680, 5400));
		avaliability1.add(new TimeRange(6120, 6840));
		TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
		List<String> constraints1 = new ArrayList<>(6);
		constraints1.add("New-Panelist:2");
		constraints1.add("Paired-Panelists:3");
		constraints1.add("Single-Category:2");
		constraints1.add("Max-Panels(2):1");
		constraints1.add("Min-Panels(1):1");
		constraints1.add("Minimum-Capacity(2):2");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		String name2 = "new2";
		List<String> panelists2 = new ArrayList<>(3);
		panelists2.add("Trey Lyons");
		panelists2.add("Alice");
		panelists2.add("Bob");
		List<TimeRange> avaliability2 = new ArrayList<>(5);
		avaliability2.add(new TimeRange(360, 1080));
		avaliability2.add(new TimeRange(1800, 2520));
		avaliability2.add(new TimeRange(3240, 3960));
		avaliability2.add(new TimeRange(4680, 5400));
		avaliability2.add(new TimeRange(6120, 6840));
		TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
		List<String> constraints2 = new ArrayList<>(6);
		constraints2.add("New-Panelist:2");
		constraints2.add("Paired-Panelists:3");
		constraints2.add("Single-Category:2");
		constraints2.add("Max-Panels(2):1");
		constraints2.add("Min-Panels(1):1");
		constraints2.add("Minimum-Capacity(2):2");
		List<String> category2 = new ArrayList<>(1);
		category2.add("IA");

		String name3 = "new3";
		List<String> panelists3 = new ArrayList<>(3);
		panelists3.add("Trey Lyons");
		panelists3.add("Carol");
		panelists3.add("Dean");
		List<TimeRange> avaliability3 = new ArrayList<>(5);
		avaliability3.add(new TimeRange(360, 1080));
		avaliability3.add(new TimeRange(1800, 2520));
		avaliability3.add(new TimeRange(3240, 3960));
		avaliability3.add(new TimeRange(4680, 5400));
		avaliability3.add(new TimeRange(6120, 6840));
		TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
		List<String> constraints3 = new ArrayList<>(6);
		constraints3.add("New-Panelist:2");
		constraints3.add("Paired-Panelists:3");
		constraints3.add("Single-Category:2");
		constraints3.add("Max-Panels(2):1");
		constraints3.add("Min-Panels(1):1");
		constraints3.add("Minimum-Capacity(2):2");
		List<String> category3 = new ArrayList<>(1);
		category3.add("IA");

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(6, 7)));

		ScheduleData.init(Arrays.asList(v), 1);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
		Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
		Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

		ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		MaxPanelistConstraint mPC1 = null;
		MaxPanelistConstraint mPC2 = null;
		MaxPanelistConstraint mPC3 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC1 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC2 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(2).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC3 = (MaxPanelistConstraint) c;
				break;
			}
		}

		assertTrue(mPC1.isConstraintViolated());
		assertTrue(mPC2.isConstraintViolated());
		assertTrue(mPC3.isConstraintViolated());
	}

	@Test
	public void test_isConstraintViolated_scheduled_false() throws Exception {

		String name1 = "new1";
		List<String> panelists1 = new ArrayList<>(3);
		panelists1.add("Trey Lyons");
		panelists1.add("Jurek Martin");
		panelists1.add("Patrick Boel");
		List<TimeRange> avaliability1 = new ArrayList<>(5);
		avaliability1.add(new TimeRange(360, 1080));
		avaliability1.add(new TimeRange(1800, 2520));
		avaliability1.add(new TimeRange(3240, 3960));
		avaliability1.add(new TimeRange(4680, 5400));
		avaliability1.add(new TimeRange(6120, 6840));
		TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
		List<String> constraints1 = new ArrayList<>(6);
		constraints1.add("New-Panelist:2");
		constraints1.add("Paired-Panelists:3");
		constraints1.add("Single-Category:2");
		constraints1.add("Max-Panels(2):1");
		constraints1.add("Min-Panels(1):1");
		constraints1.add("Minimum-Capacity(2):2");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		String name2 = "new2";
		List<String> panelists2 = new ArrayList<>(3);
		panelists2.add("Trey Lyons");
		panelists2.add("Alice");
		panelists2.add("Bob");
		List<TimeRange> avaliability2 = new ArrayList<>(5);
		avaliability2.add(new TimeRange(360, 1080));
		avaliability2.add(new TimeRange(1800, 2520));
		avaliability2.add(new TimeRange(3240, 3960));
		avaliability2.add(new TimeRange(4680, 5400));
		avaliability2.add(new TimeRange(6120, 6840));
		TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
		List<String> constraints2 = new ArrayList<>(6);
		constraints2.add("New-Panelist:2");
		constraints2.add("Paired-Panelists:3");
		constraints2.add("Single-Category:2");
		constraints2.add("Max-Panels(2):1");
		constraints2.add("Min-Panels(1):1");
		constraints2.add("Minimum-Capacity(2):2");
		List<String> category2 = new ArrayList<>(1);
		category2.add("IA");

		String name3 = "new3";
		List<String> panelists3 = new ArrayList<>(3);
		panelists3.add("Trey Lyons");
		panelists3.add("Carol");
		panelists3.add("Dean");
		List<TimeRange> avaliability3 = new ArrayList<>(5);
		avaliability3.add(new TimeRange(360, 1080));
		avaliability3.add(new TimeRange(1800, 2520));
		avaliability3.add(new TimeRange(3240, 3960));
		avaliability3.add(new TimeRange(4680, 5400));
		avaliability3.add(new TimeRange(6120, 6840));
		TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
		List<String> constraints3 = new ArrayList<>(6);
		constraints3.add("New-Panelist:2");
		constraints3.add("Paired-Panelists:3");
		constraints3.add("Single-Category:2");
		constraints3.add("Max-Panels(2):1");
		constraints3.add("Min-Panels(1):1");
		constraints3.add("Minimum-Capacity(2):2");
		List<String> category3 = new ArrayList<>(1);
		category3.add("IA");

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(1440, 1500)));

		ScheduleData.init(Arrays.asList(v), 2);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
		Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
		Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

		ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		MaxPanelistConstraint mPC1 = null;
		MaxPanelistConstraint mPC2 = null;
		MaxPanelistConstraint mPC3 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC1 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC2 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(2).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC3 = (MaxPanelistConstraint) c;
				break;
			}
		}

		assertFalse(mPC1.isConstraintViolated());
		assertFalse(mPC2.isConstraintViolated());
		assertFalse(mPC3.isConstraintViolated());
	}

	@Test
	public void test_isConstraintViolated_predictive_true() {

		String name1 = "new1";
		List<String> panelists1 = new ArrayList<>(3);
		panelists1.add("Trey Lyons");
		panelists1.add("Jurek Martin");
		panelists1.add("Patrick Boel");
		List<TimeRange> avaliability1 = new ArrayList<>(5);
		avaliability1.add(new TimeRange(360, 1080));
		avaliability1.add(new TimeRange(1800, 2520));
		avaliability1.add(new TimeRange(3240, 3960));
		avaliability1.add(new TimeRange(4680, 5400));
		avaliability1.add(new TimeRange(6120, 6840));
		TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
		List<String> constraints1 = new ArrayList<>(6);
		constraints1.add("New-Panelist:2");
		constraints1.add("Paired-Panelists:3");
		constraints1.add("Single-Category:2");
		constraints1.add("Max-Panels(2):1");
		constraints1.add("Min-Panels(1):1");
		constraints1.add("Minimum-Capacity(2):2");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		String name2 = "new2";
		List<String> panelists2 = new ArrayList<>(3);
		panelists2.add("Trey Lyons");
		panelists2.add("Alice");
		panelists2.add("Bob");
		List<TimeRange> avaliability2 = new ArrayList<>(5);
		avaliability2.add(new TimeRange(360, 1080));
		avaliability2.add(new TimeRange(1800, 2520));
		avaliability2.add(new TimeRange(3240, 3960));
		avaliability2.add(new TimeRange(4680, 5400));
		avaliability2.add(new TimeRange(6120, 6840));
		TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
		List<String> constraints2 = new ArrayList<>(6);
		constraints2.add("New-Panelist:2");
		constraints2.add("Paired-Panelists:3");
		constraints2.add("Single-Category:2");
		constraints2.add("Max-Panels(2):1");
		constraints2.add("Min-Panels(1):1");
		constraints2.add("Minimum-Capacity(2):2");
		List<String> category2 = new ArrayList<>(1);
		category2.add("IA");

		String name3 = "new3";
		List<String> panelists3 = new ArrayList<>(3);
		panelists3.add("Trey Lyons");
		panelists3.add("Carol");
		panelists3.add("Dean");
		List<TimeRange> avaliability3 = new ArrayList<>(5);
		avaliability3.add(new TimeRange(360, 1080));
		avaliability3.add(new TimeRange(1800, 2520));
		avaliability3.add(new TimeRange(3240, 3960));
		avaliability3.add(new TimeRange(4680, 5400));
		avaliability3.add(new TimeRange(6120, 6840));
		TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
		List<String> constraints3 = new ArrayList<>(6);
		constraints3.add("New-Panelist:2");
		constraints3.add("Paired-Panelists:3");
		constraints3.add("Single-Category:2");
		constraints3.add("Max-Panels(2):1");
		constraints3.add("Min-Panels(1):1");
		constraints3.add("Minimum-Capacity(2):2");
		List<String> category3 = new ArrayList<>(1);
		category3.add("IA");

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(6, 7)));

		ScheduleData.init(Arrays.asList(v), 1);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
		Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
		Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

		ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		MaxPanelistConstraint mPC1 = null;
		MaxPanelistConstraint mPC2 = null;
		MaxPanelistConstraint mPC3 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC1 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC2 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getFreePanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC3 = (MaxPanelistConstraint) c;
				break;
			}
		}

		assertTrue(mPC3.isConstraintViolated(ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0)));
	}

	@Test
	public void test_isConstraintViolated_predictive_false() {

		String name1 = "new1";
		List<String> panelists1 = new ArrayList<>(3);
		panelists1.add("Trey Lyons");
		panelists1.add("Jurek Martin");
		panelists1.add("Patrick Boel");
		List<TimeRange> avaliability1 = new ArrayList<>(5);
		avaliability1.add(new TimeRange(360, 1080));
		avaliability1.add(new TimeRange(1800, 2520));
		avaliability1.add(new TimeRange(3240, 3960));
		avaliability1.add(new TimeRange(4680, 5400));
		avaliability1.add(new TimeRange(6120, 6840));
		TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
		List<String> constraints1 = new ArrayList<>(6);
		constraints1.add("New-Panelist:2");
		constraints1.add("Paired-Panelists:3");
		constraints1.add("Single-Category:2");
		constraints1.add("Max-Panels(2):1");
		constraints1.add("Min-Panels(1):1");
		constraints1.add("Minimum-Capacity(2):2");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		String name2 = "new2";
		List<String> panelists2 = new ArrayList<>(3);
		panelists2.add("Trey Lyons");
		panelists2.add("Alice");
		panelists2.add("Bob");
		List<TimeRange> avaliability2 = new ArrayList<>(5);
		avaliability2.add(new TimeRange(360, 1080));
		avaliability2.add(new TimeRange(1800, 2520));
		avaliability2.add(new TimeRange(3240, 3960));
		avaliability2.add(new TimeRange(4680, 5400));
		avaliability2.add(new TimeRange(6120, 6840));
		TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
		List<String> constraints2 = new ArrayList<>(6);
		constraints2.add("New-Panelist:2");
		constraints2.add("Paired-Panelists:3");
		constraints2.add("Single-Category:2");
		constraints2.add("Max-Panels(2):1");
		constraints2.add("Min-Panels(1):1");
		constraints2.add("Minimum-Capacity(2):2");
		List<String> category2 = new ArrayList<>(1);
		category2.add("IA");

		String name3 = "new3";
		List<String> panelists3 = new ArrayList<>(3);
		panelists3.add("Trey Lyons");
		panelists3.add("Carol");
		panelists3.add("Dean");
		List<TimeRange> avaliability3 = new ArrayList<>(5);
		avaliability3.add(new TimeRange(360, 1080));
		avaliability3.add(new TimeRange(1800, 2520));
		avaliability3.add(new TimeRange(3240, 3960));
		avaliability3.add(new TimeRange(4680, 5400));
		avaliability3.add(new TimeRange(6120, 6840));
		TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
		List<String> constraints3 = new ArrayList<>(6);
		constraints3.add("New-Panelist:2");
		constraints3.add("Paired-Panelists:3");
		constraints3.add("Single-Category:2");
		constraints3.add("Max-Panels(2):1");
		constraints3.add("Min-Panels(1):1");
		constraints3.add("Minimum-Capacity(2):2");
		List<String> category3 = new ArrayList<>(1);
		category3.add("IA");

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(1440, 1500)));

		ScheduleData.init(Arrays.asList(v), 2);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
		Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
		Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

		ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		MaxPanelistConstraint mPC1 = null;
		MaxPanelistConstraint mPC2 = null;
		MaxPanelistConstraint mPC3 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC1 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC2 = (MaxPanelistConstraint) c;
				break;
			}
		}

		for (Constraint c : ScheduleData.instance().getFreePanels().get(0).CONSTRAINTS) {
			if (c instanceof MaxPanelistConstraint) {
				mPC3 = (MaxPanelistConstraint) c;
				break;
			}
		}

		assertFalse(mPC1.isConstraintViolated());
		assertFalse(mPC2.isConstraintViolated());
		assertFalse(mPC3.isConstraintViolated(ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0)));
	}

	@After
	public void tearDown() {
		ScheduleData.deleteScheduleData();
	}
}
