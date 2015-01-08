package jensfranz.de.fritzboxgooglemusicindexer.event;

import jensfranz.de.fritzboxgooglemusicindexer.mvc.model.FritzboxInformation;

public class SignInPressedEvent {
    private final FritzboxInformation fritzboxInformation;

    public SignInPressedEvent(final FritzboxInformation fritzboxInformation) {
        this.fritzboxInformation = fritzboxInformation;
    }

    public FritzboxInformation getFritzboxInformation() {
        return fritzboxInformation;
    }
}
