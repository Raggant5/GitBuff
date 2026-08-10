package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.services.calendar.Calendar;
import entity.CalendarEvent;

class GoogleCalendarDataAccessObjectTest {

    private static final String EXISTING_CALENDAR = """
            {"items":[{"id":"calendar-amir","description":"GitBuff user: amir"}]}
            """;

    @Test
    void addEventUsesExistingCalendarAndConfiguresEmailReminder() {
        final QueueTransport transport = new QueueTransport();
        transport.enqueueJson(EXISTING_CALENDAR);
        transport.enqueueJson("{\"id\":\"event-created\"}");
        final GoogleCalendarDataAccessObject dataAccess =
                new GoogleCalendarDataAccessObject(calendar(transport));

        dataAccess.addCalendarEvent(
                "amir", "Workout", "Leg day", LocalDate.of(2026, 8, 9));

        assertEquals(List.of("GET", "POST"), transport.methods);
        assertTrue(transport.urls.get(1).contains("calendars/calendar-amir/events"));
        final String requestBody = transport.requestBodies.get(1);
        assertTrue(requestBody.contains("gitbuff686@gmail.com"));
        assertTrue(requestBody.contains("\"minutes\":1440"));
        assertTrue(requestBody.contains("\"date\":\"2026-08-09\""));
        assertTrue(requestBody.contains("\"date\":\"2026-08-10\""));
    }

    @Test
    void addEventCreatesSecondaryCalendarWhenOneDoesNotExist() {
        final QueueTransport transport = new QueueTransport();
        transport.enqueueJson("{\"items\":[]}");
        transport.enqueueJson("{\"id\":\"new-calendar\"}");
        transport.enqueueJson("{\"id\":\"new-event\"}");
        final GoogleCalendarDataAccessObject dataAccess =
                new GoogleCalendarDataAccessObject(calendar(transport));

        dataAccess.addCalendarEvent(
                "new-user", "Meal", "Lunch", LocalDate.of(2026, 8, 10));

        assertEquals(List.of("GET", "POST", "POST"), transport.methods);
        assertTrue(transport.requestBodies.get(1).contains("GitBuff - new-user"));
        assertTrue(transport.requestBodies.get(1).contains("GitBuff user: new-user"));
        assertTrue(transport.urls.get(2).contains("calendars/new-calendar/events"));
    }

    @Test
    void removeEventDeletesFromUsersSecondaryCalendar() {
        final QueueTransport transport = new QueueTransport();
        transport.enqueueJson(EXISTING_CALENDAR);
        transport.enqueueNoContent();
        final GoogleCalendarDataAccessObject dataAccess =
                new GoogleCalendarDataAccessObject(calendar(transport));

        dataAccess.removeCalendarEvent("amir", "event-4");

        assertEquals("DELETE", transport.methods.get(1));
        assertTrue(transport.urls.get(1).contains(
                "calendars/calendar-amir/events/event-4"));
    }

    @Test
    void getUserEventsLoadsPagesAndSupportsAllDayAndTimedEvents() {
        final QueueTransport transport = new QueueTransport();
        transport.enqueueJson(EXISTING_CALENDAR);
        transport.enqueueJson("""
                {
                  "nextPageToken":"next-page",
                  "items":[
                    {"id":"all-day","status":"confirmed","summary":"Meal",
                     "description":"Lunch","start":{"date":"2026-08-09"}},
                    {"id":"timed","status":"confirmed","summary":"Workout",
                     "description":"Run","start":{"dateTime":"2026-08-10T01:30:00-04:00"}},
                    {"id":"cancelled","status":"cancelled","summary":"Old",
                     "start":{"date":"2026-08-11"}},
                    {"id":"no-start","status":"confirmed","summary":"Incomplete"}
                  ]
                }
                """);
        transport.enqueueJson("""
                {"items":[
                  {"id":"page-two","status":"confirmed","summary":"Dinner",
                   "description":"Meal","start":{"date":"2026-08-12"}}
                ]}
                """);
        final Calendar service = calendar(transport);
        final AtomicInteger supplierCalls = new AtomicInteger();
        final GoogleCalendarDataAccessObject dataAccess =
                new GoogleCalendarDataAccessObject(() -> {
                    supplierCalls.incrementAndGet();
                    return service;
                });

        final List<CalendarEvent> events = dataAccess.getUserEvents("amir");

        assertEquals(1, supplierCalls.get());
        assertEquals(3, events.size());
        assertEquals("all-day", events.get(0).getEventId());
        assertEquals(LocalDate.of(2026, 8, 9), events.get(0).getActivityDate());
        assertEquals(LocalDate.of(2026, 8, 10), events.get(1).getActivityDate());
        assertEquals("page-two", events.get(2).getEventId());
        assertTrue(transport.urls.get(2).contains("pageToken=next-page"));
    }

    @Test
    void apiFailuresAreTranslatedForEachCalendarOperation() {
        final IllegalStateException addFailure = assertThrows(
                IllegalStateException.class,
                () -> {
                    failingDataAccess().addCalendarEvent(
                            "amir", "Meal", "Lunch", LocalDate.now());
                });
        final IllegalStateException removeFailure = assertThrows(
                IllegalStateException.class,
                () -> {
                    failingDataAccess().removeCalendarEvent("amir", "event-1");
                });
        final IllegalStateException loadFailure = assertThrows(
                IllegalStateException.class,
                () -> {
                    failingDataAccess().getUserEvents("amir");
                });

        assertEquals("Unable to add the calendar event.", addFailure.getMessage());
        assertEquals("Unable to remove the calendar event.", removeFailure.getMessage());
        assertEquals("Unable to load the user's calendar events.", loadFailure.getMessage());
    }

    private GoogleCalendarDataAccessObject failingDataAccess() {
        final QueueTransport transport = new QueueTransport();
        transport.enqueueError();
        return new GoogleCalendarDataAccessObject(calendar(transport));
    }

    private Calendar calendar(QueueTransport transport) {
        return new Calendar.Builder(
                transport, GsonFactory.getDefaultInstance(), request -> { })
                .setApplicationName("GitBuff Test")
                .build();
    }

    private static final class QueueTransport extends MockHttpTransport {
        private final Deque<MockLowLevelHttpResponse> responses = new ArrayDeque<>();
        private final List<String> methods = new ArrayList<>();
        private final List<String> urls = new ArrayList<>();
        private final List<String> requestBodies = new ArrayList<>();

        void enqueueJson(String content) {
            this.responses.add(new MockLowLevelHttpResponse()
                    .setStatusCode(200)
                    .setContentType("application/json; charset=UTF-8")
                    .setContent(content));
        }

        void enqueueNoContent() {
            this.responses.add(new MockLowLevelHttpResponse()
                    .setStatusCode(204)
                    .setZeroContent());
        }

        void enqueueError() {
            this.responses.add(new MockLowLevelHttpResponse()
                    .setStatusCode(500)
                    .setContentType("application/json; charset=UTF-8")
                    .setContent("{\"error\":{\"code\":500,\"message\":\"failure\"}}"));
        }

        @Override
        public LowLevelHttpRequest buildRequest(String method, String url) {
            this.methods.add(method);
            this.urls.add(url);
            return new MockLowLevelHttpRequest(url) {
                @Override
                public LowLevelHttpResponse execute() throws IOException {
                    QueueTransport.this.requestBodies.add(this.getContentAsString());
                    return QueueTransport.this.responses.removeFirst();
                }
            };
        }
    }
}
