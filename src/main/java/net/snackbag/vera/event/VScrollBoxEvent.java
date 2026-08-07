package net.snackbag.vera.event;

public class VScrollBoxEvent {
    public record ScrolledX(double before, double after) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.ScrollBox.SCROLLED_X;
        }
    }

    public record ScrolledY(double before, double after) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.ScrollBox.SCROLLED_Y;
        }
    }
}
