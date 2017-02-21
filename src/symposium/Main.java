package symposium;

import symposium.model.*;

import java.io.IOException;
import java.util.*;

import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        boolean evolutionFlag = false;
        String inputFilePath = "datas/data2017.json"; // default file
        for (String arg: args) {
            if (arg.equals("--evolution")) {
                evolutionFlag = true;
            } else {
                inputFilePath = arg;
            }
        }

        if (evolutionFlag) {
            evolutionary(inputFilePath);
        } else {
            standard(inputFilePath);
        }
    }

    public static void standard(String inputFilePath) {
        int[] diffValues = new int[5];
        diffValues[0] = 10; // SIZE_CONSTRAINT_VALUE
        diffValues[1] = 100000; // VENUE_CONSTRAINT_VALUE
        diffValues[2] = 100000; // TIME_CONSTRAINT_VALUE
        diffValues[3] = 10000000; // AVAILABILITY_CONSTRAINT_VALUE
        diffValues[4] = 100; // Panelist

        // Reading parsing json files
        try {
					Parser.parse(inputFilePath);
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        // Schedule data is initiated

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();
        //long elapsedTime = System.nanoTime() - initTime;

        // Print report
        System.out.println(Report.INSTANCE.toString());

        System.out.println(_DebugStatus.__debugStats());
    }

    public static void evolutionary(String inputFilePath){
        int[][] optimaladjust = new int[3][5];
        int[][] optimalscore = new int[3][2];
        int[] adjust = new int[5];

        int[][] provisionaladjustment = new int[10][5];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                optimaladjust[i][j] = 1;
            }
            for (int j=0; j < 2; j++) {
                optimalscore[i][j] = 1000000;
            }
        }

        for (int i = 0; i < 10; i++) {
            adjust[0] = optimaladjust[0][0];
            adjust[1] = optimaladjust[0][1];
            adjust[2] = optimaladjust[0][2];
            adjust[3] = optimaladjust[0][3];
            adjust[4] = optimaladjust[0][4];

            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < adjust.length; k++) {
                    provisionaladjustment[j][k] = (int) Math.round(Math.random());
                }

                try {
									Parser.parse(inputFilePath);
								} catch (IOException | ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                // Schedule data is initiated
                for (int m = 0; m < adjust.length; m++) {
                    adjust[m] = adjust[m] * (int) Math.pow(8, provisionaladjustment[j][m]);
                    if (adjust[m] == 0) {
                        adjust[m] = 1;
                    }
                }

                DummyScheduler bs = new DummyScheduler(adjust);
                bs.makeSchedule();


                int[] score = ScheduleData.instance().calculateScheduleOptimization();
                boolean check = false;
                for (int m = 0; m < 3; m++) {
                    if (m == 0) {
                        check = (score[0] < optimalscore[0][0] && score[1] < optimalscore[0][1]);
                    } else if (m == 1) {
                        check = (score[0] < optimalscore[1][0] || score[1] < optimalscore[1][1]);
                    } else if (m == 2) {
                        check = (score[0] < optimalscore[2][0]);
                    }

                    if (check) {
                        optimalscore[m][0] = score[0];
                        optimalscore[m][1] = score[1];
                        optimaladjust[m][0] = adjust[0];
                        optimaladjust[m][1] = adjust[1];
                        optimaladjust[m][2] = adjust[2];
                        optimaladjust[m][3] = adjust[3];
                        optimaladjust[m][4] = adjust[4];
                    }
                }
                ScheduleData.deleteScheduleData();
            }
        }
        System.out.println("\n-------------------THE RESULTS BY DECREASING BOTH VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscore[0][0]);
        System.out.println("Unscheduled = " + optimalscore[0][1]);
        System.out.println("Scale for Size = " + optimaladjust[0][0]);
        System.out.println("Scale for Venue = " + optimaladjust[0][1]);
        System.out.println("Scale for Time = " + optimaladjust[0][2]);
        System.out.println("Scale for Availability = " + optimaladjust[0][3]);
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING EITHER VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscore[1][0]);
        System.out.println("Unscheduled = " + optimalscore[1][1]);
        System.out.println("Scale for Size = " + optimaladjust[1][0]);
        System.out.println("Scale for Venue = " + optimaladjust[1][1]);
        System.out.println("Scale for Time = " + optimaladjust[1][2]);
        System.out.println("Scale for Availability  = " + optimaladjust[1][3]);
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING ONLY VIOLATIONS-------------------");
        System.out.println("Violations = " + optimalscore[2][0]);
        System.out.println("Unscheduled = " + optimalscore[2][1]);
        System.out.println("Scale for Size = " + optimaladjust[2][0]);
        System.out.println("Scale for Venue = " + optimaladjust[2][1]);
        System.out.println("Scale for Time = " + optimaladjust[2][2]);
        System.out.println("Scale for Availability = " + optimaladjust[2][3]);
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);
    }

    private static String __debugStats() {
        StringBuilder constraintCount = new StringBuilder();
        Map<String, Integer> violationMap = new HashMap();
        violationMap.put("New-Panelist Constraint", 0);
        violationMap.put("Paired-Panelist Constraint", 0);
        violationMap.put("Single-Category Constraint", 0);
        violationMap.put("Venue Constraint", 0);
        violationMap.put("Max-Panels Constraint", 0);
        violationMap.put("Min-Panels Constraint", 0);
        violationMap.put("Minimum-Capacity Constraint", 0);
        violationMap.put("Specific Time Constraint", 0);
        violationMap.put("Consecutive Panels Constraint", 0);

        constraintCount.append("\n------------------------------ Debug Stats ------------------------------ ");

        for (Panel panel : ScheduleData.instance().getAssignedPanels()) {
            for (Constraint constraint : panel.CONSTRAINTS) {
                if (constraint.isConstraintViolated(panel.getVenueTime())) {
                    if (constraint instanceof NewPanelistConstraint) {
                        violationMap.put("New-Panelist Constraint", violationMap.get("New-Panelist Constraint")+1);
                    }
                    if (constraint instanceof PairedPanelistConstraint) {
                        violationMap.put("Paired-Panelist Constraint", violationMap.get("Paired-Panelist Constraint")+1);
                    }
                    if (constraint instanceof CategoryConstraint) {
                        violationMap.put("Single-Category Constraint", violationMap.get("Single-Category Constraint")+1);
                    }
                    if (constraint instanceof VenueFilter) {
                        violationMap.put("Venue Constraint", violationMap.get("Venue Constraint")+1);
                    }
                    if (constraint instanceof MaxPanelistConstraint) {
                        violationMap.put("Max-Panels Constraint", violationMap.get("Max-Panels Constraint"+1));
                    }
                    if (constraint instanceof SizeConstraint) {
                        violationMap.put("Minimum-Capacity Constraint", violationMap.get("Minimum-Capacity Constraint")+1);
                    }
                    if (constraint instanceof SpecificTimeFilter) {
                        violationMap.put("Specific Time Constraint", violationMap.get("Specific Time Constraint")+1);
                    }
                    if (constraint instanceof ConsecutivePanelsConstraint) {
                        violationMap.put("Consecutive Panels Constraint", violationMap.get("Consecutive Panels Constraint")+1);
                    }

                }
            }
        }

        int scheduled = ScheduleData.instance().getAssignedPanels().size();
        int unscheduled = ScheduleData.instance().getUnschedulablePanels().size();
        int total = scheduled + unscheduled;
        float percent = 100*((float) scheduled/(float) total);
        constraintCount.append("\n\n").append(percent + "% of panels scheduled (" + scheduled + " out of " + total + ")");

        int totalViolations = 0;

        for (String key : violationMap.keySet()) {
            constraintCount.append("\n\n").append(key + " violated " + violationMap.get(key) + " times");
            totalViolations += violationMap.get(key);
        }

        constraintCount.append("\n\n").append(totalViolations + " Total Number of Violations ");


        int countPrefVenueViolations = 0;
        int countPrefVenue = 0;
        constraintCount.append("\n\n------------------------------ Free VenueTimes ------------------------------ \n");
        for(Venue v : ScheduleData.instance().VENUES) {
            if(v.PRIORITY < 10) {
                constraintCount.append("\n").append(v.NAME);
                for (VenueTime vt : v.getFreeVenueTimes()) {

                    constraintCount.append("\n\t").append(vt);
                    countPrefVenueViolations++;

                }
                for(VenueTime vt : v.getAssignedVenueTimes()) {
                    countPrefVenue++;
                }
            }

        }

            constraintCount.append("\n\n------------------------------ Venue Times Violating The duration Filter ------------------------------ \n");
            int countDuration = 0;
            for (Panel panel : ScheduleData.instance().getAssignedPanels()) {
                if((panel.PANELISTS.size() <= 2) && TimeFormat.withinError(panel.getVenueTime().TIME.length(),
                        80, 1)){
                //    constraintCount.append("\n").append(panel.NAME).append(" Violated Duration");
                    countDuration++;
                }
                else if((panel.PANELISTS.size() > 2) && TimeFormat.withinError(panel.getVenueTime().TIME.length(),
                        50, 1)){
                //    constraintCount.append("\n").append(panel.NAME).append(" Violated Duration");
                    countDuration++;
                }
            }

            constraintCount.append("\n\n").append("Number of Violations for Duration: ").append(countDuration);
            constraintCount.append("\n\n").append("Number of Violations for Preferred: ").append(countPrefVenueViolations);

            constraintCount.append("\n\n").append("Number of Scheulded for Preferred: ").append(countPrefVenue);

        return constraintCount.toString();
    }
}
