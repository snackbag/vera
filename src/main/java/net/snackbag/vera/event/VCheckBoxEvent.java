package net.snackbag.vera.event;

public class VCheckBoxEvent {
    public record StateChanged(boolean checked) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.CheckBox.CHECK_STATE_CHANGED;
        }
    }
}
