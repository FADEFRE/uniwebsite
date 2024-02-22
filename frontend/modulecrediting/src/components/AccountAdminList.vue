<script setup>
import RoleDropdown from "./RoleDropdown.vue";
import TrashIcon from "../assets/icons/TrashIcon.vue";
import { ref, onBeforeMount } from "vue";
import { getAllUsers, getUserMeId, putUserRole, deleteUser } from "../scripts/axios-requests";

let currentUserId = undefined
const userList = ref([])

onBeforeMount(() => {
  getUserMeId()
      .then(userData => {
        currentUserId = userData['userId']
        return getAllUsers()
      })
      .then(data => {
        userList.value = data.toSorted((a, b) => a['userId'] - b['userId'])
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
      <div v-for="user in userList">

        <div v-if="user['userId'] === currentUserId" class="user-container user-self-item">
          <p class="username-text">{{ user.username }}</p>
          <div class="right-side">
            <div class="user-self-container">
              <p class="overview-text">ADMIN</p>
            </div>
          </div>
          
        </div>

        <div v-else class="user-container">
          <p class="username-text">{{ user.username }}</p>
          <div class="right-side">
            <RoleDropdown
              :model-value="user.role"
              @update:model-value="(newRole) => triggerChangeRole(user.userId, newRole)"
          />
          <TrashIcon @click="triggerDeleteUser(user.userId)" />
          </div>
          
        </div>

      </div>
    </div>


  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.admin-list-container {
  @include basicContainer();
}

.user-list {
  @include verticalList(s);
}

.username-text {
  width: 50%
}

.user-container {  
  @include verticalListItem($gray);
  padding: 0 spacing(m);
  align-items: center;
}
.right-side {
  display: flex;
  width: 50%;
  justify-content: space-between;
}

.user-self-item {
  @include verticalListItem($gray);
  border-color: $dark-gray;
}
.user-self-container {
  @include smallHighlightBox();
  background-color: $dark-gray;
}
</style>