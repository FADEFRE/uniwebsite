import { defineStore } from 'pinia';


export const useAuthStore = defineStore('authUser',{
    state: () => ({
        isAuthenticated: false,
        currentUser: {
            userId: "",
            username: ""
        },
        intervalName: "" 
    }),

    actions: {
        async login(loginData) {},
        setIsAuthenticated(value) { this.isAuthenticated = value; },
        setIntervalName(intervalName) {this.intervalName = intervalName; },
        //async refresh() {},
        async logout() { 
            this.userId = "";
            this.username = "";
            this.isAuthenticated = false }
    }
})