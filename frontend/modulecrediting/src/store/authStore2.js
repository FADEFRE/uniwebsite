import { defineStore } from 'pinia';


export const useAuthStore = defineStore('authUser',{
    state: () => ({
        isAuthenticated: false,
        currentUser: {
            userId: "-1",
            username: ""
        },
        intervalName: "" 
    }),

    getters: {
        getIsAuthenticated() { return this.isAuthenticated },
        getCurrentUserId() { return this.currentUser.userId }
    },


    actions: {
        setIsAuthenticated(value) { this.isAuthenticated = value; },
        setIntervalName(intervalName) {this.intervalName = intervalName; },
        setCurrentUser(currentUser) { this.currentUser = currentUser},
        //async refresh() {},
        async logout() { 
            this.userId = "-1";
            this.username = "";
            this.isAuthenticated = false }
    }
})