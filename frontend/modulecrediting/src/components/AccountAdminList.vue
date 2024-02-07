<script setup>
import { ref, onBeforeMount } from "vue";
import {getAllUsers, putUserRole} from "../scripts/axios-requests";
import RoleDropdown from "./RoleDropdown.vue";

const userList = ref([])

onBeforeMount(() => {
  getAllUsers()
      .then(data => userList.value = data.toSorted((a, b) => a['userId'] - b['userId']))
})

const triggerChangeRole = (id, newRole) => {
  putUserRole(id, newRole)
      .then(_ => location.reload())
}
</script>

<template>
  <div>

    <h2>Alle Benutzer</h2>

    <div v-for="user in userList">
      <p>{{ user.username }}</p>
      <RoleDropdown :model-value="user.role" @update:model-value="(newRole) => triggerChangeRole(user.userId, newRole)" />
      <img src="@/assets/icons/Trash.svg">
    </div>

  </div>
</template>

<style scoped lang="scss">

</style>