<script setup>
import { ref } from "vue";
import ButtonLink from "./ButtonLink.vue";
import RoleDropdown from "./RoleDropdown.vue";
import { createUser } from "@/scripts/axios-requests";

const username = ref('')
const password = ref('')
const passwordConfirm = ref('')
const role = ref('')

const usernameEmpty = ref(false)
const passwordEmpty = ref(false)
const passwordConfirmEmpty = ref(false)
const roleEmpty = ref(false)

const createFailed = ref(false)

const triggerCreateUser = () => {
  // checking empty
  usernameEmpty.value = !username.value
  passwordEmpty.value = !password.value
  passwordConfirmEmpty.value = !passwordConfirm.value
  roleEmpty.value = !role.value
  // request
  if (!usernameEmpty.value && !passwordEmpty.value && !passwordConfirmEmpty.value && !roleEmpty.value) {
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
}
</script>

<template>
  <div class="admin-create-container">

    <h2>Benutzer erstellen</h2>
    <div class="first-row">
      <div class="input-container">
        <InputText type="text" placeholder="Benutzername" v-model="username"
                   class="button-height white" :class="{ 'invalid': usernameEmpty || createFailed }" />
        <small v-if="usernameEmpty" class="invalid-text">Der Benutzername darf nicht leer sein</small>
        <small v-if="createFailed" class="invalid-text">Es existiert bereits ein Benutzer mit diesem Namen</small>
      </div>
      <RoleDropdown v-model="role" :class="{ 'invalid': roleEmpty }" />
      <small v-if="roleEmpty" class="invalid-text">Es muss eine Rolle ausgewählt sein</small>
    </div>
    <div class="second-row">
      <InputText type="text" placeholder="Passwort" v-model="password" class="white" :class="{ 'invalid': passwordEmpty }" />
      <small v-if="passwordEmpty" class="invalid-text">Passwort darf nicht leer sein</small>
      <InputText type="text" placeholder="Passwort bestätigen" v-model="passwordConfirm" class="white" :class="{ 'invalid': passwordConfirmEmpty }" />
      <small v-if="passwordConfirmEmpty" class="invalid-text">Passwort bestätigen darf nicht leer sein</small>
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