import { defineStore } from 'pinia';


export const useNavTypeStore = defineStore('navRole',{
    state: () => ({
        currentRole: "user"
    }),

    getters: {
        getCurrentRoleNav() { return this.currentRole }
    },


    actions: {
        setCurrentRoleNav(currentRole) { this.currentRole = currentRole }
    }
})