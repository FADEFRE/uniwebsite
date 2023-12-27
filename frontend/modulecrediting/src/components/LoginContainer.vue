<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

const user = ref('');
const styleInvalid = ref(false);
const password = ref('');

const router = useRouter();

const login = () => {
    if (user.value === 'studienbüro' && password.value) {
        const routeData = router.resolve({ name: 'studyOfficeSelection' });
        window.open(routeData.href, '_top');
    } else if (user.value === 'pav' && password.value) {
        const routeData = router.resolve({ name: 'chairmanSelection' });
        window.open(routeData.href, '_top');
    } else {
        styleInvalid.value = true;
    }
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
                <input type="text" placeholder="Benutzername" v-model="user" class="input-text"
                    :class="{ 'invalid': styleInvalid }" />

                <input type="password" placeholder="Passwort" v-model="password" class="input-text"
                    :class="{ 'invalid': styleInvalid }" />
            </div>

            <div class="button-container">
                <Button @click="login" class="button-login">Anmelden</Button>
            </div>

            <p v-if="styleInvalid" class="error-message">Ungültige Anmeldeinformationen. Bitte versuche es erneut.</p>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.login-container {
    width: fit-content;
    background-color: $white;
    display: flex;
    padding: 4.375rem;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.625rem;
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

input {
    width: 100%;
    padding: 0.625rem;

    &:focus {
        background-color: $gray;
    }
}
</style>
