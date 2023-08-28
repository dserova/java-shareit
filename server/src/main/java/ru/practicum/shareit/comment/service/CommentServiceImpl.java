package ru.practicum.shareit.comment.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.error.BookingBadRequestException;
import ru.practicum.shareit.error.CommentNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(long userId, long itemId, @NonNull Comment comment) {
        LocalDateTime dateTime = LocalDateTime.now();
        Booking b = bookingRepository.findFirstByBooker_IdAndEndBeforeOrderByStartDesc(userId, dateTime).orElseThrow(BookingBadRequestException::new);
        comment.setAuthor(b.getBooker());
        comment.setItem(b.getItem());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentByItem(long userId, long itemId) {
        return commentRepository.findByItem_Id(itemId).orElseThrow(CommentNotFoundException::new);
    }
}
