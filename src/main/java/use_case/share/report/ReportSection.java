package use_case.share.report;

/**
 * Component in the Composite design pattern used to assemble a user's shareable progress
 * report. A ReportSection may be a single leaf (ProfileReportSection) or a composite group of
 * other sections (CompositeReportSection).
 */
public interface ReportSection {

    /**
     * Renders this section, and, for composite sections, all of its children, as plain text.
     *
     * @return the rendered text, or an empty string if this section has no content to share
     */
    String render();

    /**
     * Reports whether this section has any content worth sharing.
     *
     * @return true if calling {@link #render()} would produce non-empty text
     */
    boolean hasContent();
}
