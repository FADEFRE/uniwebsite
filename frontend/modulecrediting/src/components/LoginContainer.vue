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
            <h2 class="login-heading">ANMELDUNG</h2>

            <div class="input-container">
                <InputText type="text" placeholder="Benutzername" v-model="login_username" class="input-text"
                    :class="{ 'invalid': styleInvalid }" />

                <InputText type="password" placeholder="Passwort" v-model="login_password" class="input-text"
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

.login-container {
    @include singleContainer();
}

.logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 27rem;
    height: 11rem;
}

.login-content {
    display: flex;
    width: 100%;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.5rem;
}

.input-container {
    display: flex;
    width: 100%;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.5rem;

    & .p-inputtext {
        width: 100%;
        &:hover {
            background-color: $white-hover;
        }
    }
}

.invalid {
  border: 2px solid $red;
}

.invalid-text {
  color: $red;
}

</style>
