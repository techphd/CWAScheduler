package symposium.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Test;

public class SpecificTimeFilterTest {

	@After
	public void tearDown() {
		ScheduleData.deleteScheduleData();
	}

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

		Constraint sTC = new SpecificTimeFilter(priority, p, 1440);
		assert (sTC.PRIORITY == ConstraintPriority.REQUIRED);
	}

	@Test
	public void test_isConstraintViolated_true() throws Exception {

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
		constraints1.add("Time(1;10:00):1");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(6, 7)));

		ScheduleData.init(Arrays.asList(v), 4);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

		ScheduleData.instance().initPanels(Arrays.asList(p1));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		SpecificTimeFilter sTC1 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof SpecificTimeFilter) {
				sTC1 = (SpecificTimeFilter) c;
				break;
			}
		}

		assertTrue(sTC1.isConstraintViolated());
	}

	@Test
	public void test_isConstraintViolated_false() throws Exception {

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
		constraints1.add("Time(0;10:00):1");
		List<String> category1 = new ArrayList<>(1);
		category1.add("IA");

		Venue v = new Venue("South Building", 4, 2, Arrays.asList(new TimeRange(600, 660), new TimeRange(1440, 1500)));

		ScheduleData.init(Arrays.asList(v), 2);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

		ScheduleData.instance().initPanels(Arrays.asList(p1));

		ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0),
				ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

		SpecificTimeFilter sTC1 = null;

		for (Constraint c : ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
			if (c instanceof SpecificTimeFilter) {
				sTC1 = (SpecificTimeFilter) c;
				break;
			}
		}

		assertFalse(sTC1.isConstraintViolated());
	}

	@Test
	public void test_isConstraintViolated_truePredictive() {

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

		Venue v = new Venue("South Building", 4, 2,
				Arrays.asList(new TimeRange(1, 3), new TimeRange(4, 5), new TimeRange(6, 7)));

		ScheduleData.init(Arrays.asList(v), 4);

		Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

		ScheduleData.instance().initPanels(Arrays.asList(p1));

		SpecificTimeFilter sTC = new SpecificTimeFilter(ConstraintPriority.REQUIRED, p1, 600);

		assertTrue(sTC.isConstraintViolated(ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0)));
	}
}
