<script setup>
import { ref, onBeforeMount } from "vue";
import { getAllUsers } from "../scripts/axios-requests";
import RoleDropdown from "./RoleDropdown.vue";

const userList = ref([])

onBeforeMount(() => {
  getAllUsers()
      .then(data => userList.value = data)
})
</script>

<template>
  <div>

    <h2>Alle Benutzer</h2>

    <div v-for="user in userList">
      <p>{{ user.username }}</p>
      <RoleDropdown />
      <img src="@/assets/icons/Trash.svg">
    </div>

  </div>
</template>

<style scoped lang="scss">

</style>