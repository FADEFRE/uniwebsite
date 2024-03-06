<script setup>
import { onBeforeMount, ref } from "vue";
import { logout } from "@/router/logout"
import ButtonLink from "@/components/button/ButtonLink.vue";
import { getUserMe, putUserUsername, putUserPassword } from "@/requests/user-requests";
import { passwordRegex, usernameRegex } from "@/config/regex";

const userId = ref()

const username = ref()
const password = ref()
const passwordConfirm = ref()

onBeforeMount(() => {
  getUserMe()
    .then(data => {
      userId.value = data['userId']
      username.value = data['username']
    })
})

const usernameInvalid = ref(false)
const usernameExists = ref(false)

const passwordInvalid = ref(false)
const passwordNotMatching = ref(false)

const saveUsername = () => {
  // checking
  usernameInvalid.value = !usernameRegex.test(username.value)
  // request
  if (!usernameInvalid.value) {
    putUserUsername(userId.value, username.value)
        .then(_ => logout())
        .catch(error => {
          if (error.response.status === 409) {
            usernameExists.value = true
          }
        })
  }
}

const savePassword = () => {
  // checking
  passwordInvalid.value = !passwordRegex.test(password.value)
  passwordNotMatching.value = password.value !== passwordConfirm.value
  // request
  if (!passwordInvalid.value && !passwordNotMatching.value) {
    putUserPassword(userId.value, password.value, passwordConfirm.value)
        .then(_ => logout())
  }
}
</script>

<template>
  <div class="account-settings-container">

    <h2>Benutzereinstellungen</h2>
    <div class="settings-container">
      <div class="input-container">
        <label for="username">Benutzername ändern</label>
        <InputText type="text" class="white" v-model="username" id="username" :class="{ 'invalid': usernameInvalid || usernameExists }" />
        <small v-if="usernameInvalid" class="invalid-text">Dieser Benutzername ist nicht erlaubt</small>
        <small v-if="usernameExists" class="invalid-text">Benutzername existiert bereits</small>
      </div>
      <ButtonLink class="save-button" @click="saveUsername">Speichern</ButtonLink>
    </div>

    <div class="settings-container">
      <div class="password-container">
      <div class="input-container">
        <label for="password">Passwort ändern</label>
        <InputText type="password" class="white" v-model="password" id="password"
          :class="{ 'invalid': passwordNotMatching || passwordInvalid }" />
        <small v-if="passwordInvalid" class="invalid-text">Das Passwort darf nicht leer sein</small>
      </div>
      <div class="input-container">
        <label for="password-confirm">Passwort bestätigen</label>
        <InputText type="password" class="white" v-model="passwordConfirm" id="password-confirm"
          :class="{ 'invalid': passwordNotMatching || passwordInvalid }" />
        <small v-if="passwordNotMatching" class="invalid-text">Die Passwörter müssen übereinstimmen</small>
      </div>
    </div>
      <ButtonLink @click="savePassword">Speichern</ButtonLink>
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.account-settings-container {
  @include basicContainer();
  @include verticalListItem($white);
}

.settings-container {
  @include verticalList(s);
}

.password-container {
  @include screenSplit();
}
</style>