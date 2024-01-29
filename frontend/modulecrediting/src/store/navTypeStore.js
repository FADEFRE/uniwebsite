import { defineStore } from 'pinia';


export const useNavTypeStore = defineStore('navRole',{
    state: () => ({
        currentRole: "user",
        locale: 'DE'
    }),

    getters: {
        getCurrentRoleNav() { return this.currentRole }
    },


    actions: {
        setCurrentRoleNav(currentRole) { this.currentRole = currentRole },
        logout() { this.currentRole = "user" }
    }
})