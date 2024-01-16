<script setup>
import { login } from "../router/login";
import { ref } from "vue";
const styleInvalid = ref(false);

const login_username = ref();
const login_password = ref();

</script>

<template>
    <div class="login-container">
        <div class="logo-container">
            <img src="@/assets/Universität_Leipzig_logo.svg" alt="Logo" />
        </div>
        <div class="login-content">
            <h2 class="login-heading">ANMELDUNG</h2>

            <div class="input-container">
                <input type="text" placeholder="Benutzername" v-model="login_username" class="input-text"
                    :class="{ 'invalid': styleInvalid }" />

                <input type="password" placeholder="Passwort" v-model="login_password" class="input-text"
                    :class="{ 'invalid': styleInvalid }" />
            </div>

            <div class="button-container">
                <Button @click="login(login_username, login_password)" class="button-login">Anmelden</Button>
            </div>

            <p v-if="styleInvalid" class="error-message">Ungültige Anmeldeinformationen. Bitte versuche es erneut.</p>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.login-container {
    @include singleContainer();
}

.logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 27.42188rem;
    height: 11.25rem;
}

.login-content {
    display: flex;
    width: 100%;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.5625rem;
}

.input-container {
    display: flex;
    width: 100%;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.625rem;
}
</style>
