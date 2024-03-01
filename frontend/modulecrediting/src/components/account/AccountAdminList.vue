<script setup>
import RoleDropdown from "@/components/util/RoleDropdown.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";
import { ref, onBeforeMount } from "vue";
import { deleteUser, getAllUsers, getUserMeId, putUserRole } from "@/requests/user-requests";

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

        <div v-if="user['userId'] === currentUserId" class="user-container">
          <div class="user-list-item user-self-list-item">
            <p class="username-text">{{ user.username }}</p>
          </div>
          
          <div class="user-self-role">
              <p class="overview-text">ADMIN</p>
          </div>

        </div>

        <div v-else class="user-container">
          <div class="user-list-item">
            <p class="username-text">{{ user.username }}</p>
          <TrashIcon @click="triggerDeleteUser(user.userId)" />
          </div>
          
          <RoleDropdown
              :model-value="user.role"
              @update:model-value="(newRole) => triggerChangeRole(user.userId, newRole)"
          />
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

.user-list-item {
  @include verticalListItem($gray);
  width: calc(100% - 17rem);
  padding: 0 spacing(m);
  align-items: center;
}
.user-container {
  display: flex;
}


.user-self-list-item {
  border-color: $dark-gray;
}

.user-self-role {
  @include smallHighlightBox();
  background-color: $dark-gray;
  width: 17rem;

  justify-content: flex-start;
}
</style>