import i18n from "@/i18n"
import { useUserStore } from "@/store/authStore";

const translate = {
    get supportedLocales() {
        return import.meta.env.VITE_SUPPORTED_LOCALES.split(",")
    },

    set currentLocale(newLocale) {
        i18n.global.locale.value = newLocale
    },

    async switchLanguage(newLocale) {
        translate.currentLocale = newLocale
        const store = useUserStore()
        store.locale = newLocale
    },

    setCurrentLocale(newLocale) {
        translate.currentLocale = newLocale
    },

    isLocaleSuppported(locale) {
        return translate.supportedLocales.includes(locale)
    },

    get defaultLocale() {
        return import.meta.env.VITE_DEFAULT_LOCALE
    },

    getUserLocale() {
        const locale = window.navigator.language ||
        window.navigator.userLanguage ||
        translate.defaultLocale
        return { locale: locale }
    },


    guessDefaultLocale() {
        const store = useUserStore()
        const userPersistedLocale = store.getLocale
        if (userPersistedLocale !== null && translate.isLocaleSuppported(userPersistedLocale)) { 
            return userPersistedLocale 
        }
        const userPreferedLocale = translate.getUserLocale()
        if(translate.isLocaleSuppported(userPreferedLocale)) { return userPreferedLocale }
        return translate.defaultLocale
    }

}

export default translate