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
            <h1 class="login-heading">ANMELDUNG</h1>

            <div class="input-container">
                <div class="input-box">
                    <span class="p-input-icon-right">
                        <input type="text" placeholder="Benutzername" v-model="user" class="input-text"
                        :class="{ 'invalid': styleInvalid }" />
                    </span>
                </div>
                <div class="input-box">
                    <span class="p-input-icon-right">
                        <input type="password" placeholder="Passwort" v-model="password" class="input-text"
                        :class="{ 'invalid': styleInvalid }" />
                    </span>
                </div>
            </div>

                <div class="button-container">
                    <Button @click="login" class="button-login">Anmelden</Button>
                </div>

                <p v-if="styleInvalid" class="error-message">Ungültige Anmeldeinformationen. Bitte versuche es erneut.</p>
            </div>
        </div>
</template>

<style scoped>
.logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 200px;
   
}

.logo-container img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 100vh;
   
}

.login-content {
}
</style>
