<script setup>
import ButtonLink from "@/components/ButtonLink.vue";
import { login } from "@/router/login";
import { ref } from "vue";

const styleInvalid = ref(false);
const login_username = ref();
const login_password = ref();
const errorMessage = ref("");

const attemptLogin = () => {
    login(login_username.value, login_password.value)
        .then((success) => {
        })
        .catch((error) => {
            styleInvalid.value = true;
            errorMessage.value = "Ungültige Anmeldedaten. Bitte versuche es erneut.";
        });
};
</script>


<template>
    <div class="login-container">
        <div class="logo-container">
            <img src="@/assets/Universität_Leipzig_logo.svg" alt="Logo" />
        </div>
        <div class="login-content">
            <h1 class="login-heading">ANMELDUNG</h1>

            <div class="input-container">
                <InputText type="text" placeholder="Benutzername" v-model="login_username" class="white"
                    :class="{ 'invalid': styleInvalid }" />

                <InputText type="password" placeholder="Passwort" v-model="login_password" class="white"
                    :class="{ 'invalid': styleInvalid }" @keydown.enter.prevent="attemptLogin" />
            </div>

            <div class="button-container">
                <ButtonLink @click="attemptLogin" class="button-login">Anmelden</ButtonLink>
            </div>
            <small v-if="styleInvalid" class="invalid-text">{{ errorMessage }}</small>
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
