<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <a href="/" class="logo">
          <img class="logo-responsive" src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" />
        </a>
        <v-btn @click="$i18n.locale = 'EN'" class="language-button">EN</v-btn>
        <v-btn @click="$i18n.locale = 'DE'" class="language-button">DE</v-btn>
        <div class="router-container">
          <TheNavigation />
        </div>
      </div>
    </header>
    <router-view class="main-content" />
  </div>
</template>

<style lang="scss">
@import './assets/mixins.scss';
@import './assets/variables.scss';
@import './assets/main.scss';

.header-container {

  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;


  background-color: $white;
}

.logo {
  background-color: $white;
}

.logo-responsive {
  width: 27.42188rem;
  height: 11.25rem;
}

.language-button {
    background-color: grey;
    width: 2.54cm;
    height: 1cm;

    &:hover {
        cursor: pointer;
        background-color: darkgrey;
    }

    &:active {
        transform: scale(0.95);
    }
}




</style>

<script setup>
import TheNavigation from "@/components/TheNavigation.vue";
import { onMounted } from "vue";

onMounted(() => {
    document.cookie='locale=DE'
})
</script>