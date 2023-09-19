package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(long userId, long itemId, Comment comment);

    List<Comment> getCommentByItem(long userId, long itemId);
}
