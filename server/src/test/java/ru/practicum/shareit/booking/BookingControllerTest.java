package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.IncorrectStateException;
import ru.practicum.shareit.exceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingServiceImpl bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private BookingDto bookingDto;
    private BookingResponseDto bookingResponseDto;
    private PageRequest pageRequest;

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "name1", "mail@mail.ru");
        Item item = new Item(1L, "item1", "description1", true, user, null);
        bookingDto = new BookingDto(1L, 1L,
                LocalDateTime.of(2023, 5, 23, 12, 0), LocalDateTime.MAX);
        bookingResponseDto = new BookingResponseDto(1L, BookingStatus.APPROVED,
                LocalDateTime.of(2023, 5, 23, 12, 0), LocalDateTime.MAX, item, user);
        pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "start"));
    }

    @Test
    void createNewBookingTest() throws Exception {
        when(bookingService.createNewBooking(bookingDto, 1L)).thenReturn(bookingResponseDto);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item.id").value(1L));
        verify(bookingService, times(1)).createNewBooking(bookingDto, 1L);
    }

    @Test
    void createNewBookingWithItemNotAvailableTest() throws Exception {
        when(bookingService.createNewBooking(bookingDto, 1L)).thenThrow(new ItemIsNotAvailableException(""));
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approveBookingTest() throws Exception {
        when(bookingService.approveBooking(1L, 1L, true)).thenReturn(bookingResponseDto);
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item.name").value("item1"));
        verify(bookingService, times(1)).approveBooking(1L, 1L, true);
    }

    @Test
    void approveBookingWithIncorrectStateTest() throws Exception {
        when(bookingService.approveBooking(1L, 1L, true)).thenThrow(new IncorrectStateException(""));
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingResponseDto);
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.item.name").value("item1"));
        verify(bookingService, times(1)).getBookingById(1L, 1L);
    }

    @Test
    void getBookingByIncorrectIdTest() throws Exception {
        when(bookingService.getBookingById(1L, 10L)).thenThrow(new BookingNotFoundException(""));
        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBookingsByUserIdTest() throws Exception {
        when(bookingService.getAllBookingsByUserId(1L, BookingState.ALL, pageRequest))
                .thenReturn(Collections.singletonList(bookingResponseDto));
        mockMvc.perform(get("/bookings?state=ALL&from=0&size=2")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].item.name").value("item1"));
        verify(bookingService, times(1))
                .getAllBookingsByUserId(1L, BookingState.ALL,
                        pageRequest);
    }

    @Test
    void getAllBookingsOfCurrentUserItemsTest() throws Exception {
        when(bookingService.getAllBookingsOfCurrentUserItems(1L, BookingState.ALL, pageRequest))
                .thenReturn(Collections.singletonList(bookingResponseDto));
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=2")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].item.name").value("item1"));
        verify(bookingService, times(1))
                .getAllBookingsOfCurrentUserItems(1L, BookingState.ALL,
                        pageRequest);
    }
}
