package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public Item mapToItem(ItemDto itemDto, User user) {
        return new Item(null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                null
        );
    }

    public ItemBookingDto mapToItemBookingDto(Item item,
                                              ItemBookingDto.BookingDto lastBookingDto,
                                              ItemBookingDto.BookingDto nextBookingDto,
                                              List<CommentDto> comments){
        return new ItemBookingDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                lastBookingDto,
                nextBookingDto,
                comments);
    }

    public List<CommentDto> mapToListCommentDto(List<Comment> list){
        return list.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

    public ItemBookingDto.BookingDto mapToLastNextBookingDto(List<Booking> list){
        if (list!=null && !list.isEmpty()){
            return new ItemBookingDto.BookingDto(list.get(0).getId(),
                    list.get(0).getStart(),
                    list.get(0).getEnd(),
                    list.get(0).getBooker().getId());
        }else {
            return null;
        }
    }

    public List<ItemDto> mapToListItemDto(List<Item> items) {
        return items.stream().map(this::mapToItemDto).collect(Collectors.toList());
    }

    public Comment mapToComment(CommentDto commentDto, Item item, User user){
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                item,
                user,
                LocalDateTime.now());
    }

    public CommentDto mapToCommentDto(Comment comment){
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
