package taha.kamel.multithreading.color;

public record Color(String color) {
    @Override
    public String toString() {
        return color + "*" + Colors.WHITE.color();
    }
}
