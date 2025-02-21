package com.sms.multitenantschool.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TenantSettings implements Serializable {

    @JsonProperty("customDomain")
    private CustomDomain customDomain;

    @JsonProperty("languagePreferences")
    private LanguagePreferences languagePreferences;

    @JsonProperty("timeZoneSettings")
    private TimeZoneSettings timeZoneSettings;

    @JsonProperty("brandingAssets")
    private BrandingAssets brandingAssets;

    @JsonProperty("customCSSJS")
    private CustomCSSJS customCSSJS;

    public CustomDomain getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(CustomDomain customDomain) {
        this.customDomain = customDomain;
    }

    public LanguagePreferences getLanguagePreferences() {
        return languagePreferences;
    }

    public void setLanguagePreferences(LanguagePreferences languagePreferences) {
        this.languagePreferences = languagePreferences;
    }

    public TimeZoneSettings getTimeZoneSettings() {
        return timeZoneSettings;
    }

    public void setTimeZoneSettings(TimeZoneSettings timeZoneSettings) {
        this.timeZoneSettings = timeZoneSettings;
    }

    public BrandingAssets getBrandingAssets() {
        return brandingAssets;
    }

    public void setBrandingAssets(BrandingAssets brandingAssets) {
        this.brandingAssets = brandingAssets;
    }

    public CustomCSSJS getCustomCSSJS() {
        return customCSSJS;
    }

    public void setCustomCSSJS(CustomCSSJS customCSSJS) {
        this.customCSSJS = customCSSJS;
    }

    // Nested Classes

    public static class CustomDomain {
        @JsonProperty("enabled")
        private boolean enabled;

        @JsonProperty("domain")
        private String domain;

        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    public static class LanguagePreferences {
        @JsonProperty("preferredLanguage")
        private String preferredLanguage;

        @JsonProperty("availableLanguages")
        private List<String> availableLanguages;

        // Getters and Setters
        public String getPreferredLanguage() {
            return preferredLanguage;
        }

        public void setPreferredLanguage(String preferredLanguage) {
            this.preferredLanguage = preferredLanguage;
        }

        public List<String> getAvailableLanguages() {
            return availableLanguages;
        }

        public void setAvailableLanguages(List<String> availableLanguages) {
            this.availableLanguages = availableLanguages;
        }
    }

    public static class TimeZoneSettings {
        @JsonProperty("timeZone")
        private String timeZone;

        @JsonProperty("availableTimeZones")
        private List<String> availableTimeZones;

        // Getters and Setters
        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public List<String> getAvailableTimeZones() {
            return availableTimeZones;
        }

        public void setAvailableTimeZones(List<String> availableTimeZones) {
            this.availableTimeZones = availableTimeZones;
        }
    }

    public static class BrandingAssets {
        @JsonProperty("logoUrl")
        private String logoUrl;

        @JsonProperty("faviconUrl")
        private String faviconUrl;

        @JsonProperty("backgroundImageUrl")
        private String backgroundImageUrl;

        @JsonProperty("fontFamily")
        private String fontFamily;

        @JsonProperty("emailTemplates")
        private Map<String, String> emailTemplates;

        // Getters and Setters
        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getFaviconUrl() {
            return faviconUrl;
        }

        public void setFaviconUrl(String faviconUrl) {
            this.faviconUrl = faviconUrl;
        }

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }

        public void setBackgroundImageUrl(String backgroundImageUrl) {
            this.backgroundImageUrl = backgroundImageUrl;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public Map<String, String> getEmailTemplates() {
            return emailTemplates;
        }

        public void setEmailTemplates(Map<String, String> emailTemplates) {
            this.emailTemplates = emailTemplates;
        }
    }

    public static class CustomCSSJS {
        @JsonProperty("customCSS")
        private String customCSS;

        @JsonProperty("customJS")
        private String customJS;

        // Getters and Setters
        public String getCustomCSS() {
            return customCSS;
        }

        public void setCustomCSS(String customCSS) {
            this.customCSS = customCSS;
        }

        public String getCustomJS() {
            return customJS;
        }

        public void setCustomJS(String customJS) {
            this.customJS = customJS;
        }
    }
}