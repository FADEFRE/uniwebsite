<script setup>
import { ref } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import RoleDropdown from "@/components/util/RoleDropdown.vue";
import { postNewUser } from "@/requests/user-requests";
import { usernameRegex, passwordRegex } from "@/config/regex";

const username = ref('')
const password = ref('')
const passwordConfirm = ref('')
const role = ref('')

const usernameInvalid = ref(false)
const passwordInvalid = ref(false)
const passwordConfirmInvalid = ref(false)
const roleEmpty = ref(false)

const createFailed = ref(false)

const triggerCreateUser = () => {
  // checking empty
  usernameInvalid.value = !usernameRegex.test(username.value)
  passwordInvalid.value = !passwordRegex.test(password.value)
  passwordConfirmInvalid.value = password.value !== passwordConfirm.value
  roleEmpty.value = !role.value
  // request
  if (!usernameInvalid.value && !passwordInvalid.value && !passwordConfirmInvalid.value && !roleEmpty.value) {
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
                   class="button-height white" :class="{ 'invalid': usernameInvalid || createFailed }" />
        <small v-if="usernameInvalid" class="invalid-text">$t('AccountAdminCreate.UsernameForbidden')</small>
        <small v-if="createFailed" class="invalid-text">$t('AccountAdminCreate.UserExists')</small>
      </div>
      <RoleDropdown v-model="role" :invalid="roleEmpty" />
    </div>
    <div class="second-row">
      <div class="input-container">
        <InputText type="text" :placeholder="$t('AccountAdminCreate.Password')" v-model="password" class="white" :class="{ 'invalid': passwordInvalid }" />
        <small v-if="passwordInvalid" class="invalid-text">{{$t('AccountAdminCreate.PasswordForbidden')}}</small>
      </div>
      <div class="input-container">
        <InputText type="text" :placeholder="$t('AccountAdminCreate.ConfirmPassword')" v-model="passwordConfirm" class="white" :class="{ 'invalid': passwordConfirmInvalid }" />
        <small v-if="passwordConfirmInvalid" class="invalid-text">{{$t('AccountAdminCreate.ConfirmPasswordSame')}}</small>
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