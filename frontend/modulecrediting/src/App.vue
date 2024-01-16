<script setup>

import router from "./router";
import httpResource from "./scripts/httpResource"
import { performLogout } from "./scripts/utils"

async function logout () {
  const response = await httpResource.post("/api/auth/logout");
  console.log(response)
  performLogout();
  const routeData = router.resolve({name: 'login'})
  window.open(routeData.href, '_top')
}

</script>




<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <a href="/" class="logo">
          <img class="logo-responsive" src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" />
        </a>
        <div class="spacer"></div>
        <div class="router-container">
          <router-link to="/" class="router-button">Antrag</router-link>
          <router-link to="/status" class="router-button">Status</router-link>
          <Button @click="logout">Logout</Button>
        </div>
      </div>
    </header>
    <router-view class="main-content" />
  </div>
</template>

<style>



.view-container {
  margin: 10px 20px 0;
}

body {
  margin: 0;
  padding: 0;
  background-image: url(./assets/Universität_Leipzig_background.svg);
  background-size: contain;
  background-position: left;
  background-repeat: no-repeat;
  background-position-y: 50px;

  background-attachment: fixed;

}
</style>

<style scoped>
.header-container {
  height: 7vh;
  display: flex;
  align-items: center;
  border-width: 1px;
  border-color: #aaaaaa;
  border-bottom-style: solid;
  background-color: white;
}


.header-background {
  background-image: none;

}

.logo {
  background-color: white;
}

.logo {
  height: 150%;
  width: auto;
}

.spacer {
  flex: 1;
}

.router-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #b02f2c;
  color: #fff;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  margin-left: 15px;
}
</style>
