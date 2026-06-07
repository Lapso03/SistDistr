package com.example.demo.controller;

import com.example.demo.dto.FaqChatQuestion;
import com.example.demo.dto.FaqChatResponse;
import com.example.demo.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/faq")
public class FaqController {

    @Autowired private FaqService faqService;

    @GetMapping
    public String faqPage(Model model) {
        model.addAttribute("conversationId", UUID.randomUUID().toString());
        model.addAttribute("chatQuestion", new FaqChatQuestion());
        return "faq";
    }

    @PostMapping
    public String faqPost(@ModelAttribute FaqChatQuestion chatQuestion,
                          @RequestParam String conversationId,
                          Authentication auth,
                          Model model) {
        FaqChatResponse response = faqService.getAnswer(
                chatQuestion.getQuestion(), conversationId);

        model.addAttribute("question", chatQuestion.getQuestion());
        model.addAttribute("response", response);
        model.addAttribute("conversationId", conversationId);
        model.addAttribute("chatQuestion", new FaqChatQuestion());
        return "faq";
    }
}