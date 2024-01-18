<script setup>
import TheNavigation from "@/components/TheNavigation.vue";
import TheLanguageSelection from "@/components/TheLanguageSelection.vue";
import { onMounted } from "vue";

onMounted(() => {
    document.cookie='locale=DE'
})
</script>

<template>
  <div>
    <header class="header-background">
      <div class="header-container">
        <a href="/" class="logo">
          <img class="logo-responsive" src="./assets/Universität_Leipzig_logo.svg" alt="Logo der Universität Leipzig" />
        </a>
        <TheLanguageSelection />
          <TheNavigation />
      </div>
    </header>
    <router-view class="main-content" />
  </div>
</template>

<style lang="scss">
@import './assets/mixins.scss';
@import './assets/variables.scss';
@import './assets/main.scss';

.header-background {
  background-color: $white;
  
}

.header-container {

  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;

  background-image: url(assets/icons/HeaderBackground.svg);
  background-repeat: no-repeat;
  background-size: contain;
  background-position: right;

  
}

.logo {
  background-color: $white;
  padding: 0;
  margin: 0;

}

.logo-responsive {
  width: 27.42188rem;
  height: 11.25rem;
}
</style>