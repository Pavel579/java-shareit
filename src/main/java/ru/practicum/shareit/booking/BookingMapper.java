package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BookingMapper {
    public BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd());

    }

    public Booking mapToBooking(BookingDto bookingDto, Item item, User user) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                BookingStatus.WAITING
        );
    }

    /*public BookingGetDto mapToBookingGetDto(Booking booking, Item item) {
        return new BookingGetDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBookerId(),
                item.getId(),
                item.getName()
        );
    }*/

    /*public BookingOwnerDto mapToBookingOwnerDto(Booking booking, Item item){
        return new BookingOwnerDto(
                booking.getId(),
                booking.getStatus(),
                booking.getBookerId(),
                item.getId(),
                item.getName()
        );
    }*/

    public BookingGetDto mapToBookingGetDto(Booking booking){
        return new BookingGetDto(
                booking.getId(),
                booking.getStatus(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker()
        );
    }

    public List<BookingGetDto> mapToListBookingGetDto(List<Booking> list){
        return list.stream().map(this::mapToBookingGetDto).collect(Collectors.toList());
    }

    /*public List<BookingGetDto> mapToListBookingGetDto(Map<Booking, Item> bookings){
        List<BookingGetDto> list = new ArrayList<>();
        for (Map.Entry<Booking, Item> entry : bookings.entrySet()){
            list.add(mapToBookingGetDto(entry.getKey(), entry.getValue()));
        }
        return list;
    }*/


}
