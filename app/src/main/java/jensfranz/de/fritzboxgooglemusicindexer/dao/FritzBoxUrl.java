package jensfranz.de.fritzboxgooglemusicindexer.dao;

import java.text.MessageFormat;

/**
 * Provides URLs to FritzBoxs functions.
 */
public class FritzBoxUrl {
    private static final String urlTemplate = "http://{0}{1}";
    private static final String loginMapping = "/login_sid.lua";
    private static final String loginParams = "?user=&response={0}";
    private static final String mediaSettingsMapping = "/storage/media_settings.lua";
    private static final String mediaSettingsParams = "?sid={0}";

    public static String getLoginUrl(final String host) {
        return MessageFormat.format(urlTemplate, host, loginMapping);
    }

    public static String getLoginUrl(final String host, final String challengeResponse) {
        final String loginUrl = getLoginUrl(host);
        return MessageFormat.format(loginUrl + loginParams, challengeResponse);
    }

    public static String getMediaSettingsUrl(final String host) {
        return MessageFormat.format(urlTemplate, host, mediaSettingsMapping);
    }

    public static String getMediaSettingsUrl(final String host, final String sid) {
        final String mediaSettingsUrl = getMediaSettingsUrl(host);
        return MessageFormat.format(mediaSettingsUrl + mediaSettingsParams, sid);
    }
}
