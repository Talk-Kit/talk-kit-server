package com.canal.dto;

import java.util.*;

public record ResponseOpenAiApi(List<Choice> choices) {
    public record Choice(int index, Message message) {}
    public record Message(String role,String content) {}
}
