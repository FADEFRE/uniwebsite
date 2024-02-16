<script setup>
import {onBeforeMount, ref} from "vue";
import {getUserMe, putUserPassword, putUserUsername} from "../scripts/axios-requests";
import { logout } from "../router/logout"

const userId = ref()

const username = ref()
const password = ref()
const passwordConfirm = ref()

onBeforeMount(() => {
  getUserMe()
      .then(data => {
        userId.value = data['userId']
        username.value = data['username']  // todo fix username null in backend
      })
})

const usernameEmpty = ref(false)
const usernameExists = ref(false)

const passwordNotMatching = ref(false)
const passwordEmpty = ref(false)

const saveUsername = () => {
  if (!username.value) {
    usernameEmpty.value = true
    return
  }
  putUserUsername(userId.value, username.value)
      .then(_ => logout())
      .catch(error => {
        if (error.response.status === 409) {
          usernameExists.value = true
        }
      })
}

const savePassword = () => {
  console.log(password.value)
  console.log(passwordConfirm.value)
  if (password.value !== passwordConfirm.value) {
    passwordNotMatching.value = true
    passwordEmpty.value = false
    return
  }
  if (!password.value) {
    passwordNotMatching.value = false
    passwordEmpty.value = true
    return
  }
  putUserPassword(userId.value, password.value, passwordConfirm.value)
      .then(_ => logout())
}
</script>

<template>
  <div class="account-settings-container">

    <h2>Benutzereinstellungen</h2>

    <div>
      <label for="username">Benutzername</label>
      <InputText type="text" v-model="username" id="username" :class="{ 'invalid': usernameEmpty || usernameExists }"/>
      <small v-if="usernameEmpty" class="invalid-text">Benutzername darf nicht leer sein</small>
      <small v-if="usernameExists" class="invalid-text">Benutzername existiert bereits</small>
      <Button @click="saveUsername">Speichern</Button>
    </div>

    <div>
      <label for="password">Passwort</label>
      <InputText type="text" v-model="password" id="password" :class="{ 'invalid': passwordNotMatching || passwordEmpty }"/>
      <label for="password-confirm">Passwort bestätigen</label>
      <InputText type="text" v-model="passwordConfirm" id="password-confirm" :class="{ 'invalid': passwordNotMatching || passwordEmpty }" />
      <small v-if="passwordNotMatching" class="invalid-text">Die Passwörter müssen übereinstimmen</small>
      <small v-if="passwordEmpty" class="invalid-text">Das Passwort darf nicht leer sein</small>
      <Button @click="savePassword">Speichern</Button>
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.account-settings-container {
  @include basicContainer();
}
</style>