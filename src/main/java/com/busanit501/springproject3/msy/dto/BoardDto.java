package com.busanit501.springproject3.msy.dto;

import com.busanit501.springproject3.msy.entity.Board;
import com.busanit501.springproject3.msy.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDto {
    private Long id;
    private String title;
    private String writer;
    private String boardContent;
    private String filename;
    private String filepath;

    // LocalDateTime 필드에 JSON 직렬화 형식 적용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;

    private List<Comment> answerList;

    public Board toEntity() {
        Board board = new Board();
        board.setId(this.id);
        board.setTitle(this.title);
        board.setWriter(this.writer);
        board.setBoardContent(this.boardContent);
        board.setFilename(this.filename);
        board.setFilepath(this.filepath);
        board.setCreateDate(this.createDate);
        board.setModifyDate(this.modifyDate);
        return board;
    }

    public static BoardDto fromEntity(Board board) {
        BoardDto dto = new BoardDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setWriter(board.getWriter());
        dto.setBoardContent(board.getBoardContent());
        dto.setFilename(board.getFilename());
        dto.setFilepath(board.getFilepath());
        dto.setCreateDate(board.getCreateDate());
        dto.setModifyDate(board.getModifyDate());
        dto.setAnswerList(board.getComments());
        return dto;
    }
}
