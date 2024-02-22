<script setup>
import { ref } from "vue";
import ButtonLink from "./ButtonLink.vue";
import { createUser } from "../scripts/axios-requests";
import RoleDropdown from "./RoleDropdown.vue";

const username = ref()
const password = ref()
const passwordConfirm = ref()
const role = ref()

const createFailed = ref(false)

const triggerCreateUser = () => {
  createUser(username.value, password.value, passwordConfirm.value, role.value)
    .then(_ => location.reload())
    .catch(error => {
      if (error.response.status) {
        createFailed.value = true
      } else {
        location.reload()
      }
    })
}
</script>

<template>
  <div class="admin-create-container">

    <h2>Benutzer erstellen</h2>
    <div class="first-row">
      <div class="input-container">
        <InputText type="text" class="button-height white" placeholder="Benutzername" v-model="username"
          :class="{ 'invalid': createFailed }" />
        <small v-if="createFailed" class="invalid-text">Es existiert bereits ein Benutzer mit diesem Namen</small>
      </div>
      <RoleDropdown v-model="role" />
    </div>
    <div class="second-row">
      <InputText type="text" class="white" placeholder="Passwort" v-model="password" />
      <InputText type="text" class="white" placeholder="Passwort bestätigen" v-model="passwordConfirm" />
    </div>


    <ButtonLink :redButton="true" @click="triggerCreateUser">Benutzer erstellen</ButtonLink>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.admin-create-container {
  @include basicContainer();
}

.first-row {
  display: flex;
  width: 100%;
  gap: spacing(m);
}

.second-row {
  @include screenSplit();
}</style>