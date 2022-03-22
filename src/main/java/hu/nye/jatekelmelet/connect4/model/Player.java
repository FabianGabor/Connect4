package hu.nye.jatekelmelet.connect4.model;

import lombok.Getter;

@Getter
public class Player {
    private final String name;
    private final Color color;
    private final String symbol;
    private final String type;

    public Player(String name, Color color, String type) {
        if(color.equals(Color.EMPTY)) throw new IllegalArgumentException("Player cannot have an empty symbol");

        this.name = name;
        this.color = color;
        this.symbol = color.getValue();
        this.type = type;
    }

    public String toString(){
        String s = "Player: " + name + '\n';
        s += "Color: " + color + '\n';
        s += "Symbol: " + symbol + '\n';

        return s;
    }
}
