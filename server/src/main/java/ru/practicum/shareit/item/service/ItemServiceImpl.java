package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FalseBookerException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service("DBItem")
@RequiredArgsConstructor
@Primary
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemResponseDto> getUserItems(Integer userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> getItemById(item.getId(), userId))
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
    public ItemResponseDto getItemById(Integer itemId, Integer userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.findWhereAfterEnd(itemId, now).isEmpty() || !userId.equals(item.getOwner().getId()) ? null : bookingRepository.findWhereAfterEnd(itemId, now).getLast();
        Booking nextBooking = bookingRepository.findWhereStartBefore(itemId, now).isEmpty() || !userId.equals(item.getOwner().getId()) ? null : bookingRepository.findWhereStartBefore(itemId, now).getFirst();
        List<Comment> itemComments = commentRepository.findByItemId(itemId);
        return ItemMapper.toResponseDto(item, lastBooking, nextBooking, itemComments);
    }

    @Override
    @Transactional
    public Item addItem(Integer userId, Item item, Long requestId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        item.setOwner(owner);
        if (requestId != null) {
            ItemRequest request = itemRequestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запрос с таким id не найден в БД"));
            item.setRequest(request);
        }
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Integer userId, Item item) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        item.setOwner(owner);
        Item itemFromDb = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        if (!Objects.equals(itemFromDb.getOwner().getId(), userId)) {
            throw new NotOwnerException("Пользователь с id " + userId + " не является владельцем " + itemFromDb.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemFromDb.getDescription());
        }
        if (item.getName() == null) {
            item.setName(itemFromDb.getName());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemFromDb.getAvailable());
        }
        if (item.getRequest() == null || item.getRequest().getId() == null) {
            item.setRequest(itemFromDb.getRequest());
        }
        return itemRepository.save(item);
    }

    @Override
    public CommentResponseDto addComment(Integer authorId, Comment comment, Integer itemId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в базе данных"));
        comment.setAuthor(author);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден в базе данных"));
        comment.setItem(item);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> commentAuthorBookings = bookingRepository.findByBookerIdAndItemIdOrderByStartAsc(authorId, itemId).stream()
                .filter(booking -> booking.getEnd().isBefore(now.plusHours(4)))
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED))
                .toList();
        log.info("У пользователя с id {} нашлось {} законченных бронирований по предмету с id {}", authorId, commentAuthorBookings.size(), itemId);
        if (commentAuthorBookings.isEmpty()) {
            throw new FalseBookerException("Данный пользователь не брал в аренду данный предмет, либо аренда еще не закончилась");
        }
        Comment dbComment = commentRepository.save(comment);
        return CommentMapper.toResponseDto(dbComment);
    }
}
