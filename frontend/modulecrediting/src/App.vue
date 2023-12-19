<<<<<<< HEAD
<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
=======
<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <img src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" class="logo" />
        <div class="spacer"></div>
        <div class="router-container">
          <router-link to="/" class="router-button">Antrag</router-link>
          <router-link to="/status" class="router-button">Status</router-link>
        </div>
      </div>
    </header>
    <router-view class="main-content" />
  </div>
>>>>>>> develop
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
<<<<<<< HEAD
header {
  line-height: 1.5;
  max-height: 100vh;
=======
.header-container {
  height: 7vh;
  display: flex;
  align-items: center;
  border-width: 1px;
  border-color: #aaaaaa;
  border-bottom-style: solid;
  background-color: white;
>>>>>>> develop
}


.header-background {
  background-image: none;

}


.logo {
<<<<<<< HEAD
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
=======
  height: 150%;
  width: auto;
}

.spacer {
  flex: 1;
>>>>>>> develop
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
<<<<<<< HEAD
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
=======
  padding: 10px 20px;
  background-color: #b02f2c;
  color: #fff;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  margin-left: 15px;
>>>>>>> develop
}
</style>
