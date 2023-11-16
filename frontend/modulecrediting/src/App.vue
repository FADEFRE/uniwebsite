<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <div class="header-container">

      <img src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" class="logo"/>

      <div class="router-container">
        <router-link to="/" class="router-button">Antrag</router-link>
        <router-link to="/test" class="router-button">Status</router-link>
      </div>

    </div>
  </header>

  <RouterView />
</template>

<style scoped>
.header-container {
  height: 7vh;
  display: flex;
}

.logo {
  height: 100%;
}

.router-container {
  display: inline;
  justify-content: right;
}

.router-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #3498db; /* Set your desired background color */
  color: #fff; /* Set your desired text color */
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}
</style>
