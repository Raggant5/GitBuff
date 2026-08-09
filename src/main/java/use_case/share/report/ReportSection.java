package use_case.share.report;

/**
 * Component in the Composite design pattern used to assemble a user's shareable progress
 * report.
 *
 * <p>A {@code ReportSection} may be a single, self-contained block of content - a leaf, such
 * as {@link ProfileReportSection} - or a group of other sections - a composite, see
 * {@link CompositeReportSection}. Because both leaves and composites implement this same
 * interface, {@code use_case.share.ShareProgressInteractor} can treat them uniformly: it adds
 * whichever sections the user's active {@code PrivacySetting}s call for and renders the whole
 * tree with a single {@link #render()} call, without knowing how many sections exist or how
 * they are grouped.
 *
 * <p>This keeps the report open for extension - a new section is added by writing one more
 * class that implements {@code ReportSection} - without modifying the interactor that builds
 * the report (Open/Closed Principle).
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

