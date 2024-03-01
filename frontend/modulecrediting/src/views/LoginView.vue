<script setup>
import { ref } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import { login } from "@/router/login";

const login_username = ref()
const login_password = ref()

const usernameEmpty = ref()
const passwordEmpty = ref()
const styleInvalid = ref(false)

const attemptLogin = () => {
  styleInvalid.value = false
  usernameEmpty.value = !login_username.value
  passwordEmpty.value = !login_password.value
  if (!usernameEmpty.value && !passwordEmpty.value) {
    login(login_username.value, login_password.value)
        .catch(_ => styleInvalid.value = true)
  }
}
</script>


<template>
  <div class="main centered">
    <div class="login-container">
      <div class="logo-container">
        <img src="@/assets/Universität_Leipzig_logo.svg" :alt="$t('LoginView.Logo')" />
      </div>
      <div class="login-content">
        <h1 class="login-heading">{{ $t('LoginView.LogIn') }}</h1>

        <div class="input-container">
          <InputText type="text" :placeholder="$t('LoginView.Username')" v-model="login_username" class="white"
                     :class="{ 'invalid': usernameEmpty || styleInvalid }" />
          <small v-if="usernameEmpty" class="invalid-text">{{ $t('LoginView.UsernameEmpty') }}</small>

          <InputText type="password" :placeholder="$t('LoginView.Password')" v-model="login_password" class="white"
                     :class="{ 'invalid': passwordEmpty || styleInvalid }" @keydown.enter.prevent="attemptLogin" />
          <small v-if="passwordEmpty" class="invalid-text">{{ $t('LoginView.PasswordEmpty') }}</small>
        </div>

        <div class="button-container">
          <ButtonLink @click="attemptLogin" class="button-login">{{ $t('LoginView.login') }}</ButtonLink>
        </div>
        <small v-if="styleInvalid" class="invalid-text">{{ $t('LoginView.InvalidData') }}</small>
      </div>
    </div>
  </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.login-container {
  @include singleContainer();
}

.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 27rem;
  height: 11rem;

  @include breakpoint(l) {
    width: 21rem;
    height: 5rem;
  }
}

.login-content {
  display: flex;
  width: 100%;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: spacing(l);
}
</style>
