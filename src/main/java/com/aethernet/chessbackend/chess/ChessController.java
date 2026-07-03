package com.aethernet.chessbackend.chess;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chess")
@RequiredArgsConstructor
public class ChessController {

    @GetMapping
    public String hello() {
        return "hello";
    }

}
