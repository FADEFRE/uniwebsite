<script setup>
import { ref } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import RoleDropdown from "@/components/util/RoleDropdown.vue";

import {postNewUser} from "@/requests/user-requests";

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
    postNewUser(username.value, password.value, passwordConfirm.value, role.value)
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

    <h2>{{ $t('AccountAdminCreate.CreateUser') }}</h2>
    <div class="first-row">
      <div class="input-container">
        <InputText type="text" :placeholder="$t('AccountAdminCreate.Username')" v-model="username"
                   class="button-height white" :class="{ 'invalid': usernameEmpty || createFailed }" />
        <small v-if="usernameEmpty" class="invalid-text">{{ $t('AccountAdminCreate.UsernameEmpty') }}</small>
        <small v-if="createFailed" class="invalid-text">{{ $t('AccountAdminCreate.UserExists') }}</small>
      </div>
      <RoleDropdown v-model="role" :invalid="roleEmpty" />
    </div>
    <div class="second-row">
      <div class="input-container">
        <InputText type="text" :placeholder="$t('AccountAdminCreate.Password')" v-model="password" class="white" :class="{ 'invalid': passwordEmpty }" />
        <small v-if="passwordEmpty" class="invalid-text">{{ $t('AccountAdminCreate.PasswordEmpty') }}</small>
      </div>
      <div class="input-container">
        <InputText type="text" :placeholder="$t('AccountAdminCreate.ConfirmPassword')" v-model="passwordConfirm" class="white" :class="{ 'invalid': passwordConfirmEmpty }" />
        <small v-if="passwordConfirmEmpty" class="invalid-text">{{ $t('AccountAdminCreate.ConfirmPasswordEmpty') }}</small>
      </div>
      
    </div>


    <ButtonLink :redButton="true" @click="triggerCreateUser">{{ $t('AccountAdminCreate.CreateUserButton') }}</ButtonLink>

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