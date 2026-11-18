package net.snackbag.vera.event;

public class VLineInputEvent {
    public record CharLimited(char chr) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.LineInput.ADD_CHAR_LIMITED;
        }
    }
}
