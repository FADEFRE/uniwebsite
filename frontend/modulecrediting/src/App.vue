<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { ref, provide } from 'vue'
import axios from "axios"

let courseData = ref()
axios.get('http://localhost:8090/courses-leipzig')
    .then(response => {
      console.log(response.data)
      courseData.value = response.data
    })
// todo error catching

provide('courseData', courseData)
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

  border-width: 1px;
  border-color: #aaaaaa;
  border-bottom-style: solid;
}

.logo {
  height: 100%;
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
