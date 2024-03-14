<script setup>
import { ref } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import RoleDropdown from "@/components/util/RoleDropdown.vue";
import { postNewUser } from "@/requests/user-requests";
import { usernameRegex, passwordRegex } from "@/config/regex";

/*
create a new user with a specific role
 */

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

    <h2>Benutzer erstellen</h2>
    <div class="first-row">
      <div class="input-container">
        <InputText type="text" placeholder="Benutzername" v-model="username" class="button-height white" :class="{ 'invalid': usernameInvalid || createFailed }" />
        <small v-if="usernameInvalid" class="invalid-text">Dieser Benutzername ist nicht erlaubt</small>
        <small v-if="createFailed" class="invalid-text">Es existiert bereits ein Benutzer mit diesem Namen</small>
      </div>
      <RoleDropdown v-model="role" :invalid="roleEmpty" />
    </div>
    <div class="second-row">
      <div class="input-container">
        <InputText type="text" placeholder="Passwort" v-model="password" class="white" :class="{ 'invalid': passwordInvalid }" />
        <small v-if="passwordInvalid" class="invalid-text">Passwort ist nicht erlaubt</small>
      </div>
      <div class="input-container">
        <InputText type="text" placeholder="Passwort bestätigen" v-model="passwordConfirm" class="white" :class="{ 'invalid': passwordConfirmInvalid }" />
        <small v-if="passwordConfirmInvalid" class="invalid-text">Die Passwörter müssen übereinstimmen</small>
      </div>
      
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