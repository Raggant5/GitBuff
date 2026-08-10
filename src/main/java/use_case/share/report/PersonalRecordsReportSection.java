package use_case.share.report;

/**
 * Leaf ReportSection presenting the user's all-time personal records.
 */
public class PersonalRecordsReportSection implements ReportSection {

    private final double totalMinutesWorkedOut;

    /**
     * Constructs a personal-records section.
     *
     * @param totalMinutesWorkedOut the user's all-time total minutes worked out
     */
    public PersonalRecordsReportSection(final double totalMinutesWorkedOut) {
        this.totalMinutesWorkedOut = totalMinutesWorkedOut;
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public String render() {
        final StringBuilder builder = new StringBuilder();
        builder.append("--- PERSONAL RECORDS (PRs) ---\n");
        builder.append("Total Time Worked Out (All-Time): ")
                .append(Math.round(this.totalMinutesWorkedOut)).append(" minutes\n\n");
        return builder.toString();
    }
}
