package jensfranz.de.fritzboxgooglemusicindexer.business;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Used for XML Parsing of session data.
 */
@Root(name = "SessionInfo")
public class SessionInfo {
    @Element(name = "SID")
    private String sid;

    @Element(name = "Challenge")
    private String challenge;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
