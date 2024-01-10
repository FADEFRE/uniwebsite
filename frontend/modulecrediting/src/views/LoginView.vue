<script>
import router from "../router";
import httpResource from "../http/httpResource";
import { parseApierror, performLogout, getAuthenticatedUser} from "../util/utils";


export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: ""
      },
      displayErrorMessage: false,
      errorMessage: "",
      loginInProcess: false
    };
  },
  methods: {
    async login() { //TODO: login wird noch als "" übergeben
      this.loginInProcess = true;
      let canNavigate = false;
      const loginRequest = {
        username: this.loginForm.username,
        password: this.loginForm.password
      };
      try {
        const response = await httpResource.post("/auth/login", loginRequest);
        if (response.status === 200) {
          await getAuthenticatedUser();
          canNavigate = true;
          //Correct routing for usernames -> get request api
          if (loginRequest.username === "studyoffice") {
            const routeData = router.resolve({name: 'studyOfficeSelection'})
            window.open(routeData.href, '_top')
          }
        }
      } catch (error) {
        performLogout();
        const apierror = parseApierror(error);
        this.displayErrorMessage = true;
        this.errorMessage = apierror.message;
      }
      this.loginInProcess = false;

      if (canNavigate) {
        router.replace("/");
      }
    }
  }
};


</script>

<template>
  <div class="view-container">
    <div class="login-container">
      <div class="login-content">

        <h1 class="login-heading">Login</h1>

        <div class="input-container">
          <div class="input-box">
          <span class="p-input-icon-right">
            <input type="text" placeholder="Benutzername" v-model="loginForm.username" class="input-text" :class="{'invalid': styleInvalid}" />
          </span>
          </div>

          <div class="input-box">
          <span class="p-input-icon-right">
            <input type="password" placeholder="Passwort" v-model="loginForm.password" class="input-text" :class="{'invalid': styleInvalid}"/>
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