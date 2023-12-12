<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

const user = ref()
const styleInvalid = ref(false)
const password = ref()

const router = useRouter()

const login = () => {
  if (user.value === 'studienbüro' && password.value) {

    const routeData = router.resolve({name: 'studyOfficeSelection'})
    window.open(routeData.href, '_top')

  } else if (user.value === 'pav' && password.value) {

    const routeData = router.resolve({name: 'chairmanSelection'})
    window.open(routeData.href, '_top')

  } else {
    styleInvalid.value = true;
  }
}
</script>

<template>
  <div class="view-container">
    <div class="login-container">
      <div class="login-content">

        <h1 class="login-heading">Login</h1>

        <div class="input-container">
          <div class="input-box">
          <span class="p-input-icon-right">
            <input type="text" placeholder="Benutzername" v-model="user" class="input-text" :class="{'invalid': styleInvalid}" />
          </span>
          </div>

          <div class="input-box">
          <span class="p-input-icon-right">
            <input type="password" placeholder="Passwort" v-model="password" class="input-text" :class="{'invalid': styleInvalid}"/>
          </span>
          </div>
        </div>

        <div class="button-container">
          <button @click="login" class="button-login">Anmelden</button>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>
.view-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.login-container {
  padding: 20px;
  border-width: 1px;
  border-radius: 20px;
  border-style: solid;

  background-color: #f8f8f8;
}

.login-heading {
  text-align: center;
}

.input-container {
  margin: 50px 0 30px 0;
}

.input-box {
  width: 100%;
  margin: 10px;
}

.input-text {
  width: 14vw;
  font-size: 16px;
  padding: 5px;
  border-color: black;
  border-radius: 3px;
}

.invalid {
  border-style: solid;
  border-width: 1px;
  border-color: #d8413f;
}

.button-container {
  width: auto;
  display: flex;
  justify-content: center;
  margin: 30px 0 10px 0;
}

.button-login {
  width: 10vw;
  padding: 5px;

  font-size: 16px;
  background-color: #d8413f;

  outline: none;
  border-style: solid;
  border-width: 1px;
  border-radius: 100px;
  border-color: #000000;
}
</style>