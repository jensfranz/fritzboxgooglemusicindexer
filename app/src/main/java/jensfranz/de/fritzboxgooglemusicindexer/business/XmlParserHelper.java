package jensfranz.de.fritzboxgooglemusicindexer.business;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XmlParserHelper {

    public static String parseChallenge(final String xmlSidResponse) {
        final Serializer serializer = new Persister();
        final SessionInfo sessionInfo = getSessionInfo(xmlSidResponse, serializer);
        return sessionInfo.getChallenge();
    }

    private static SessionInfo getSessionInfo(String xmlSidResponse, Serializer serializer) {
        final SessionInfo sessionInfo;
        try {
            sessionInfo = serializer.read(SessionInfo.class, xmlSidResponse, false);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return sessionInfo;
    }

    public static String parseSid(final String xmlSidResponse) {
        final Serializer serializer = new Persister();
        final SessionInfo sessionInfo = getSessionInfo(xmlSidResponse, serializer);
        return sessionInfo.getSid();
    }
}