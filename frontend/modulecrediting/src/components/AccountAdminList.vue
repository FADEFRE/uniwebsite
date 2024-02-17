<script setup>
import { ref, onBeforeMount } from "vue";
import {deleteUser, getAllUsers, getUserMeId, putUserRole} from "../scripts/axios-requests";
import RoleDropdown from "./RoleDropdown.vue";
import TrashIcon from "../assets/icons/TrashIcon.vue";

let currentUserId = undefined
const userList = ref([])

onBeforeMount(() => {
  getUserMeId()
      .then(userData => {
        currentUserId = userData['userId']
        return getAllUsers()
      })
      .then(data => {
        userList.value = data.filter(user => user['userId'] !== currentUserId).toSorted((a, b) => a['userId'] - b['userId'])
      })
})

const triggerChangeRole = (id, newRole) => {
  putUserRole(id, newRole)
    .then(_ => location.reload())
}

const triggerDeleteUser = (id) => {
  deleteUser(id)
      .then(_ => location.reload())
}
</script>

<template>
  <div class="admin-list-container">

    <h2>Alle Benutzer</h2>

    <div class="user-list">
      <div v-for="user in userList" class="user-container">
        <p>{{ user.username }}</p>
        <RoleDropdown :model-value="user.role"
          @update:model-value="(newRole) => triggerChangeRole(user.userId, newRole)" />
          <div class="trash-icon-wrapper">
          <TrashIcon @click="triggerDeleteUser(user.userId)" />
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.admin-list-container {
  @include basicContainer();
}

.user-list {
  @include verticalList(small);
}

.user-container {
  @include verticalListItem($gray);
  padding: 0 spacing(m);
  align-items: center;
}

.trash-icon-wrapper {
  @include smallHighlightBox();
  transition: 0.1s ease-in-out;
  &:hover {
    background-color: $gray-hover;
  }
}
</style>