package com.jvm.classloading;

/**
 * Created by yjh on 15-11-21.
 */
public class PolyConstructors {
    public static void main(String[] args) {
        new RoundGlyph(5);
    }

}

class Glyph {
    void draw() {System.out.println("Glyph");}
    Glyph() {
        System.out.println("Glyph() before draw()");
        draw();
        System.out.println("Glyph() after draw()");
    }
}

class RoundGlyph extends Glyph {
    private int radius = 1;
    RoundGlyph(int r) {
        radius = r;
        System.out.println("RoundGlyph.RoundGlyph(), radius = " + radius);
    }
    void draw() {
        System.out.println("RoundGlyph.draw(), radius = " + radius);
    }
}
