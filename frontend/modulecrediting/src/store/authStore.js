import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        currentUser: { user: false },
        intervalName: "",
        nav: "user",
        locale: 'de'
    }),
    
    persist: {
        storage: sessionStorage,
    },

    getters: {
        getCurrentUser() { return this.currentUser.user },
        getCurrentRoleNav() { return this.nav },
        getLocale() { return this.locale }
    },

    actions: {
        setIntervalName(intervalName) {this.intervalName = intervalName },
        setCurrentUser(loggedInUser) { this.currentUser.user = loggedInUser },
        setLocale(newLocale) { this.locale = newLocale },
        setCurrentRoleNav(currentRole) { this.nav = currentRole },
        async logout() { 
            this.nav = "user";
            this.currentUser.user = false;
        },
    },
})