package symposium.model;

import org.junit.After;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PanelistConstraintTest {

    @Test
    public void testConstructor() {
        ConstraintPriority priority = ConstraintPriority.VERY_IMPORTANT;

        Venue v1 = new Venue("North Building", 4, 2,Collections.EMPTY_LIST);
        List<Venue> venues = Arrays.asList(v1);

        ScheduleData.init(venues, 4);


        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        PanelistConstraint pc = new PanelistConstraint(priority, p1);

        assert(pc.PRIORITY == ConstraintPriority.VERY_IMPORTANT);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
    }

    @Test
    public void test_isConstraintViolated_scheduled_true() {

        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2, vt1);

        Venue v2 = new Venue("South Building", 4, 2, vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Panelist-Constraint:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Panelist-Constraint:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PanelistConstraint pc1 = null;
        PanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc1 = (PanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc2 = (PanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PanelistConstraint never initialized");
        }

        assertTrue(pc1.isConstraintViolated());
        assertTrue(pc2.isConstraintViolated());
    }

    @Test
    public void test_isConstraintViolated_scheduled_false1() {

        List<TimeRange> vt1 = Arrays.asList(new TimeRange(600, 700));

        Venue v1 = new Venue("North Building", 4, 2, vt1);

        List<TimeRange> vt2 = Arrays.asList(new TimeRange(800, 900));

        Venue v2 = new Venue("South Building", 4, 2, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Panelist-Constraint:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Panelist-Constraint:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PanelistConstraint pc1 = null;
        PanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc1 = (PanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc2 = (PanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PanelistConstraint never initialized");
        }

        assertFalse(pc1.isConstraintViolated());
        assertFalse(pc2.isConstraintViolated());
    }

    @Test
    public void test_isConstraintViolated_scheduled_false2() {

        List<TimeRange> vt1 = Arrays.asList(new TimeRange(600, 700));

        Venue v1 = new Venue("North Building", 4, 2, vt1);

        List<TimeRange> vt2 = Arrays.asList(new TimeRange(600, 700));

        Venue v2 = new Venue("South Building", 4, 2, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Panelist-Constraint:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Alice");
        panelists2.add("Bob");
        panelists2.add("Clark");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Panelist-Constraint:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PanelistConstraint pc1 = null;
        PanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc1 = (PanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc2 = (PanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PanelistConstraint never initialized");
        }

        assertFalse(pc1.isConstraintViolated());
        assertFalse(pc2.isConstraintViolated());
    }

    @Test
    public void test_isConstraintViolated_predictive_true() {

        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2, vt1);

        Venue v2 = new Venue("South Building", 4, 2, vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Panelist-Constraint:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Panelist-Constraint:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        PanelistConstraint pc1 = null;
        PanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc1 = (PanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getFreePanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc2 = (PanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PanelistConstraint never initialized");
        }

        assertTrue(pc2.isConstraintViolated(ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0)));
    }

    @Test
    public void test_isConstraintViolated_predictive_false(){

        List<TimeRange> vt1 = Arrays.asList(new TimeRange(600, 700));

        Venue v1 = new Venue("North Building", 4, 2, vt1);

        List<TimeRange> vt2 = Arrays.asList(new TimeRange(600, 700));

        Venue v2 = new Venue("South Building", 4, 2, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new";
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
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Panelist-Constraint:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Alice");
        panelists2.add("Bob");
        panelists2.add("Clark");
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
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Panelist-Constraint:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        PanelistConstraint pc1 = null;
        PanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc1 = (PanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getFreePanels().get(0).CONSTRAINTS) {
            if (c instanceof PanelistConstraint) {
                pc2 = (PanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PanelistConstraint never initialized");
        }

        assertFalse(pc1.isConstraintViolated());
        assertFalse(pc2.isConstraintViolated(ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0)));
    }

    @After
    public void tearDown() {
        ScheduleData.deleteScheduleData();
    }

}