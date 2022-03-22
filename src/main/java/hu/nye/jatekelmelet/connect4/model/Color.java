package hu.nye.jatekelmelet.connect4.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Color {
    EMPTY("( )"),
    RED("(R)"),
    BLACK("(B)"),
    WHITE("(W)");

    private final String value;
}
