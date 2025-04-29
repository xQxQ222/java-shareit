package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getBookingById(Integer userId, int bookingId) {
        return get("/" + bookingId, userId);
    }


    public ResponseEntity<Object> getBookingByCurrentUser(Integer userId, String state) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("?state={state}", Long.valueOf(userId), parameters);
    }

    public ResponseEntity<Object> getBookingsByItemOwner(Integer userId, String state) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner?state={state}", Long.valueOf(userId), parameters);
    }

    public ResponseEntity<Object> approveBooking(boolean approved, Integer bookingId, Integer userId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", Long.valueOf(userId), parameters, null);
    }

    public ResponseEntity<Object> createBooking(Integer userId, BookingRequestDto bookingRequestDto) {
        return post("", Long.valueOf(userId), bookingRequestDto);
    }
}
