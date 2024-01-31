import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        currentUser: { user: false },
        intervalName: "",
        nav: "user",
        locale: 'DE'
    }),
    
    persist: {
        storage: sessionStorage,
    },

    getters: {
        getCurrentUser() { return this.currentUser.user },
        getCurrentRoleNav() { return this.nav },
    },

    actions: {
        setIntervalName(intervalName) {this.intervalName = intervalName },
        setCurrentUser(loggedInUser) { this.currentUser.user = loggedInUser },
        setCurrentRoleNav(currentRole) { this.nav = currentRole },
        async logout() { 
            this.nav = "user";
            this.currentUser.user = false;
        },
    },
})