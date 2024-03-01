<script setup>
import { onBeforeMount, ref } from "vue";
import { logout } from "@/router/logout"
import ButtonLink from "@/components/button/ButtonLink.vue";
import {getUserMe, putUserPassword, putUserUsername} from "@/requests/user-requests";

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

    <h2>{{$t('AccountSettings.Usersettings')}}</h2>
    <div class="settings-container">
      <div class="input-container">
        <label for="username">{{$t('AccountSettings.ChangeUsername')}}</label>
        <InputText type="text" class="white" v-model="username" id="username" :class="{ 'invalid': usernameEmpty || usernameExists }" />
        <small v-if="usernameEmpty" class="invalid-text">{{$t('AccountSettings.UsernameEmpty')}}</small>
        <small v-if="usernameExists" class="invalid-text">{{$t('AccountSettings.UsernameExists')}}</small>
      </div>
      <ButtonLink class="save-button" @click="saveUsername">{{$t('AccountSettings.save')}}</ButtonLink>
    </div>

    <div class="settings-container">
      <div class="password-container">
      <div class="input-container">
        <label for="password">{{$t('AccountSettings.ChangePassword')}}</label>
        <InputText type="password" class="white" v-model="password" id="password"
          :class="{ 'invalid': passwordNotMatching || passwordEmpty }" />
        <small v-if="passwordEmpty" class="invalid-text">{{$t('AccountSettings.PasswordEmpty')}}</small>
      </div>
      <div class="input-container">
        <label for="password-confirm">{{$t('AccountSettings.PasswordConfirm')}}</label>
        <InputText type="password" class="white" v-model="passwordConfirm" id="password-confirm"
          :class="{ 'invalid': passwordNotMatching || passwordEmpty }" />
        <small v-if="passwordNotMatching" class="invalid-text">{{$t('AccountSettings.Passwordsame')}}</small>
      </div>
    </div>
      <ButtonLink @click="savePassword">{{$t('AccountSettings.save')}}</ButtonLink>
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