
<script setup>
import router from "../router";
import httpResource from "../http/httpResource";
import { parseApierror, performLogout, getAuthenticatedUser} from "../util/utils";
import { ref } from "vue";

const login_username = ref();
const login_password = ref();
const displayErrorMessage = ref();
const errorMessage = ref();
const loginInProcess = ref();

async function login () {
  loginInProcess.value = true;
  let canNavigate = false;
  const loginRequest = {
    username: login_username.value,
    password: login_password.value
  };
  try {
    console.log(loginRequest)
    const response = await httpResource.post("/auth/login", loginRequest);
    console.log(response)
    if (response.status === 200) {
      await getAuthenticatedUser();
      canNavigate = true;
      const routeData = router.resolve({name: 'studyOfficeSelection'})
      window.open(routeData.href, '_top')
      //Correct routing for usernames -> get request api
      if (loginRequest.username === "studyoffice") {
        const routeData = router.resolve({name: 'studyOfficeSelection'})
        window.open(routeData.href, '_top')
      }
    }
  } catch (error) {
    performLogout();
    const apierror = parseApierror(error);
    displayErrorMessage.value = true;
    errorMessage.value = apierror.message;
  }
  loginInProcess.value = false;

  if (canNavigate) {
    router.replace("/");
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
            <input type="text" placeholder="Benutzername" v-model="login_username" class="input-text" />
          </span>
          </div>

          <div class="input-box">
          <span class="p-input-icon-right">
            <input type="password" placeholder="Passwort" v-model="login_password" class="input-text" />
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