package use_case.share.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CompositeReportSectionTest {

    @Test
    void hasContentFalseWhenNoChildrenHaveContent() {
        final CompositeReportSection composite = new CompositeReportSection("Report")
                .add(new FakeReportSection(false, "hidden"));

        assertFalse(composite.hasContent());
    }

    @Test
    void hasContentTrueWhenAnyChildHasContent() {
        final CompositeReportSection composite = new CompositeReportSection("Report")
                .add(new FakeReportSection(false, "hidden"))
                .add(new FakeReportSection(true, "visible"));

        assertTrue(composite.hasContent());
    }

    @Test
    void renderIncludesHeadingAndOnlyChildrenWithContent() {
        final CompositeReportSection composite = new CompositeReportSection("Report")
                .add(new FakeReportSection(false, "hidden"))
                .add(new FakeReportSection(true, "visible"));

        final String rendered = composite.render();

        assertTrue(rendered.contains("Report"));
        assertTrue(rendered.contains("visible"));
        assertFalse(rendered.contains("hidden"));
    }

    @Test
    void renderWithBlankHeadingOmitsHeadingLine() {
        final CompositeReportSection composite = new CompositeReportSection("")
                .add(new FakeReportSection(true, "visible"));

        assertEquals("visible", composite.render());
    }

    private static final class FakeReportSection implements ReportSection {
        private final boolean hasContent;
        private final String text;

        FakeReportSection(final boolean hasContent, final String text) {
            this.hasContent = hasContent;
            this.text = text;
        }

        @Override
        public String render() {
            return this.text;
        }

        @Override
        public boolean hasContent() {
            return this.hasContent;
        }
    }
}
