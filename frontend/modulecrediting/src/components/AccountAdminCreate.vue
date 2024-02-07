<script setup>
import { ref } from "vue";
import ButtonLink from "./ButtonLink.vue";
import { createUser } from "../scripts/axios-requests";

const username = ref()
const password = ref()
const passwordConfirm = ref()

const roleOptions = [
  { value: 'ROLE_STUDY', label: 'STUDIENBUERO' },
  { value: 'ROLE_CHAIR', label: 'PRUEFUNGSAUSSCHUSS' },
  { value: 'ROLE_ADMIN', label: 'ADMIN' }
]
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
  <div>

    <h2>Benutzer erstellen</h2>

    <InputText type="text" placeholder="Benutzername" v-model="username" :class="{ 'invalid': createFailed }" />
    <small v-if="createFailed">Es existiert bereits ein Benutzer mit diesem Namen</small>
    <InputText type="text" placeholder="Passwort" v-model="password" />
    <InputText type="text" placeholder="Passwort bestätigen" v-model="passwordConfirm" />

    <Dropdown placeholder="Rolle" v-model="role" :options="roleOptions" option-value="value" option-label="label" >
      <template #dropdownicon>
        <img src="../assets/icons/ArrowWhite.svg">
      </template>
    </Dropdown>

    <ButtonLink :red-button="true" @click="triggerCreateUser">
      <p>Benutzer erstellen</p>
    </ButtonLink>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
</style>