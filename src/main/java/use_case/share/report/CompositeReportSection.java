package use_case.share.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite node in the ReportSection tree. Aggregates any number of child ReportSections -
 * leaves or further composites - and renders them one after another, prefixed by an optional
 * heading.
 */
public class CompositeReportSection implements ReportSection {

    private final String heading;
    private final List<ReportSection> children = new ArrayList<>();

    /**
     * Constructs an empty composite section.
     *
     * @param heading heading printed above the rendered children, or blank/{@code null} for none
     */
    public CompositeReportSection(final String heading) {
        this.heading = heading;
    }

    /**
     * Adds a child section - a leaf or another composite - to this composite.
     *
     * @param child the section to add
     * @return this composite, so calls can be chained
     */
    public CompositeReportSection add(final ReportSection child) {
        this.children.add(child);
        return this;
    }

    @Override
    public boolean hasContent() {
        boolean found = false;
        for (final ReportSection child : this.children) {
            if (child.hasContent()) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public String render() {
        final StringBuilder builder = new StringBuilder();
        if (this.heading != null && !this.heading.isBlank()) {
            builder.append(this.heading).append("\n\n");
        }
        for (final ReportSection child : this.children) {
            if (child.hasContent()) {
                builder.append(child.render());
            }
        }
        return builder.toString();
    }
}
