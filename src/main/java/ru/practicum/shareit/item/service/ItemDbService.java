package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FalseBookerException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoToShow;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service("DBItem")
@RequiredArgsConstructor
@Primary
public class ItemDbService implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDtoToShow> getUserItems(Integer userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> getItemById(item.getId()))
                .toList();
    }

    @Override
    public List<Item> getItemsByCaption(String caption) {
        if (caption.isBlank()) {
            return List.of();
        }
        return itemRepository.search(caption);
    }

    @Override
    public ItemDtoToShow getItemById(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.findByItemIdAndEndAfterOrderByStartAsc(itemId, now).isEmpty() ? null : bookingRepository.findByItemIdAndEndAfterOrderByStartAsc(itemId, now).getLast();
        Booking nextBooking = bookingRepository.findByItemIdAndStartBeforeOrderByStartAsc(itemId, now).isEmpty() ? null : bookingRepository.findByItemIdAndStartBeforeOrderByStartAsc(itemId, now).getFirst();
        List<Comment> itemComments = commentRepository.findByItemId(itemId);
        return ItemMapper.toWatchModel(item, lastBooking, nextBooking, itemComments);
    }

    @Override
    public Item addItem(Integer userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        return itemRepository.save(ItemMapper.toRegularModel(0, itemDto, owner, null));
    }

    @Override
    public Item updateItem(Integer userId, ItemDto item, int itemId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        Item itemFromDb = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        if (itemFromDb.getOwner().getId() != userId) {
            throw new NotOwnerException("Пользователь с id " + userId + " не является владельцем " + itemFromDb.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemFromDb.getDescription());
        }
        if (item.getName() == null) {
            item.setName(itemFromDb.getName());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemFromDb.isAvailable());
        }
        return itemRepository.save(ItemMapper.toRegularModel(itemId, item, owner, null));
    }

    @Override
    @Transactional
    public Comment addComment(Integer authorId, CommentDto dto, Integer itemId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        List<Booking> commentAuthorBookings = bookingRepository.findByBookerIdAndEndBeforeOrderByStartAsc(authorId, LocalDateTime.now()).stream()
                .filter(booking -> booking.getItem().getId() == itemId)
                .toList();
        if (commentAuthorBookings.isEmpty()) {
            throw new FalseBookerException("Данный пользователь не брал в аренду данный предмет, либо аренда еще не закончилась");
        }
        Comment newComment = CommentMapper.toRegularModel(0, author, dto, item, LocalDateTime.now());
        return commentRepository.save(newComment);
    }
}
