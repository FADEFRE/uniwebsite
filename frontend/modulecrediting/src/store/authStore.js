import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        currentUser: { userId: "-1" },
        intervalName: "",
        nav: "user",
        locale: 'DE'
    }),
    
    persist: {
        storage: sessionStorage,
    },

    getters: {
        getCurrentUserId() { return this.currentUser.userId },
        getCurrentRoleNav() { return this.nav },
    },

    actions: {
        setIntervalName(intervalName) {this.intervalName = intervalName },
        setCurrentUser(currentUser) { this.currentUser = currentUser },
        setCurrentRoleNav(currentRole) { this.nav = currentRole },
        async logout() { 
            this.nav = "user"
            this.currentUser.userId = "-1" 
        },
    },
})