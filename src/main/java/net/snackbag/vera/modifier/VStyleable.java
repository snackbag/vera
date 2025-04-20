package net.snackbag.vera.modifier;

public interface VStyleable {
    VStyleable parseStyleFromString(String val);

    boolean matchesStyleString(String val);

    String getStylingTypeId();
}
