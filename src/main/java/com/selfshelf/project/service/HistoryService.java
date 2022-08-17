package com.selfshelf.project.service;

import com.selfshelf.project.model.History;

import com.selfshelf.project.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    public List<History> findIssueHistoryByUserId (Long userId) {
        List<History> historyList = historyRepository.findAllIssues();
        return historyList.stream().filter(history -> history.getIssuedBook().getUserEntity().getId().equals(userId)).collect(Collectors.toList());
    }

    public List<History> findIssueHistoryByBookId (Long bookId) {
        List<History> historyList = historyRepository.findAllIssues();
        return historyList.stream().filter(history -> history.getIssuedBook().getBookEntity().getId().equals(bookId)).collect(Collectors.toList());
    }


}
