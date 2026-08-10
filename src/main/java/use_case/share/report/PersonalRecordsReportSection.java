package use_case.share.report;

/**
 * Leaf {@link ReportSection} presenting the user's all-time personal records.
 *
 * <p>Included in the report only when the user has enabled
 * {@code PrivacySetting.SHARE_PERSONAL_RECORDS}; see
 * {@code use_case.share.ShareProgressInteractor}.
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
